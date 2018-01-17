package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.EquipmentManageCameraService;
import parkingos.com.bolink.service.EquipmentManageChannelService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_channel")
public class EquipmentManageChannelAction {

	Logger logger = Logger.getLogger(EquipmentManageChannelAction.class);


	@Autowired
	private EquipmentManageChannelService equipmentManageChannelService;

	@RequestMapping(value = "/query")
	public String query(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		logger.info(reqParameterMap);

		JSONObject result = equipmentManageChannelService.selectResultByConditions(reqParameterMap);



		logger.info(result);
		StringUtils.ajaxOutput(response,result.toJSONString());
		return null;
	}
}