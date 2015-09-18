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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhihui.quicksearch.bean.UserInfoJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class RegisterActivity extends Activity implements OnClickListener{

	TextView txt_userhint1, txt_userhint2, txt_userhint3, txt_userhint4;
	EditText edi_username, edi_usergmail, edi_userpass, edi_surepass;
	Button btn_login, btn_sure, btn_cancel;
	
	String username, usergmail, userpass, surepass;
	private Thread thread;
	List<NameValuePair> params; 
	Message msg = new Message();
	
	private static RegisterActivity reg;
	public static RegisterActivity getObj(){
        return reg;
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		reg = this;
		init();
	}
	
	private void init(){
		txt_userhint1 = (TextView) findViewById(R.id.txt_userhint1);
		txt_userhint2 = (TextView) findViewById(R.id.txt_userhint2);
		txt_userhint3 = (TextView) findViewById(R.id.txt_userhint3);
		txt_userhint4 = (TextView) findViewById(R.id.txt_userhint4);
		edi_username = (EditText) findViewById(R.id.edi_username);
		edi_usergmail = (EditText) findViewById(R.id.edi_usergmail);
		edi_userpass = (EditText) findViewById(R.id.edi_userpass);
		edi_surepass = (EditText) findViewById(R.id.edi_surepass);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_login.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_login:
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_sure:
			username = edi_username.getText().toString().trim();
			usergmail = edi_usergmail.getText().toString().trim();
			userpass = edi_userpass.getText().toString().trim();
			surepass = edi_surepass.getText().toString().trim();
			if (username != null && !"".equals(username) && usergmail != null && !"".equals(usergmail) && userpass != null && !"".equals(userpass)&& surepass != null && !"".equals(surepass)) {
				if(!SearchUtil.isEmail(edi_usergmail.getText().toString())){
					txt_userhint2.setText(getResources().getString(R.string.txt_gmailhint));
				}else if(!SearchUtil.check_password(edi_userpass.getText().toString())){
					txt_userhint3.setText(getResources().getString(R.string.txt_passhint));
				}else{
					if(!userpass.endsWith(surepass)){
						txt_userhint4.setText(getResources().getString(R.string.samepass));
					}else{
						txt_userhint3.setText("");
						txt_userhint2.setText("");
						txt_userhint4.setText("");
						//开启线程请求注册
						if(!SearchUtil.isAvailableNetwork(RegisterActivity.this)){
							Toast.makeText(RegisterActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
							return;
						}
						params = new ArrayList<>();
						String str = SearchGlobal.net_five;
						String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"user\":\""+username+"\",\"email\":\""+usergmail+"\",\"pwd\":\""+userpass+"\"}}";
						params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
						System.out.println("--d参数--" + Base64Util.encodeByKey(Base64Util.encode(requestStr)));
						params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
						System.out.println("--c参数--" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr))));
						thread = new Thread(new Runnable() {
							public void run() {
								//reg();
								try {
									String url = SearchGlobal.net_search + SearchGlobal.register;
									String result = SearchHttp.post(url, params);
									System.out.println("------返回数据注册数据------" + result);
									if(result != null){
										UserInfoJ info = new UserInfoJ(RegisterActivity.this, result);
										if (info.success) {
											msg.what = 0; 
											handler.sendMessage(msg);
										}else{
											if(info.result == 6){
												msg.what = 1; 
												handler.sendMessage(msg);
												
											}else if(info.result == 7){
												msg.what = 2; 
												handler.sendMessage(msg);
											}
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});
						thread.start();
					}
				}
				
			} else if("".equals(username)){
				txt_userhint1.setText(getResources().getString(R.string.txt_usernamenull));
			} else if("".equals(usergmail)){
				txt_userhint1.setText("");
				txt_userhint2.setText(getResources().getString(R.string.txt_gmailnull));
			} else if("".equals(userpass)){
				txt_userhint1.setText("");
				txt_userhint2.setText("");
				txt_userhint3.setText(getResources().getString(R.string.txt_passnull));
			} else if("".equals(surepass)){
				txt_userhint1.setText("");
				txt_userhint2.setText("");
				txt_userhint3.setText("");
				txt_userhint4.setText(getResources().getString(R.string.okpass));
			} else {
				txt_userhint1.setText("");
				txt_userhint2.setText("");
				txt_userhint3.setText("");
				txt_userhint4.setText("");
			}
			break;
		case R.id.btn_cancel:
			RegisterActivity.this.finish();
			break;

		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				txt_userhint3.setText("已经注册成功等待3秒回到主页面");
				sendBroadcast(new Intent("com.zhihui.search.log"));
				sendBroadcast(new Intent("com.zhihui.search.custom")); 
				RegisterActivity.this.finish();
			}else if(msg.what == 1){
				txt_userhint3.setText("邮箱已注册");
			}else if(msg.what == 2){
				txt_userhint3.setText("用户名已注册");
			}
		};
	};
}
