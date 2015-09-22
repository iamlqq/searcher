package com.zhihui.quicksearch.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhihui.quicksearch.adapter.CustomAdapter;
import com.zhihui.quicksearch.adapter.CustomAdapterLocal;
import com.zhihui.quicksearch.adapter.NavigationAdapterOne;
import com.zhihui.quicksearch.adapter.NavigationAdapterTwo;
import com.zhihui.quicksearch.adapter.NavigationSadapterO;
import com.zhihui.quicksearch.adapter.NavigationSadapterT;
import com.zhihui.quicksearch.bean.CustomJ;
import com.zhihui.quicksearch.bean.NavigationOneJ;
import com.zhihui.quicksearch.bean.NavigationTwoJ;
import com.zhihui.quicksearch.bean.RulesCustomLocal;
import com.zhihui.quicksearch.bean.UserInfoJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.sqlite.SearchSqlite;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class NavigationActivity extends Activity implements OnClickListener{
	private View mView;
	GridView grid_view1, grid_view2;
//	private Spinner spinner2;
	private Spinner spinner3;
	private File cache2;
	
	Button btn_hot;
	
	private Thread threadO;
	List<NameValuePair> paramsO;
	String ldataOne;
	NavigationOneJ infoO;
	Handler handler1;
	NavigationAdapterOne naviOneadapter;
	NavigationSadapterO naSo;
	Button user_txt;
	
	private Thread threadT;
	List<NameValuePair> paramsT;
	String ldataTwo;
	NavigationTwoJ infoT;
	Handler handler2;
	NavigationAdapterTwo naviTwoadapter;
	NavigationSadapterT naSt;
	
	//自定义模块
	List<NameValuePair> custom_params;
	private Thread custom_thread;
	CustomJ custom_info;
	GridView custom_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cache2 = new File(Environment.getExternalStorageDirectory(), "cache2");
        
		init();
		setContentView(mView);
		
		/*IntentFilter filter = new IntentFilter();  
        filter.addAction("com.zhihui.search.log");  
        registerReceiver(mylogReceiver, filter);*/
        
		IntentFilter filter1 = new IntentFilter();  
		filter1.addAction("com.zhihui.search.custom");  
        registerReceiver(myReceiver, filter1);
        data1();
        data2();
	}
	
	public void init(){
		LayoutInflater inflater = LayoutInflater.from(this);
		mView = inflater.inflate(R.layout.navigation, null);
		
		user_txt = (Button) mView.findViewById(R.id.user_txt);
		user_txt.setOnClickListener(this);
		grid_view1 = (GridView) mView.findViewById(R.id.grid_view1);
		grid_view2 = (GridView) mView.findViewById(R.id.grid_view2);
		btn_hot = (Button) mView.findViewById(R.id.btn_hot);
		btn_hot.setOnClickListener(this);
//		spinner2 = (Spinner) mView.findViewById(R.id.Spinner02);
		spinner3 = (Spinner) mView.findViewById(R.id.Spinner03);
		
	}
	
	public void data1(){
		ldataOne = SearchUtil.getNaviOneData(NavigationActivity.this);
		
		if(ldataOne == null){
			if(!SearchUtil.isAvailableNetwork(NavigationActivity.this)){
				Toast.makeText(NavigationActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
				return;
			}
			paramsO = new ArrayList<NameValuePair>();
			String str = SearchGlobal.net_two;
			String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{}}";
			paramsO.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
			paramsO.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
			threadO = new Thread(new Runnable(){
				public void run() {
					try {
						String url = SearchGlobal.net_search + SearchGlobal.search;
						String result = SearchHttp.post(url, paramsO);
						System.out.println("------返回2数据------" + result);
						if(result != null){
							infoO = new NavigationOneJ(result);
							if (infoT.success) {
								JSONObject json = new JSONObject();
								String time = SearchUtil.getTodayYMD();
								try {
									json.put(time, result);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								String new_result = json.toString();
								SearchPreference.setFiledString(NavigationActivity.this,
										SearchPreference.NAVIGA_KEY_DATA_1, new_result);
								SearchUtil.deleteFile(cache2);
								if(!cache2.exists()){
						            cache2.mkdirs();
						        }
								Message msg = new Message();
								msg.what = 0; 
								handler1.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			threadO.start();
		}else{
			infoO = new NavigationOneJ(ldataOne);
			init1();
		}
		handler1 = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(msg.what == 0){
				//界面跳转代码。。。。。
					init1();
				}
			};
		};
	}
	
	public void data2(){
		ldataTwo = SearchUtil.getNaviTwoData(NavigationActivity.this);
		if(ldataTwo == null){
			if(!SearchUtil.isAvailableNetwork(NavigationActivity.this)){
				Toast.makeText(NavigationActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
				return;
			}
			paramsT = new ArrayList<NameValuePair>();
			String str = SearchGlobal.net_three;
			String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{}}";
			paramsT.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
			paramsT.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
			threadT = new Thread(new Runnable() {
				public void run() {
					//reg();
					try {
						String url = SearchGlobal.net_search + SearchGlobal.search;
//						String result = AndroidHttp.postnoParam(url);
						String result = SearchHttp.post(url, paramsT);
						System.out.println("------返回3数据------" + result);
						if(result != null){
							infoT = new NavigationTwoJ(result);
							if (infoT.success) {
								JSONObject json = new JSONObject();
								String time = SearchUtil.getTodayYMD();
								try {
									json.put(time, result);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								String new_result = json.toString();
								SearchPreference.setFiledString(NavigationActivity.this,
										SearchPreference.NAVIGA_KEY_DATA_2, new_result);
//								init2();
								Message msg = new Message();
								msg.what = 0; 
								handler2.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			threadT.start();
		}else{
			infoT = new NavigationTwoJ(ldataTwo);
			init2();
		}
		handler2 = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(msg.what == 0){
				//界面跳转代码。。。。。
					init2();
				}
			};
		};
	}
	UserInfoJ info;
	List<NameValuePair> isparams;
	public void islogin(String uid, String token){
		if(!SearchUtil.isAvailableNetwork(NavigationActivity.this)){
			Toast.makeText(NavigationActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
			return;
		}
		isparams = new ArrayList<NameValuePair>();
		String str = SearchGlobal.net_eleven;
		String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"uid\":\""+uid+"\",\"token\":\""+token+"\"}}";
		isparams.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
		isparams.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
		Thread isthread = new Thread(new Runnable() {
			public void run() {
				//reg();
				try {
					String url = SearchGlobal.net_search + SearchGlobal.islogin;
//					String result = AndroidHttp.postnoParam(url);
					String result = SearchHttp.post(url, isparams);
					System.out.println("------返回是否登录数据------" + result);
					if(result != null){
						info = new UserInfoJ(NavigationActivity.this, result);
						if(info.success){
							Message msg = new Message();
							msg.what = 0; 
							update.sendMessage(msg);
						}else{
							Message msg = new Message();
							msg.what = 1; 
							update.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		isthread.start();
	}
	//使用数组形式操作第一个Spinner
//    class SpinnerSelectedListenerOne implements OnItemSelectedListener{
// 
//        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//                long arg3) {
//        	custom_view.setVisibility(View.GONE);
//        	grid_view1.setVisibility(View.VISIBLE);
//        	if(infoO.allone.get(arg2) != null){
//        		naviOneadapter = new NavigationAdapterOne(NavigationActivity.this, infoO.allone.get(arg2), cache);
//    			grid_view1.setAdapter(naviOneadapter);
//    			naviOneadapter.notifyDataSetChanged();
//        	}
//        }
// 
//        public void onNothingSelected(AdapterView<?> arg0) {
//        }
//    }
    
    // 通过反射机制处理spinner控件点击默认选项不重新加载的问题
//    class OnTouchListenerClass implements OnTouchListener {
//        public boolean onTouch(View v, MotionEvent event) {
//            try {
//                Class<?> clazz = AdapterView.class;
//                Field field = clazz.getDeclaredField("mOldSelectedRowId");
//                field.setAccessible(true);
//                field.setLong(spinner2,Long.MIN_VALUE);
//            } catch(Exception e){
//                e.printStackTrace();
//            }
//            return false;
//        }
//    }
    
	//使用数组形式操作第二个Spinner
    class SpinnerSelectedListenerTwo implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	if(infoT.alltwo.get(arg2) != null){
        		naviTwoadapter = new NavigationAdapterTwo(NavigationActivity.this, infoT.alltwo.get(arg2));
    			grid_view2.setAdapter(naviTwoadapter);
    			naviTwoadapter.notifyDataSetChanged();
        	}
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
	
	private void init1(){
		//将可选内容与ArrayAdapter连接起来
//		naSo = new NavigationSadapterO(this, infoO.list);
		//将adapter 添加到spinner中
//		spinner2.setAdapter(naSo);
//		spinner2.setOnTouchListener(new OnTouchListenerClass());
		//添加事件Spinner事件监听 
//		spinner2.setOnItemSelectedListener(new SpinnerSelectedListenerOne());
        //设置默认值
//		spinner2.setVisibility(View.VISIBLE);
		
		if(infoO.list1 != null && !infoO.list1.isEmpty()){
			naviOneadapter = new NavigationAdapterOne(this, infoO.list1, cache2);
			grid_view1.setAdapter(naviOneadapter);
		}
		grid_view1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(); 
				intent.setAction("android.intent.action.VIEW");    
				Uri content_url = Uri.parse(infoO.list1.get(arg2).link); 
//				Uri content_url = Uri.parse("http://www.baidu.com"); 
				intent.setData(content_url); 
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
	}
	
	private void init2(){
		//将可选内容与ArrayAdapter连接起来
		naSt = new NavigationSadapterT(this, infoT.list);
        //将adapter 添加到spinner中
        spinner3.setAdapter(naSt);
        //添加事件Spinner事件监听  
        spinner3.setOnItemSelectedListener(new SpinnerSelectedListenerTwo());
         
        //设置默认值
        spinner3.setVisibility(View.VISIBLE);
		
        if(infoT.alltwo != null && !infoT.alltwo.isEmpty()){
			if(infoT.alltwo.get(0) != null){
				naviTwoadapter = new NavigationAdapterTwo(this, infoT.alltwo.get(0));
				grid_view2.setAdapter(naviTwoadapter);
			}
        }
		grid_view2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(); 
				intent.setAction("android.intent.action.VIEW");    
				Uri content_url = Uri.parse(infoT.list1.get(arg2).link);
//				Uri content_url = Uri.parse("http://www.baidu.com");
				intent.setData(content_url); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_hot:
			custom_view.setVisibility(View.GONE);
			grid_view1.setVisibility(View.VISIBLE);
			break;
		case R.id.user_txt:
//			String uid = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_UID, null);
//			String token = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_TOKEN, null);
//			if (uid != null && !"".equals(uid) && token != null && !"".equals(token)) {
//				custom_data(uid, token);
//				
//			}else{
				custom_view.setVisibility(View.VISIBLE);
				grid_view1.setVisibility(View.GONE);
				//没有登录
//				Intent intent = new Intent(NavigationActivity.this, RegisterActivity.class);
//				startActivity(intent);
				final List<RulesCustomLocal> list = SearchSqlite.getInstance(NavigationActivity.this).selectCustom();
				if(list != null && !list.isEmpty()){
					RulesCustomLocal lc = new RulesCustomLocal();
					lc.id_c = 9999;
					lc.customName = "Add+";
					lc.link = "";
					list.add(lc);
					CustomAdapterLocal adapter = new CustomAdapterLocal(NavigationActivity.this, list);
					custom_view.setAdapter(adapter);
					custom_view.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							if(list.get(arg2).id_c == 9999){
								Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
								startActivity(intent);
							}else{
								Intent intent = new Intent(); 
								intent.setAction("android.intent.action.VIEW");    
								Uri content_url = Uri.parse(list.get(arg2).link);
								intent.setData(content_url); 
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}
					});
				}else{
					RulesCustomLocal lc = new RulesCustomLocal();
					lc.id_c = 9999;
					lc.customName = "Add+";
					lc.link = "";
					list.add(lc);
					CustomAdapterLocal adapter = new CustomAdapterLocal(NavigationActivity.this, list);
					custom_view.setAdapter(adapter);
					custom_view.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							if(list.get(arg2).id_c == 9999){
								Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
								startActivity(intent);
							}
						}
					});
				}
//			}
//			Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
//			startActivity(intent);
			break;
//		case R.id.user_eit:
//			//点击设置
//			Intent intent = new Intent(NavigationActivity.this, UserSettingActivity.class);
//			startActivity(intent);
//			break;
		default:
			break;
		}
	}
	
	public void custom_data(String uid, String token){
		//已经登录进去自定义页面
//		Intent intent = new Intent(NavigationActivity.this, CustomActivity.class);
//		startActivity(intent);
		if(!SearchUtil.isAvailableNetwork(NavigationActivity.this)){
			Toast.makeText(NavigationActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
			return;
		}
		custom_params = new ArrayList<NameValuePair>();
		String str = SearchGlobal.net_four;
		String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"uid\":\""+uid+"\",\"token\":\""+token+"\"}}";
		custom_params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
		custom_params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
		custom_thread = new Thread(new Runnable() {
			public void run() {
				//reg();
				try {
					String url = SearchGlobal.net_search + SearchGlobal.search;
//					String result = AndroidHttp.postnoParam(url);
					String result = SearchHttp.post(url, custom_params);
					System.out.println("------返回自定义数据------" + result);
					if(result != null){
						custom_info = new CustomJ(result);
						if (custom_info.success) {
							Message msg = new Message();
							msg.what = 0; 
							custom_handler.sendMessage(msg);
						}
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		custom_thread.start();
	}
	
	Handler custom_handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
			//界面跳转代码。。。。。
				data();
			}
		};
	};
	
	private void data(){
		custom_view.setVisibility(View.VISIBLE);
		grid_view1.setVisibility(View.GONE);
		if(custom_info.list1 != null && !custom_info.list1.isEmpty()){
			CustomAdapter adapter = new CustomAdapter(NavigationActivity.this, custom_info.list1);
			custom_view.setAdapter(adapter);
			custom_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					if(custom_info.list1.get(arg2).id == 9999){
						Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent(); 
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(custom_info.list1.get(arg2).link);
						intent.setData(content_url); 
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}
			});
		}else{
			custom_datal();
		}
		
	}
	
	Handler update = new Handler(){
		public void handleMessage(android.os.Message msg) {
//			if(msg.what == 0){
//			//界面跳转代码。。。。。
//				user_eit.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent(NavigationActivity.this, UserSettingActivity.class);
//						startActivity(intent);
//					}
//				});
//			}else{
//				user_eit.setVisibility(View.GONE);
//				SearchPreference.deleteSetting(NavigationActivity.this,
//						SearchPreference.SEARCH_UID, null);
//				SearchPreference.deleteSetting(NavigationActivity.this,
//						SearchPreference.SEARCH_TOKEN, null);
//			}
		};
	};
	
	/*public BroadcastReceiver mylogReceiver = new BroadcastReceiver(){  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            // TODO Auto-generated method stub
        	String uid = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_UID, null);
    		String token = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_TOKEN, null);
    		user_eit = (Button) mView.findViewById(R.id.user_eit);
    		user_eit.setVisibility(View.VISIBLE);
    		if (uid != null && !"".equals(uid) && token != null && !"".equals(token)) {
    			islogin(uid, token);
    		}else{
    			user_eit.setVisibility(View.GONE);
    		}
        }  
          
	};*/
	
//	Button user_eit;
	public BroadcastReceiver myReceiver = new BroadcastReceiver(){  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            // TODO Auto-generated method stub
        	String uid = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_UID, null);
    		String token = SearchPreference.getFiledString(NavigationActivity.this, SearchPreference.SEARCH_TOKEN, null);
    		custom_view = (GridView) mView.findViewById(R.id.custom_gridview);
//    		if (uid != null && !"".equals(uid) && token != null && !"".equals(token)) {
//    			islogin(uid, token);
//    			custom_data(uid, token);
//    		}else 
    		if(SearchPreference.ISGONE){
//    			user_eit.setVisibility(View.GONE);
    			Thread customl_thread = new Thread(new Runnable() {
    				public void run() {
    					try {
    						
    		    			Message msg = new Message();
    						msg.what = 0; 
    						customlHandler.sendMessage(msg);
    					} catch (Exception e) {
    						// TODO: handle exception
    					}
    				}
    			});
    			customl_thread.start();
    		}
        }  
          
	};
	
	//List<RulesCustomLocal> list; 
	Handler customlHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				custom_view.setVisibility(View.VISIBLE);
    			grid_view1.setVisibility(View.GONE);
				custom_datal();
			}
		};
	};
	public void custom_datal(){
		final List<RulesCustomLocal> list = SearchSqlite.getInstance(NavigationActivity.this).selectCustom();
		if(list != null && !list.isEmpty()){
			RulesCustomLocal lc = new RulesCustomLocal();
			lc.id_c = 9999;
			lc.customName = "Add+";
			lc.link = "";
			list.add(lc);
			CustomAdapterLocal adapter = new CustomAdapterLocal(NavigationActivity.this, list);
			custom_view.setAdapter(adapter);
			custom_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(list.get(arg2).id_c == 9999){
						Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent(); 
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(list.get(arg2).link);
						intent.setData(content_url); 
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}
			});
		}else{
			RulesCustomLocal lc = new RulesCustomLocal();
			lc.id_c = 9999;
			lc.customName = "Add+";
			lc.link = "";
			list.add(lc);
			CustomAdapterLocal adapter = new CustomAdapterLocal(NavigationActivity.this, list);
			custom_view.setAdapter(adapter);
			custom_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(list.get(arg2).id_c == 9999){
						Intent intent = new Intent(NavigationActivity.this, AddCustomActivity.class);
						startActivity(intent);
					}
				}
			});
		}
	}
	
}

