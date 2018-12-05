package parkingos.com.bolink.orderserver;

import com.zld.proto.*;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.models.OrderTb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class OrderServer extends BaseServer {
    private Logger logger = LoggerFactory.getLogger(OrderServer.class);


//    @RequestMapping(method = RequestMethod.GET, value = "/getList")
    public List<OrderTb> getOrdersByMapConditons(Map<String, String> map) {
//        Order u = null;
//        MapList u = null;
        OrderList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.getOrdersByMapConditons(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> orderTbList = new ArrayList<>();
        if(u!=null) {
            for (Order order : u.getOrderList()) {
                OrderTb orderTb = getOrderTbFromOrder(order);
                orderTbList.add(orderTb);
            }
        }
//        return ResponseEntity.ok("out opark Order: 订单" + u.toString());
        return orderTbList;
    }

    private OrderTb getOrderTbFromOrder(Order order) {
        OrderTb orderTb = new OrderTb();
        orderTb.setComid(order.getComid());
        orderTb.setCarNumber(order.getCarNumber());
        orderTb.setId(order.getId());
        orderTb.setState(order.getState());
        orderTb.setOrderIdLocal(order.getOrderIdLocal());
        orderTb.setCarpicTableName(order.getCarpicTableName());
        orderTb.setCarType(order.getCarType());
        if(order.getEndTime()>0){
            orderTb.setEndTime(order.getEndTime());
        }
        orderTb.setAmountReceivable(new BigDecimal(order.getAmountReceivable()+""));
        orderTb.setElectronicPrepay(new BigDecimal(order.getElectronicPrepay()+""));
        orderTb.setElectronicPay(new BigDecimal(order.getElectronicPay()+""));
        orderTb.setCashPrepay(new BigDecimal(order.getCashPrepay()+""));
        orderTb.setCashPay(new BigDecimal(order.getCashPay()+""));
        orderTb.setReduceAmount(new BigDecimal(order.getReduceAmount()+""));
        orderTb.setCreateTime(order.getCreateTime());
        orderTb.setTotal(new BigDecimal(order.getTotal()+""));
        orderTb.setUin(order.getUin());
        orderTb.setcType(order.getCType());
        orderTb.setOutUid(order.getOutUid());
        orderTb.setUid(order.getUid());
        orderTb.setPayType(order.getPayType());
        orderTb.setInPassid(order.getInPassid());
        orderTb.setOutPassid(order.getOutPassid());
        orderTb.setFreereasons(order.getFreereasons());
        return orderTb;
    }


    public int selectOrdersCount(Map<String, String> map) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.selectOrdersCount(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public Map<String,String> selectMoneyByExample(Map<String, String> map) {
        OrderMap u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.selectMoneyByExample(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        return u.getMapMap();
    }


    public List<OrderTb> qryOrdersByComidAndOrderId(Long comid,String orderid,String tableName) {
        OrderList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setOrderIdLocal(orderid)
                        .setTableName(tableName)
                        .build();
                u = stub.qryOrdersByComidAndOrderId(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> list = new ArrayList<>();
        if(u!=null) {
            for (Order order : u.getOrderList()) {
                OrderTb orderTb = getOrderTbFromOrder(order);
                list.add(orderTb);
            }
        }
        return list;
    }


    public List<Map<String,String>> selectCityDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }


    public List<Map<String,String>> selectCityParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityParkDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> selectCityMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityMonthAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }


    public List<Map<String,String>> selectParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> selectParkMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkMonthAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> selectParkCollectorAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkCollectorAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public int resetDataByComid(Long comid,String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .build();
                u = stub.resetDataByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
            return u.getCount();
        }
        else{
            logger.error("server is down!!");
            return -1;
        }

    }

    public OrderTb getSelectOrder(long l, String carNumber, String tableName) {
        Order u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(l)
                        .setCarNumber(carNumber)
                        .setTableName(tableName)
                        .build();

                u = stub.getSelectOrder(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        OrderTb orderTb = getOrderTbFromOrder(u);
        return orderTb;
    }

    public List<OrderTb> getOrdersByCars(long comid, List<String> carNumberList, String tableName) {
        OrderList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderCars order = OrderCars.newBuilder()
                        .setComid(comid)
                        .addAllCarNumber(carNumberList)
                        .setTableName(tableName)
                        .build();//getOrderCars(comid,carNumberList,tableName);
                logger.info("===OrderCars"+order);
                u = stub.getOrdersByCars(order);
                logger.info("===OrderCars"+u);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> list = new ArrayList<>();
        if(u!=null) {
            for (Order order : u.getOrderList()) {
                OrderTb orderTb = getOrderTbFromOrder(order);
                list.add(orderTb);
            }
        }
        return list;
    }

    private OrderCars getOrderCars(long comid, List<String> carNumberList, String tableName) {
        logger.info("====carNumberList"+carNumberList+"~~"+carNumberList.size());
        if(carNumberList!=null &&carNumberList.size()==1) {
            OrderCars order = OrderCars.newBuilder()
                    .setComid(comid)
                    .setCarNumber(0, carNumberList.get(0))
                    .setTableName(tableName)
                    .build();
            return order;
        }

        if(carNumberList!=null &&carNumberList.size()==2) {
            OrderCars order = OrderCars.newBuilder()
                    .setComid(comid)
                    .setCarNumber(0, carNumberList.get(0))
                    .setCarNumber(1, carNumberList.get(1))
                    .setTableName(tableName)
                    .build();
            return order;
        }
        if(carNumberList!=null &&carNumberList.size()==3) {
            OrderCars order = OrderCars.newBuilder()
                    .setComid(comid)
                    .setCarNumber(0, carNumberList.get(0))
                    .setCarNumber(1, carNumberList.get(1))
                    .setCarNumber(2, carNumberList.get(2))
                    .setTableName(tableName)
                    .build();
            return order;
        }
        if(carNumberList!=null &&carNumberList.size()==2) {
            OrderCars order = OrderCars.newBuilder()
                    .setComid(comid)
                    .setCarNumber(0, carNumberList.get(0))
                    .setCarNumber(1, carNumberList.get(1))
                    .setCarNumber(2, carNumberList.get(2))
                    .setCarNumber(3, carNumberList.get(3))
                    .setTableName(tableName)
                    .build();
            return order;
        }
        return null;
    }

    public List<Map<String,String>> getEntryCar(long tday, long l, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(l)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCar(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getExitCar(long tday, long l, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(l)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCar(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getParkRank(long tday, int groupid, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getParkRank(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        logger.info("getParkRank~~~~~~~~~"+list);
        return list;
    }

    public int getEntryCount(long tday, int groupid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public int getExitCount(long tday, int groupid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public int getInparkCount(long tday, int groupid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getInparkCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public List<Map<String,String>> getEntryCarByComid(long tday, int comid, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCarByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        logger.info("get maplist:"+u);
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getExitCarByComid(long tday, int comid, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCarByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        logger.info("get maplist:"+u);
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getRankByout(long tday, int comid, String tableName) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getRankByout(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public int getEntryCountbc(long tday, int comid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }

    public int getExitCountbc(long tday, int comid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }

    public int getInparkCountbc(long tday, int comid, String tableName) {
        OrderCount u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getInparkCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }
}