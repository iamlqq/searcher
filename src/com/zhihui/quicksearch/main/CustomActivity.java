package com.zhihui.quicksearch.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zhihui.quicksearch.adapter.CustomAdapter;
import com.zhihui.quicksearch.bean.CustomJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SecurityUtils;

public class CustomActivity extends Activity implements OnClickListener{
	GridView custom_view;
	List<NameValuePair> custom_params;
	private Thread custom_thread;
	CustomJ custom_info;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcustom);
//		custom_view = (GridView) findViewById(R.id.custom_view);
		custom_params = new ArrayList<>();
		String str = SearchGlobal.net_four;
		String uid = SearchPreference.getFiledString(CustomActivity.this, SearchPreference.SEARCH_UID, null);
		String token = SearchPreference.getFiledString(CustomActivity.this, SearchPreference.SEARCH_TOKEN, null);
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
		if(custom_info.list1 != null){
			CustomAdapter adapter = new CustomAdapter(CustomActivity.this, custom_info.list1);
			custom_view.setAdapter(adapter);
		}
		custom_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(custom_info.list1.get(arg2).id == 9999){
					Intent intent = new Intent(CustomActivity.this, AddCustomActivity.class);
					startActivity(intent);
				}else{
					
				}
			}
		});
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
