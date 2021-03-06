package api.service;

import api.util.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeatService {
	private static final String STANDARD_TIME = "12:00";

	private String getKey(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		Date date = Util.string2Date(dateStr, Util.TIME_FORMAT1);
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String key;
		if (minute >= 0 && minute < 60) {
			key = String.format("%02d:%02d", hour, 0);
		} else {
			key = String.format("%02d:%02d", hour, 30);
		}
		return key;
	}

	public List<Map.Entry<String, Float>> getSortedKeyList(Map<String, Float> map) {
		List<Map.Entry<String, Float>> keyList = new ArrayList<>(map.entrySet());
		Collections.sort(keyList, new Comparator<Map.Entry<String, Float>>() {
			@Override
			public int compare(Map.Entry<String, Float> o1,
							   Map.Entry<String, Float> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		List<Map.Entry<String, Float>> newKeyList = new ArrayList<>();
		List<Map.Entry<String, Float>> beforeStandardTimeKeyList = new ArrayList<>();
		List<Map.Entry<String, Float>> afterStandardTimeKeyList = new ArrayList<>();
		for (Map.Entry<String, Float> entry : keyList) {
			String key = entry.getKey();
			if (key.compareTo(STANDARD_TIME) < 0) {
				beforeStandardTimeKeyList.add(entry);
			} else {
				afterStandardTimeKeyList.add(entry);
			}
		}
		newKeyList.addAll(afterStandardTimeKeyList);
		newKeyList.addAll(beforeStandardTimeKeyList);
		return newKeyList;
	}

	public JSONArray getHeat() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();
		if (Util.readRecord(false, titleList, dataList)) {
			for (int i = 1; i < titleList.size(); i++) {
				Map<String, Float> map = new HashMap<>();
				for (int j = 0; j < dataList.size(); j++) {
					String[] data = Util.cut(dataList.get(j));
					String key = this.getKey(data[0]);
					String tmp = data[i];
					if (!tmp.isEmpty()) {
						if (map.containsKey(key)) {
							map.put(key, map.get(key) + Float.valueOf(tmp));
						} else {
							map.put(key, Float.valueOf(tmp));
						}
					}
				}
				List<Map.Entry<String, Float>> newKeyList = this.getSortedKeyList(map);
				for (Map.Entry<String, Float> entry : newKeyList) {
					String key = entry.getKey();
					float v = entry.getValue();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("y", titleList.get(i));
					jsonObject.put("x", key);
					jsonObject.put("v", v);
					jsonArray.add(jsonObject);
				}
			}
		}
		return jsonArray;
	}
}
