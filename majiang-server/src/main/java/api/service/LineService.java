package api.service;

import api.util.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LineService {
	/*public JSONArray getLine() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		if (Util.readRecord(titleList, dataList)) {
			for (int i = 0; i < titleList.size(); i++) {
				if (i > 0) {
					System.out.print(Util.SPLIT);
				}
				System.out.print(titleList.get(i));
			}
			System.out.print("\n");

			// 总览散点图
			for (int i = 0; i < dataList.size(); i++) {
				String data = dataList.get(i);
				int index = data.indexOf(Util.SPLIT);
				Date date = Util.string2Date(data.substring(0, index - 1), Util.TIME_FORMAT);
				String dateStr = Util.date2String(date, Util.DATE_FORMAT);
				System.out.print(dateStr);
				System.out.print(data.substring(index));
				System.out.print("\n");
			}
		}
		return jsonArray;
	}*/

	public JSONArray getLine() {
		List<String> titleList = new ArrayList<>();
		List<String> dataList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		if (Util.readRecord(true, titleList, dataList)) {
			for (int i = 1; i < titleList.size(); i++) {
				float sum = 0;
				for (int j = 0; j < dataList.size(); j++) {
					String[] data = Util.cut(dataList.get(j));
					String tmp = data[i];
					JSONObject lineJsonObject = new JSONObject();
					lineJsonObject.put("name", titleList.get(i));
					lineJsonObject.put("x", data[0]);
					if (!tmp.isEmpty()) {
						sum += Float.valueOf(tmp);
						lineJsonObject.put("y", sum);
						jsonArray.add(lineJsonObject);
					} else {
						lineJsonObject.put("y", null);
						jsonArray.add(lineJsonObject);
					}
				}
			}
		}
		return jsonArray;
	}
}
