package api.service;

import java.util.*;

import api.util.Util;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

@Service
public class WaterFallService {

	public JSONArray getWaterFall() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		if (Util.readRecord(true, titleList, dataList)) {
			for (int i = 1; i < titleList.size(); i++) {
				JSONObject personJsonObject = new JSONObject();
				JSONArray dataJsonArray = new JSONArray();
				//
				for (int j = 0; j < dataList.size(); j++) {
					String[] data = Util.cut(dataList.get(j));
					Date date = Util.string2Date(data[0], Util.TIME_FORMAT1);
					String dateStr = Util.date2String(date, Util.DATE_FORMAT);
					String tmp = data[i];
					//
					JSONObject dataJsonObject = new JSONObject();
					if (tmp.isEmpty()) {
//						dataJsonObject.put("y", "'-'");
					} else {
						dataJsonObject.put("y", Float.valueOf(tmp));
						dataJsonObject.put("x", dateStr);
						dataJsonArray.add(dataJsonObject);
					}
				}
				personJsonObject.put("name", titleList.get(i));
				personJsonObject.put("data", dataJsonArray);
				jsonArray.add(personJsonObject);
			}
		}
		return jsonArray;
	}
}
