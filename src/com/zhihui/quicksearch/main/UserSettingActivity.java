package com.zhihui.quicksearch.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zhihui.quicksearch.bean.UserInfoJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class UserSettingActivity extends Activity implements OnClickListener {

	Button btn_updatepwd, btn_eit;
	List<NameValuePair> params_eit;
	UserInfoJ info;
	Message msg = new Message();
	
	private static UserSettingActivity user;
	public static UserSettingActivity getObj(){
        return user;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usersetting);
		user = this;
		init();
	}

	private void init() {
		btn_updatepwd = (Button) findViewById(R.id.btn_updatepwd);
		btn_eit = (Button) findViewById(R.id.btn_eit);
		btn_updatepwd.setOnClickListener(this);
		btn_eit.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_updatepwd:
			Intent intent = new Intent(UserSettingActivity.this, UpdatePwdActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_eit:
			dialog1();
			break;
		default:
			break;
		}
	}

	private void dialog1(){  
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������  
        builder.setTitle("��ʾ"); //���ñ���  
        builder.setMessage("�Ƿ�ȷ���˳�?"); //��������  
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { //����ȷ����ť  
            @Override  
            public void onClick(DialogInterface dialog, int which) { 
            	String uid = SearchPreference.getFiledString(UserSettingActivity.this, SearchPreference.SEARCH_UID, null);
        		String token = SearchPreference.getFiledString(UserSettingActivity.this, SearchPreference.SEARCH_TOKEN, null);
        		if(!SearchUtil.isAvailableNetwork(UserSettingActivity.this)){
					Toast.makeText(UserSettingActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
					return;
				}
        		params_eit = new ArrayList<>();
				String str = SearchGlobal.net_eight;
				String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"uid\":\""+uid+"\",\"token\":\""+token+"\"}}";
				params_eit.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
				System.out.println("--d����--" + Base64Util.encodeByKey(Base64Util.encode(requestStr)));
				params_eit.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
				System.out.println("--c����--" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr))));
            	Thread custom_thread = new Thread(new Runnable() {
					public void run() {
						try {
							String url = SearchGlobal.net_search + SearchGlobal.logout;
							String result = SearchHttp.post(url, params_eit);
							System.out.println("------�����Ƴ���¼����------" + result);
							if(result != null){
								info = new UserInfoJ(UserSettingActivity.this, result);
								if(info.success){
									msg.what = 0; 
									handler.sendMessage(msg);
								}else{
									msg.what = 1; 
									handler.sendMessage(msg);
								}
								
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				custom_thread.start();
    			
                dialog.dismiss(); //�ر�dialog  
                
            }  
        });  
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { //����ȡ����ť  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });   
        //��������������ˣ���������ʾ����  
        builder.create().show();  
    }
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				SearchPreference.deleteSetting(UserSettingActivity.this,
						SearchPreference.SEARCH_UID, null);
				SearchPreference.deleteSetting(UserSettingActivity.this,
						SearchPreference.SEARCH_TOKEN, null);
				Toast.makeText(UserSettingActivity.this, "�˳��ɹ�" , Toast.LENGTH_SHORT).show(); 
				sendBroadcast(new Intent("com.zhihui.search.log")); 
				sendBroadcast(new Intent("com.zhihui.search.custom")); 
				UserSettingActivity.this.finish();
			}else{
				Toast.makeText(UserSettingActivity.this, "�˳�ʧ��" , Toast.LENGTH_SHORT).show();  
			}
		};
	};
}
