package com.zhihui.quicksearch.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchPreference;

public class SearchUtil {
	private static final String tag = SearchUtil.class.getSimpleName();
	private Context ctx = null;
	private TelephonyManager tm = null;
	public SearchUtil(Context c) {
		ctx = c;
		if (ctx != null) {
			tm = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
	}
	
	

	public static void logInfo(String tag, String log) {
		if(fileIsExists()){
			SearchGlobal.PRINT_LOG =  true;
		}
		if (SearchGlobal.PRINT_LOG && log != null) {
			Log.i(tag, "liao," + tag + ":" + log);
		}

	}

	public static boolean fileIsExists() {
		try {
			File f = new File(Environment.getExternalStorageDirectory().toString() + "/test.txt");
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	/**
	 * 验证邮箱格式是否正确
	 * @param mail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {   
		String strPattern = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
	    //String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";  
	    Pattern p = Pattern.compile(strPattern);  
	    Matcher m = p.matcher(strEmail);  
	    return m.matches();  
	}
	
	public static boolean isUrl(String url){
		String check = "^((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?$";
		Pattern p = Pattern.compile(check);
		Matcher m = p.matcher(url);  
	    return m.matches();
	}
	
	/**
     * 验证密码
     * 1.不包含中文
     * 2.长度在6~16之间
     * @param str
     * @return true 表示正确
     * @author 
     */
	public static boolean  check_password(String str) {
		boolean flag = true;
		int int_length = str.length();
		if(int_length<6){
			flag = false;
		}else if(int_length >16){
			flag = false;
		}else {
			String s = "[\\u4e00-\\u9fa5]";
			Pattern pattern = Pattern.compile(s);
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				flag = false;//验证不通过，含有中文
			} else {
			}
		}
		return flag;
	} 
	
	/**
	 * 判断是否有可用的网络<br>
	 * 需要权限:android.permission.ACCESS_NETWORK_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAvailableNetwork(Context context) {
		boolean v = false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null) {
			v = networkInfo.isAvailable();
		}

		return v;

	}
	
	/**
	 * 获取今天的日期，如"2013-08-09"
	 * 
	 * @return 如"2013-08-09"
	 */
	public static final String getTodayYMD() {
		Date date = new Date(System.currentTimeMillis());
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String vv = format.format(c1.getTime());
		return vv;

	}
	/**
	 * 搜索引擎数据
	 * @param ctx
	 * @return
	 */
	public static String getSearchData(Context ctx) {
		String dataInfo = null;
		String s = SearchPreference.getFiledString(ctx, SearchPreference.SEARCH_KEY_DATA, null);
		if (s != null) {
			JSONObject j = null;
			try {
				j = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String today_day = getTodayYMD();
			dataInfo = j.optString(today_day);
			if (dataInfo == null || "".equals(dataInfo)) {
				return null;
			}
		}
		return dataInfo;
	}
	
	/**
	 * 热门数据
	 * @param ctx
	 * @return
	 */
	public static String getNaviOneData(Context ctx) {
		String dataInfo = null;
		String s = SearchPreference.getFiledString(ctx, SearchPreference.NAVIGA_KEY_DATA_1, null);
		if (s != null) {
			JSONObject j = null;
			try {
				j = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String today_day = getTodayYMD();
			dataInfo = j.optString(today_day);
			if (dataInfo == null || "".equals(dataInfo)) {
				return null;
			}
		}
		return dataInfo;
	}
	
	/**
	 * 分类数据
	 * @param ctx
	 * @return
	 */
	public static String getNaviTwoData(Context ctx) {
		String dataInfo = null;
		String s = SearchPreference.getFiledString(ctx, SearchPreference.NAVIGA_KEY_DATA_2, null);
		if (s != null) {
			JSONObject j = null;
			try {
				j = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String today_day = getTodayYMD();
			dataInfo = j.optString(today_day);
			if (dataInfo == null || "".equals(dataInfo)) {
				return null;
			}
		}
		return dataInfo;
	}
	
	/**
	 * 获取ISO国家码，相当于提供SIM卡的国家码。<br>
	 * SIM卡国别缩写，如中国为 "cn" 美国为 "us"<br>
	 * 
	 * @return
	 */
	public String getSimCountryIso() {
		String v = SearchPreference.getFiledString(ctx,
				SearchPreference.key_sim_country, null);

		if (v == null && tm != null) {
			v = tm.getSimCountryIso();

			if (v != null) {
				SearchPreference.setFiledString(ctx,
						SearchPreference.key_sim_country, v);
			}
		}

		if (v == null) {
			v = getCountry(ctx);
		}

		return v;

	}
	
	/**
	 * 获取国家缩写，小写
	 * 
	 * @param c
	 * @return
	 */
	public static String getCountry(Context c) {
		if (c == null) {
			return "";
		}
		Locale locale = c.getResources().getConfiguration().locale;
		String country = locale.getCountry().toLowerCase();
		return country;
	}
}
