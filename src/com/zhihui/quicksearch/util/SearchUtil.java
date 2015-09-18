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
	 * ��֤�����ʽ�Ƿ���ȷ
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
     * ��֤����
     * 1.����������
     * 2.������6~16֮��
     * @param str
     * @return true ��ʾ��ȷ
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
				flag = false;//��֤��ͨ������������
			} else {
			}
		}
		return flag;
	} 
	
	/**
	 * �ж��Ƿ��п��õ�����<br>
	 * ��ҪȨ��:android.permission.ACCESS_NETWORK_STATE
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
	 * ��ȡ��������ڣ���"2013-08-09"
	 * 
	 * @return ��"2013-08-09"
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
	 * ������������
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
	 * ��������
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
	 * ��������
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
	 * ��ȡISO�����룬�൱���ṩSIM���Ĺ����롣<br>
	 * SIM��������д�����й�Ϊ "cn" ����Ϊ "us"<br>
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
	 * ��ȡ������д��Сд
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
