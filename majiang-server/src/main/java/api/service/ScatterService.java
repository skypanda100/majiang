package api.service;

import api.util.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScatterService {
	public JSONArray getScatter() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		if (Util.readRecord(false, titleList, dataList)) {
			for (int i = 1; i < titleList.size(); i++) {
				JSONObject scatterJsonObject = new JSONObject();
				int x = 0;
				float y = 0;
				for (int j = 0; j < dataList.size(); j++) {
					String[] data = Util.cut(dataList.get(j));
					String tmp = data[i];
					if (!tmp.isEmpty()) {
						x++;
						y += Float.valueOf(tmp);
					}
				}
				scatterJsonObject.put("name", titleList.get(i));
				scatterJsonObject.put("x", x);
				scatterJsonObject.put("y", y);
				jsonArray.add(scatterJsonObject);
			}
		}
		return jsonArray;
	}
}
