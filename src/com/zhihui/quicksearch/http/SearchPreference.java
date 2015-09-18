package com.zhihui.quicksearch.http;

import android.content.Context;
import android.content.SharedPreferences;

public class SearchPreference
{
	/**
	 * 配置文件名称
	 */
	private static final String prefFileName = "_zhihui_file";

	public static boolean setFiledInt(Context context, String key, int values)
	{

		if (context == null)
		{
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, values);

		boolean success = editor.commit();

		return success;
	}

	public static boolean setFiledString(Context context, String key,
			String values)
	{

		if (context == null || values == null || values.equals(""))
		{
			return false;
		}

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

		SharedPreferences.Editor editor = sp.edit();

		String v = values;
		// 需要加密数据
		if (SearchGlobal.encryption)
		{
			v = SearchEncrypt.encryptData2(values);

		}
		editor.putString(key, v);

		boolean success = editor.commit();

		return success;
	}

	public static boolean setFiledBoolean(Context context, String key,
			boolean values)
	{
		if (context == null)
		{
			return false;
		}

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, values);

		boolean success = editor.commit();

		return success;
	}

	/**
	 * 保存一个long型的数据到配置文件中
	 * 
	 * @param context
	 *            Context
	 * 
	 * @param key
	 *            KEY名称
	 * @param values
	 *            需要保存的值
	 * @return 保存成功的话返回true，失败返回false
	 */
	public static boolean setFiledLong(Context context, String key, long values)
	{

		if (context == null)
		{
			return false;
		}

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putLong(key, values);
		return ed.commit();
	}

	public static int getFiledInt(Context context, String key, int def)
	{
		if (context == null)
		{
			return def;
		}
		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE);

		if (!sp.contains(key))
		{
			return def;
		}

		int result = sp.getInt(key, def);

		return result;
	}

	public static String getFiledString(Context context, String key, String def)
	{

		if (context == null)
		{
			return def;
		}

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE);
		if (!sp.contains(key))
		{
			return def;
		}

		String result = sp.getString(key, def);

		// 数据是加密的，需要解密
		if (SearchGlobal.encryption)
		{
			result = SearchEncrypt.decryptData2(result);
		}

		return result;
	}

	public static boolean getFiledBoolean(Context context, String key,
			boolean def)
	{
		if (context == null)
		{
			return def;
		}

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE);

		if (!sp.contains(key))
		{
			return def;
		}

		boolean result = sp.getBoolean(key, def);

		return result;
	}

	/**
	 * 从本地配置文件中读取long类型的数据
	 * 
	 * @param context
	 *            Context
	 * 
	 * @param key
	 *            KEY名称
	 * @param def
	 *            取不到的时需要返回的缺省值
	 * @return
	 */
	public static long getFiledLong(Context context, String key, long def)
	{

		if (context == null)
		{
			return def;
		}
		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE);

		if (!sp.contains(key))
		{
			return def;
		}

		return sp.getLong(key, def);
	}
	
	//-----------------------以下为QuickSearch保存KEY------------------------//
	//搜索引擎key
	public static final String SEARCH_KEY_INT = "SEARCH_KEY_INT";
	public static final String SEARCH_KEY_DATA = "SEARCH_KEY_DATA";
	public static final String NAVIGA_KEY_DATA_1 = "NAVIGA_KEY_DATA_1";
	public static final String NAVIGA_KEY_DATA_2 = "NAVIGA_KEY_DATA_2";
	public static final String key_sim_country = "sim_country";
	public static final String SEARCH_UID = "SEARCH_UID";
	public static final String SEARCH_TOKEN = "SEARCH_TOKEN";
	
	public static boolean deleteSetting(Context context, String key,
			String values)
	{

		SharedPreferences sp = context.getSharedPreferences(prefFileName,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

		SharedPreferences.Editor editor = sp.edit();

		String v = values;
		// 需要加密数据
		if (SearchGlobal.encryption)
		{
			v = SearchEncrypt.encryptData2(values);

		}
		editor.putString(key, v);

		boolean success = editor.commit();

		return success;
	}

}
