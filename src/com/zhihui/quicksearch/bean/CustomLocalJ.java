package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class CustomLocalJ {
	public static final String tag = SearchSauseJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;
	public List<CustomList> list = new ArrayList<CustomList>();
	public List<RulesCustom> list1 = new ArrayList<RulesCustom>();
	RulesCustom cus;
	
	public CustomLocalJ(String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
					appPacks = json.getJSONArray("appPacks");
					for(int i = 0; i < appPacks.length(); i++){
						CustomList data = new CustomList();
						data.name = appPacks.getJSONObject(i).optString("name");
						data.packType = appPacks.getJSONObject(i).optString("packType");
						data.apps = appPacks.getJSONObject(i).getJSONArray("apps");
						for(int j = 0; j < data.apps.length(); j++){
							cus = new RulesCustom();
							cus.id = data.apps.getJSONObject(j).optInt("id");
							cus.customName = data.apps.getJSONObject(j).optString("name");
							cus.link = data.apps.getJSONObject(j).optString("link");
							list1.add(cus);
						}
					}
					RulesCustom cus = new RulesCustom();
					cus.id = 9999;
					cus.customName = "last";
					cus.link = "last_link";
					list1.add(cus);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				SearchUtil.logInfo(tag, e.getMessage());
			}

		}else{
			RulesCustom cus = new RulesCustom();
			cus.id = 9999;
			cus.customName = "last";
			cus.link = "last_link";
			list1.add(cus);
		}
	}
}
