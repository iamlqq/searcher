package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class AddCustomJ {
	public static final String tag = NavigationTwoJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;
	public List<NavigationTwo> list = new ArrayList<NavigationTwo>();
	public List<RulesNavigationTwo> list1 = new ArrayList<RulesNavigationTwo>();;

	public AddCustomJ(String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
					appPacks = json.getJSONArray("appPacks");
					for(int i = 0; i < appPacks.length(); i++){
						NavigationTwo data = new NavigationTwo();
						data.naviTwoName = appPacks.getJSONObject(i).optString("name");
						data.naviTwoapps = appPacks.getJSONObject(i)
								.getJSONArray("apps");
						for(int j = 0; j < data.naviTwoapps.length(); j++){
							RulesNavigationTwo rule = new RulesNavigationTwo();
							rule.id = data.naviTwoapps.getJSONObject(j).optInt("id");
							rule.icon = data.naviTwoapps.getJSONObject(j).optString("imgPath");
							rule.naviName = data.naviTwoapps.getJSONObject(j).optString("name");
							rule.link = data.naviTwoapps.getJSONObject(j).optString("link");
							list1.add(rule);
						}
						list.add(data);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				SearchUtil.logInfo(tag, e.getMessage());
			}

		}
	}
}
