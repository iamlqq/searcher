package com.zhihui.quicksearch.http;


public class SearchGlobal {
	/**
	 * 测试开关
	 */
	public static final boolean test = true;
	public static final String tag = "QUICKSEARCH";

	// 测试开关，正式版本时一定要把它设为false
	// public static boolean useTest = false;

	/**
	 * 是否要打印LOG信息
	 */
	public static boolean PRINT_LOG = true;

	/**
	 * 表示String类型的数据是否要加密,保存到SharedPreferences或SDCARD时用到
	 */
	public static boolean encryption = true;

	/**
	 * 文本的编码格式
	 */
	public static final String ENCODING = "UTF-8";

	/**
	 * assets文件夹里的APK配置文件名称
	 */
	public static final String ASSETS_GOOGLE_DAT = "zhihui.cf";
	
	//-----------以下是QuickSearch全局变量-----------
	
	public static final String net_search = "http://192.168.1.249:8080";
//	public static final String net_search = "http://122.10.89.33:7080";
	
	public static final String search = "/index/appconfig";
	public static final String register = "/index/register";
	public static final String login = "/index/softlogin";
	public static final String passwd = "/index/findpasswd";
	public static final String logout = "/index/softlogout";
	public static final String editpwd = "/index/editpwd";
	public static final String islogin = "/index/isLogin";
	public static final String addUserdate = "/index/userDefined";
	
	public static final String net_one = "1001";//搜索
	public static final String net_two = "1002";//热门
	public static final String net_three = "1003";//
	public static final String net_four = "1004";//获取自定义数据
	public static final String net_five = "1005";//注册
	public static final String net_six = "1006";//登录
	public static final String net_seven = "1007";//找回密码
	public static final String net_eight = "1008";//退出登录
	public static final String net_nine = "1009";//增加自定义
	public static final String net_ten = "1010";//修改密码
	public static final String net_eleven = "1011";//是否已经登录
}
