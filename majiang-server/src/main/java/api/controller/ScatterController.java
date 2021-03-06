package api.controller;

import api.service.ScatterService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ScatterController {
	@Resource
	private ScatterService service;

	@RequestMapping(value = "/scatter", method = RequestMethod.GET)
	public JSONArray getScatter() {
		return service.getScatter();
	}
}
