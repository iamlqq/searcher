package com.zhihui.quicksearch.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhihui.quicksearch.util.SearchUtil;

public class SureCustomJ {
	public static final String tag = NavigationTwoJ.class.getSimpleName();
	public boolean success;
	public JSONArray appPacks;

	public SureCustomJ(String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				SearchUtil.logInfo(tag, e.getMessage());
			}

		}
	}
}
