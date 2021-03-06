package api.service;

import api.util.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarService {

	public JSONObject getDynamicBar() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONObject totalJsonObject = new JSONObject();
		JSONArray timeJsonArray = new JSONArray();
		JSONArray dataJsonArray = new JSONArray();

		if (Util.readRecord(true, titleList, dataList)) {
			List<Float> sumList = new ArrayList<>();
			for (int i = 0; i < titleList.size() - 1; i++) {
				sumList.add(0.0f);
			}
			for (int i = 0; i < dataList.size(); i++) {
				String[] data = Util.cut(dataList.get(i));
				JSONArray jsonArray = new JSONArray();
				for (int j = 1; j < titleList.size(); j++) {
					String name = titleList.get(j);
					int index = j - 1;
					String tmp = data[j];
					if (!tmp.isEmpty()) {
						sumList.set(index, sumList.get(index) + Float.valueOf(tmp));
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", name);
					jsonObject.put("value", sumList.get(index));
					jsonArray.add(jsonObject);
				}
				timeJsonArray.add(data[0]);
				dataJsonArray.add(jsonArray);
			}
			totalJsonObject.put("time", timeJsonArray);
			totalJsonObject.put("data", dataJsonArray);
		}
		return totalJsonObject;
	}
}
