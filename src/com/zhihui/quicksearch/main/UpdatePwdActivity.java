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

public class UpdatePwdActivity extends Activity implements OnClickListener{

	EditText edi_updatename, edi_oldpwd, edi_newpwd, edi_okpwd;
	TextView txt_userhint1, txt_userhint2, txt_userhint3, txt_userhint4;
	Button btn_sure, btn_cancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatepwd);
		init();
	}
	
	private void init(){
		edi_updatename = (EditText) findViewById(R.id.edi_updatename);
		edi_oldpwd = (EditText) findViewById(R.id.edi_oldpwd);
		edi_newpwd = (EditText) findViewById(R.id.edi_newpwd);
		edi_okpwd = (EditText) findViewById(R.id.edi_okpwd);
		
		txt_userhint1 = (TextView) findViewById(R.id.txt_userhint1);
		txt_userhint2 = (TextView) findViewById(R.id.txt_userhint2);
		txt_userhint3 = (TextView) findViewById(R.id.txt_userhint3);
		txt_userhint4 = (TextView) findViewById(R.id.txt_userhint4);
		
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_sure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}
	
	String username, oldPwd, newPwd, okPwd;
	Thread thread;
	List<NameValuePair> params;
	Message msg = new Message();
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_sure:
			username = edi_updatename.getText().toString().trim();
			oldPwd = edi_oldpwd.getText().toString().trim();
			newPwd = edi_newpwd.getText().toString().trim();
			okPwd = edi_okpwd.getText().toString().trim();
			String uid = SearchPreference.getFiledString(UpdatePwdActivity.this, SearchPreference.SEARCH_UID, null);
			String token = SearchPreference.getFiledString(UpdatePwdActivity.this, SearchPreference.SEARCH_TOKEN, null);
			if (username != null && !"".equals(username) && oldPwd != null && !"".equals(oldPwd) && newPwd != null && !"".equals(newPwd) && okPwd != null && !"".equals(okPwd)) {
					if(!SearchUtil.check_password(oldPwd)){
						txt_userhint3.setText(getResources().getString(R.string.txt_passhint));
					} else if(!SearchUtil.check_password(newPwd)){
						txt_userhint4.setText(getResources().getString(R.string.txt_passhint));
					} else{
						if(!newPwd.endsWith(okPwd)){
							txt_userhint4.setText(getResources().getString(R.string.samepass));
						}else{
							txt_userhint3.setText("");
							txt_userhint4.setText("");
							//开启线程请求注册
							if(!SearchUtil.isAvailableNetwork(UpdatePwdActivity.this)){
								Toast.makeText(UpdatePwdActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
								return;
							}
							params = new ArrayList<>();
							String str = SearchGlobal.net_ten;
							String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"uid\":\""+uid+"\",\"token\":\""+token+"\",\"username\":\""+username+"\",\"oldPwd\":\""+oldPwd+"\",\"newPwd\":\""+newPwd+"\"}}";
							params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
							System.out.println("--d参数--" + Base64Util.encodeByKey(Base64Util.encode(requestStr)));
							params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
							System.out.println("--c参数--" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr))));
							thread = new Thread(new Runnable() {
								public void run() {
									//reg();
									try {
										String url = SearchGlobal.net_search + SearchGlobal.editpwd;
										String result = SearchHttp.post(url, params);
										System.out.println("------返回修改密码数据------" + result);
										if(result != null){
											UserInfoJ info = new UserInfoJ(UpdatePwdActivity.this, result);
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
			} else if("".equals(oldPwd)){
				txt_userhint1.setText("");
				txt_userhint2.setText(getResources().getString(R.string.oldpass));
			} else if("".equals(newPwd)){
				txt_userhint1.setText("");
				txt_userhint2.setText("");
				txt_userhint3.setText(getResources().getString(R.string.newpass));
			}else if("".equals(okPwd)){
				txt_userhint1.setText("");
				txt_userhint2.setText("");
				txt_userhint3.setText("");
				txt_userhint4.setText(getResources().getString(R.string.okpass));
			}
			break;

		case R.id.btn_cancel:
			UpdatePwdActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				btn_sure.setClickable(true);
				txt_userhint4.setText("密码修改成功");
//				Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
//				startActivity(intent);
				sendBroadcast(new Intent("com.zhihui.search.log"));
				sendBroadcast(new Intent("com.zhihui.search.custom")); 
				UpdatePwdActivity.this.finish();
				closeA();
			}else if(msg.what == 1){
				btn_sure.setClickable(true);
				txt_userhint4.setText("用户名或密码错误");
			}else if(msg.what == 2){
				btn_sure.setClickable(true);
				txt_userhint4.setText("用户不存在");
			}
		};
	};
	
	/**
	 * 关闭广告要finish掉activity
	 */
	void closeA(){
        if(UserSettingActivity.getObj()!= null){
        	UserSettingActivity.getObj().finish();
        }
    }
}
