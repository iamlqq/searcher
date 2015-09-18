package com.zhihui.quicksearch.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zhihui.quicksearch.adapter.AddCustomAdapter;
import com.zhihui.quicksearch.bean.AddCustomJ;
import com.zhihui.quicksearch.bean.SureCustomJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class AddCustomActivity extends Activity implements OnClickListener{
	
	EditText edi_customname, edi_customurl;
	ListView custom_list;
	Thread thread;
	String data;
	List<NameValuePair> params;
	AddCustomJ info;
	AddCustomAdapter adapter;
	Button btn_cancel, btn_ok;
	
	List<NameValuePair> addparams;
	Thread addThread;
	private File cache;
	
	SureCustomJ sdata;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcustom);
		cache = new File(Environment.getExternalStorageDirectory(), "cache");
        
        if(!cache.exists()){
            cache.mkdirs();
        }
		init();
		data = SearchUtil.getNaviTwoData(AddCustomActivity.this);
		if(data == null){
			if(!SearchUtil.isAvailableNetwork(AddCustomActivity.this)){
				Toast.makeText(AddCustomActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
				return;
			}
			params = new ArrayList<>();
			String str = SearchGlobal.net_three;
			String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{}}";
			params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
			params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
			thread = new Thread(new Runnable() {
				public void run() {
					//reg();
					try {
						String url = SearchGlobal.net_search + SearchGlobal.search;
						String result = SearchHttp.post(url, params);
						System.out.println("------返回要添加的数据------" + result);
						if(result != null){
							info = new AddCustomJ(result);
							if (info.success) {
								SearchPreference.setFiledString(AddCustomActivity.this,
										SearchPreference.NAVIGA_KEY_DATA_2, result);
								Message msg = new Message();
								msg.what = 0; 
								addcustom_handler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			thread.start();
		}else{
			info = new AddCustomJ(data);
			updateView();
		}
	}
	private void init(){
		edi_customname = (EditText) findViewById(R.id.edi_customname);
		edi_customurl = (EditText) findViewById(R.id.edi_customurl);
		custom_list = (ListView) findViewById(R.id.custom_List);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_ok.setClickable(false);
		edi_customname.addTextChangedListener(textWatcher);  
		edi_customurl.addTextChangedListener(textWatcher);  
		
		 
	}
	
	private TextWatcher textWatcher = new TextWatcher() {  
        @Override    
        public void afterTextChanged(Editable s) {     
            // TODO Auto-generated method stub    
        	customname = edi_customname.getText().toString().trim();
			customurl = edi_customurl.getText().toString().trim();
			if(customname != null && !"".equals(customname) && customurl != null && !"".equals(customurl)){
				if(!SearchUtil.isUrl(customurl)){
					Toast.makeText(AddCustomActivity.this, "您输入的网址不符合", Toast.LENGTH_LONG).show();
					btn_ok.setClickable(false);
				}else{
					btn_ok.setClickable(true);
				}
				
			}else{
				btn_ok.setClickable(false);
			}
        }   
        @Override 
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            // TODO Auto-generated method stub  
        }  
         @Override    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count) {     
                              
        }                    
    };
	
	Handler addcustom_handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
			//界面跳转代码。。。。。
				updateView();
			}
		};
	};
	
//	String	appid = "";
	String customname, customurl;
	private void updateView(){
		adapter = new AddCustomAdapter(this, info.list1, cache);
		custom_list.setAdapter(adapter);
		custom_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				edi_customname.setText(info.list1.get(arg2).naviName);
				edi_customurl.setText(info.list1.get(arg2).link);
//				appid += info.list1.get(arg2).id;
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_cancel:
			AddCustomActivity.this.finish();
			break;

		case R.id.btn_ok:
			//启动线程添加到后台
//			AddCustomActivity.this.finish();
			customname = edi_customname.getText().toString().trim();
			customurl = edi_customurl.getText().toString().trim();
			String uid = SearchPreference.getFiledString(AddCustomActivity.this, SearchPreference.SEARCH_UID, null);
			String token = SearchPreference.getFiledString(AddCustomActivity.this, SearchPreference.SEARCH_TOKEN, null);
			
				if (uid != null && !"".equals(uid) && token != null && !"".equals(token)) {
					if(!SearchUtil.isAvailableNetwork(AddCustomActivity.this)){
						Toast.makeText(AddCustomActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
						return;
					}
					params = new ArrayList<>();
					String str = SearchGlobal.net_nine;
					String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"uid\":\""+uid+"\",\"token\":\""+token+"\",\"name\":\""+customname+"\",\"link\":\""+customurl+"\"}}";
					params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
					params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
					thread = new Thread(new Runnable() {
						public void run() {
							try {
								String url = SearchGlobal.net_search + SearchGlobal.addUserdate;
								String result = SearchHttp.post(url, params);
								System.out.println("------返回添加成功数据------" + result);
								if(result != null){
									sdata = new SureCustomJ(result);
									if (info.success) {
										Message msg = new Message();
										msg.what = 0; 
										surecustom_handler.sendMessage(msg);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});
					thread.start();
				}
			
			break;
		default:
			break;
		}
	}
	
	Handler surecustom_handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
			//界面跳转代码。。。。。
				//updateView();
				sendBroadcast(new Intent("com.zhihui.search.log"));
				sendBroadcast(new Intent("com.zhihui.search.custom"));
				AddCustomActivity.this.finish();
			}
		};
	};
}
