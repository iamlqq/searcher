package com.zhihui.quicksearch.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
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

public class ForgetPassActivity extends Activity implements OnClickListener{

	TextView txt_gmailhint;
	EditText edi_gmail;
	Button btn_cancel, btn_passsure;
	
	String str_gmail;
	private Thread thread;
	List<NameValuePair> params;
	Message msg = new Message();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpass);
		init();
	}
	
	private void init(){
		txt_gmailhint = (TextView) findViewById(R.id.txt_gmailhint);
		edi_gmail = (EditText) findViewById(R.id.edi_gmail);
		btn_cancel = (Button) findViewById(R.id.btn_forgetcancel);
		btn_cancel.setOnClickListener(this);
		btn_passsure = (Button) findViewById(R.id.btn_passsure);
		btn_passsure.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_forgetcancel:
			ForgetPassActivity.this.finish();
			break;
		case R.id.btn_passsure:
			str_gmail = edi_gmail.getText().toString().trim();
			if (str_gmail != null && !"".equals(str_gmail)) {
				if(!SearchUtil.isEmail(edi_gmail.getText().toString())){
					txt_gmailhint.setText(getResources().getString(R.string.txt_gmailhint));
				}else{
					//�����߳��һ�����
					if(!SearchUtil.isAvailableNetwork(ForgetPassActivity.this)){
						Toast.makeText(ForgetPassActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
						return;
					}
					params = new ArrayList<>();
					String str = SearchGlobal.net_seven;
					String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{\"email\":\""+str_gmail+"\"}}";
					params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
					System.out.println("--d����--" + Base64Util.encodeByKey(Base64Util.encode(requestStr)));
					params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
					System.out.println("--c����--" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr))));
					thread = new Thread(new Runnable() {
						public void run() {
							//reg();
							try {
								String url = SearchGlobal.net_search + SearchGlobal.passwd;
//								String result = AndroidHttp.postnoParam(url);
								String result = SearchHttp.post(url, params);
								System.out.println("------���������һ���������------" + result);
								if(result != null){
									UserInfoJ info = new UserInfoJ(ForgetPassActivity.this, result);
									if(info.success){
										msg.what = 1; 
										handler.sendMessage(msg);
									}else{
										if(info.result == 5){
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
			} else if("".equals(str_gmail)){
				txt_gmailhint.setText(getResources().getString(R.string.txt_gmailnull));
			} else{
				txt_gmailhint.setText("");
			}
			break;

		default:
			break;
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				txt_gmailhint.setText("��ȥע������鿴��¼����");
			}else if(msg.what == 1){
				txt_gmailhint.setText("�û�������");
			}
		};
	};
}
