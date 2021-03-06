package api.controller;

import api.service.HeatService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HeatController {
	@Resource
	private HeatService service;

	@RequestMapping(value = "/heat", method = RequestMethod.GET)
	public JSONArray getHeat() {
		return service.getHeat();
	}
}
