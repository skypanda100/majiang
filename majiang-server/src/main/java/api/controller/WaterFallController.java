package api.controller;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import api.service.WaterFallService;

@RestController
public class WaterFallController {
	@Resource
	private WaterFallService service;

	@RequestMapping(value = "/waterfall", method = RequestMethod.GET)
	public JSONArray getWaterFall() {
		return service.getWaterFall();
	}
}
