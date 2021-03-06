package api.controller;

import api.service.BarService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BarController {
	@Resource
	private BarService service;

	@RequestMapping(value = "/dynamicbar", method = RequestMethod.GET)
	public JSONObject getDynamicBar() {
		return service.getDynamicBar();
	}

}
