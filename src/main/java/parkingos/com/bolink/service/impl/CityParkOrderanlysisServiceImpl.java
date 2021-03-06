package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.CityParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityParkOrderanlysisServiceImpl implements CityParkOrderAnlysisService {

    Logger logger = Logger.getLogger(CityParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        String comidStr = reqmap.get("comid_start");


        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String nowtime= df2.format(System.currentTimeMillis());
        String sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
                "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
                "sum(reduce_amount) reduce_pay,comid from order_tb where comid";
        String free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,comid from order_tb where comid";
        String groupby = " group by comid";



        if(Check.isNumber(comidStr)){
            sql +=" = "+Long.parseLong(comidStr)+" and end_time ";
            free_sql +=" = "+Long.parseLong(comidStr)+" and end_time ";
        }else {
            List parkList = commonMethods.getParks(Long.parseLong(reqmap.get("groupid")));
            String preParams  ="";
            if(parkList!=null&&!parkList.isEmpty()){
                for(Object parkid : parkList){
                    if(preParams.equals(""))
                        preParams =parkid+"";
                    else
                        preParams += ","+parkid;
                }
                sql +=" in (" +preParams+" )  and end_time  ";
                free_sql +=" in ( "+preParams+" )  and end_time  ";
            }else{
                return result;
            }
        }


        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));

        Long btime = null;
        Long etime = null;
        String time = null;
        if(date==null||"".equals(date)){
            btime = TimeTools.getToDayBeginTime();
        }else {
            btime = Long.parseLong(date);
        }
        time = TimeTools.getTimeStr_yyyy_MM_dd(btime*1000);
        etime =btime+86399;

        logger.info("=====>>>>>>btime="+btime+"=====>>>etime="+etime);


        sql +=" between "+btime+" and "+etime;
        free_sql +=" between "+btime+" and "+etime;
        sql +=" and state= 1 and out_uid > -1 and ishd=0 ";
        free_sql +=" and state= 1 and out_uid >-1 and ishd=0 ";

        //总订单集合
        List<Map<String, Object>> totalList =commonDao.getObjectBySql(sql +groupby);
        //免费订单集合
        List<Map<String, Object>> freeList = commonDao.getObjectBySql(free_sql +" and pay_type=8 "+groupby);//pgOnlyReadService.getAllMap(free_sql +" and pay_type=8 group by out_uid,comid order by scount desc ",params);
        int totalCount = 0;//总订单数
        double totalMoney = 0.0;//订单金额
        double cashMoney = 0.0;//现金支付金额
        double elecMoney = 0.0;//电子支付金额
        double actFreeMoney = 0.0;//免费金额+减免支付
        double actRecMoney =0.0;//电子结算+现金结算
        List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
        if(totalList != null && totalList.size() > 0) {
            for (Map<String, Object> totalOrder : totalList) {
                if (totalOrder.containsKey("comid")) {
                    Long comid = (Long) totalOrder.get("comid");
                    List<Map<String, Object>> list = commonDao.getObjectBySql("select company_name from com_info_tb where id =" + comid);
                    if(list!=null&&list.size()>0){
                        totalOrder.put("comid", list.get(0).get("company_name"));
                    }else{
                        totalOrder.put("comid", "");
                    }
                }

                totalOrder.put("time",time);

                totalCount += Integer.parseInt(totalOrder.get("scount") + "");

                totalMoney += Double.parseDouble(totalOrder.get("amount_receivable") + "");

                //格式化应收
                totalOrder.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("amount_receivable")+""))));

                //现金支付
                cashMoney +=StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"));
                totalOrder.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"))));
                //电子支付
                elecMoney += StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"));
                totalOrder.put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"))));
                //每一行的合计 = 现金支付+电子支付
                totalOrder.put("act_total", String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("cash_pay")+"")+Double.parseDouble(totalOrder.get("electronic_pay")+""))));

                //减免支付
                double reduceAmount = StringUtils.formatDouble(Double.parseDouble((totalOrder.get("reduce_pay") == null ? "0.00" : totalOrder.get("reduce_pay") + "")));
                double actFreePay = reduceAmount;
                //遍历免费集合
                if (freeList != null && freeList.size() > 0) {
                    for (Map<String, Object> freeOrder : freeList) {
                        if(freeOrder.get("comid").equals(totalOrder.get("comid"))){
                            double freePay = StringUtils.formatDouble(Double.parseDouble((freeOrder.get("free_pay") == null ? "0.00" : freeOrder.get("free_pay") + "")));
                            actFreePay = freePay+reduceAmount;
                        }
                    }
                }
                actFreeMoney+=actFreePay;
                totalOrder.put("free_pay",  String.format("%.2f",actFreePay));
                backList.add(totalOrder);
            }
        }

        if(backList.size()>0){
            Map sumMap = new HashMap();
//            sumMap.put("time","合计");
            sumMap.put("comid","合计");
            sumMap.put("time",time);
            sumMap.put("scount",totalCount);
            sumMap.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(totalMoney)));
            sumMap.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(cashMoney)));
            sumMap.put("electronic_pay",String.format("%.2f",StringUtils.formatDouble(elecMoney)));
            sumMap.put("act_total",String.format("%.2f",StringUtils.formatDouble((cashMoney+elecMoney))));
            sumMap.put("free_pay",String.format("%.2f",StringUtils.formatDouble(actFreeMoney)));
            backList.add(sumMap);
        }

        result.put("rows",JSON.toJSON(backList));
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<Object> resList = JSON.parseArray(result.get("rows").toString());

        logger.error("=========>>>>>>.导出订单" + resList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (resList != null && resList.size() > 0) {
            for (Object object : resList) {
                Map<String,Object> map = (Map)object;
                List<Object> values = new ArrayList<Object>();
//                values.add(map.get("time"));
                values.add(map.get("comid"));
                values.add(map.get("time"));
                values.add(map.get("scount"));
                values.add(map.get("amount_receivable"));
                values.add(map.get("cash_pay"));
                values.add(map.get("electronic_pay"));
                values.add(map.get("act_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
