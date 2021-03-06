package api.controller;

import api.service.LineService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LineController {
	@Resource
	private LineService service;

	@RequestMapping(value = "/line", method = RequestMethod.GET)
	public JSONArray getLine() {
		return service.getLine();
	}
}
