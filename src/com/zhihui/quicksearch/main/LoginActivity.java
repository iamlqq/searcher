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
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class LoginActivity extends Activity implements OnClickListener{

	TextView txt_logintishi1, txt_logintishi2, txt_logintishi3;
	EditText edi_loginname, edi_loginpass;
	Button btn_cancel, btn_loginsure;
	
	String loginname, loginpass;
	private Thread thread;
	List<NameValuePair> params;
	Message msg = new Message();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}
	
	private void init(){
		txt_logintishi1 = (TextView) findViewById(R.id.txt_logintishi1);
		txt_logintishi2 = (TextView) findViewById(R.id.txt_logintishi2);
		txt_logintishi3 = (TextView) findViewById(R.id.txt_logintishi3);
		txt_logintishi3.setOnClickListener(this);
		edi_loginname = (EditText) findViewById(R.id.edi_loginname);
		edi_loginpass = (EditText) findViewById(R.id.edi_loginpass);
		btn_cancel = (Button) findViewById(R.id.btn_logincancel);
		btn_cancel.setOnClickListener(this);
		btn_loginsure = (Button) findViewById(R.id.btn_loginsure);
		btn_loginsure.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_logincancel:
			LoginActivity.this.finish();
			break;
		case R.id.btn_loginsure:
			loginname = edi_loginname.getText().toString();
			loginpass = edi_loginpass.getText().toString();
			if (loginname != null && !"".equals(loginname) && loginpass != null && !"".equals(loginpass)) {
				btn_loginsure.setClickable(false);
				//开启线程进行登录
				if(!SearchUtil.isAvailableNetwork(LoginActivity.this)){
					Toast.makeText(LoginActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
					return;
				}
				params = new ArrayList<>();
				String str = SearchGlobal.net_six;
				String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"user\":\""+loginname+"\",\"pwd\":\""+loginpass+"\"}}";
				params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
				System.out.println("--d参数--" + Base64Util.encodeByKey(Base64Util.encode(requestStr)));
				params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
				System.out.println("--c参数--" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr))));
				thread = new Thread(new Runnable() {
					public void run() {
						//reg();
						try {
							String url = SearchGlobal.net_search + SearchGlobal.login;
//							String result = AndroidHttp.postnoParam(url);
							String result = SearchHttp.post(url, params);
							System.out.println("------返回数据登录数据------" + result);
							if(result != null){
								UserInfoJ info = new UserInfoJ(LoginActivity.this, result);
								if(info.success){
									msg.what = 0; 
									handler.sendMessage(msg);
								}else{
									if(info.result == 4){
										msg.what = 1; 
										handler.sendMessage(msg);
									}else if(info.result == 5){
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
			} else if("".equals(loginname)){
				txt_logintishi1.setText("用户名不能为空");
			} else if("".equals(loginpass)){
				txt_logintishi1.setText("");
				txt_logintishi2.setText("密码不能为空");
			} else{
				txt_logintishi1.setText("");
				txt_logintishi2.setText("");
			}
			break;
		case R.id.txt_logintishi3:
			Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				btn_loginsure.setClickable(true);
				txt_logintishi2.setText("登录成功");
//				sendBroadcast(new Intent("com.zhihui.search.log"));
				SearchPreference.ISGONE = false;
				sendBroadcast(new Intent("com.zhihui.search.custom")); 
				LoginActivity.this.finish();
				closeA();
			}else if(msg.what == 1){
				btn_loginsure.setClickable(true);
				txt_logintishi2.setText("用户名或密码错误");
			}else if(msg.what == 2){
				btn_loginsure.setClickable(true);
				txt_logintishi2.setText("用户不存在");
			}
		};
	};
	
	/**
	 * 关闭广告要finish掉activity
	 */
	void closeA(){
        if(RegisterActivity.getObj()!= null){
        	RegisterActivity.getObj().finish();
        }
    }
}
