package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class NavigationOneJ {
	public static final String tag = NavigationTwoJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;
	public List<NavigationOne> list = new ArrayList<NavigationOne>();
//	public List<List<RulesNavigationOne>> allone = new ArrayList<>();
//	public List<RulesNavigationOne> list1;
	public List<RulesNavigationOne> list1 = new ArrayList<RulesNavigationOne>();

	public NavigationOneJ(String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
					appPacks = json.getJSONArray("appPacks");
					for(int i = 0; i < appPacks.length(); i++){
						NavigationOne data = new NavigationOne();
						data.naviOneName = appPacks.getJSONObject(i).optString("name");
						data.naviOneapps = appPacks.getJSONObject(i)
								.getJSONArray("apps");
						//list1 = new ArrayList<RulesNavigationOne>();
						for(int j = 0; j < data.naviOneapps.length(); j++){
							RulesNavigationOne rule = new RulesNavigationOne();
							rule.id = data.naviOneapps.getJSONObject(j).optInt("id");
							rule.icon = data.naviOneapps.getJSONObject(j).optString("imgPath");
							rule.naviName = data.naviOneapps.getJSONObject(j).optString("name");
							rule.link = data.naviOneapps.getJSONObject(j).optString("link");
							list1.add(rule);
						}
						//allone.add(list1);
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
