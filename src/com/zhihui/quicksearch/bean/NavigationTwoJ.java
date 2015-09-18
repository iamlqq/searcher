package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class NavigationTwoJ {
	public static final String tag = NavigationTwoJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;
	public List<NavigationTwo> list = new ArrayList<NavigationTwo>();
	public List<List<RulesNavigationTwo>> alltwo = new ArrayList<>();
	public List<RulesNavigationTwo> list1;

	public NavigationTwoJ(String str) {
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
						list1 = new ArrayList<RulesNavigationTwo>();
						for(int j = 0; j < data.naviTwoapps.length(); j++){
							RulesNavigationTwo rule = new RulesNavigationTwo();
							rule.id = data.naviTwoapps.getJSONObject(j).optInt("id");
							rule.icon = data.naviTwoapps.getJSONObject(j).optString("imgPath");
							rule.naviName = data.naviTwoapps.getJSONObject(j).optString("name");
							rule.link = data.naviTwoapps.getJSONObject(j).optString("link");
							list1.add(rule);
						}
						alltwo.add(list1);
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
