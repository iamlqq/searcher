package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class SearchSauseJ {
	public static final String tag = SearchSauseJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;
	public List<SearchSauseList> list = new ArrayList<SearchSauseList>();
	public List<RulesSearchSause> list1 = new ArrayList<RulesSearchSause>();

	public SearchSauseJ(String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
					appPacks = json.getJSONArray("appPacks");
					for(int i = 0; i < appPacks.length(); i++){
						SearchSauseList data = new SearchSauseList();
						data.name = appPacks.getJSONObject(i).optString("name");
						data.apps = appPacks.getJSONObject(i)
								.getJSONArray("apps");
						for(int j = 0; j < data.apps.length(); j++){
							RulesSearchSause rule = new RulesSearchSause();
							rule.id = data.apps.getJSONObject(j).optInt("id");
							rule.icon = data.apps.getJSONObject(j).optString("imgPath");
							rule.sauseName = data.apps.getJSONObject(j).optString("name");
							rule.link = data.apps.getJSONObject(j).optString("link");
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
