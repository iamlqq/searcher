package com.zhihui.quicksearch.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.SearchUtil;

public class UserInfoJ {
	public static final String tag = UserInfoJ.class.getSimpleName();
	public boolean success;
	public JSONObject data;
	public String uid;
	public int result;
	public String token;
	public String username;
	public List<SearchSauseList> list = new ArrayList<SearchSauseList>();
	public List<RulesSearchSause> list1 = new ArrayList<RulesSearchSause>();

	public UserInfoJ(Context ctx, String str) {
		if (str != null) {

			try {
				JSONObject json = new JSONObject(str);
				if (json != null) {
					success = json.optBoolean("success");
					if (success) {
						data = json.optJSONObject("data");
						result = data.optInt("result");
						if (result == 0) {
							System.out.println("------已经成功-----");
							uid = data.optString("uid");
							token = data.optString("token");
							username = data.optString("username");
							SearchPreference.setFiledString(ctx,
									SearchPreference.SEARCH_UID, uid);
							SearchPreference.setFiledString(ctx,
									SearchPreference.SEARCH_TOKEN, token);
						}
					} else {
						result = json.optInt("result");
						if (result == 1) {
							System.out.println("------缺少参数-----");
						} else if (result == 2) {
							System.out.println("------参数不正确-----");
						} else if (result == 3) {
							System.out.println("------用户未登录-----");
						} else if (result == 4) {
							System.out.println("------密码错误-----");
						} else if (result == 5) {
							System.out.println("------用户不存在-----");
						} else if (result == 6) {
							System.out.println("------邮箱已注册-----");
						} else if (result == 7) {
							System.out.println("------用户名已注册-----");
						}
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
				SearchUtil.logInfo(tag, e.getMessage());
			}

		}
	}
}
