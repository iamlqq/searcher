package com.zhihui.quicksearch.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zhihui.quicksearch.adapter.SearchSauseAdapter;
import com.zhihui.quicksearch.bean.SearchSauseJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

public class SearchSauseActivity extends Activity implements OnClickListener{
	
	EditText inputTxt;
	Button search_btn;
	ListView list_view;

	SearchSauseAdapter sadapter;
//	private static final String[] m={"搜索","娱乐","小说","视频","图片"};
//	private Spinner spinner;
	private Thread thread;
	List<NameValuePair> params;
	String data;
	SearchSauseJ info;
	Handler handler;
	
	private File cache1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchsause);
		//创建缓存目录，系统一运行就得创建缓存目录的，
        cache1 = new File(Environment.getExternalStorageDirectory(), "cache1");
        
//		spinner = (Spinner) findViewById(R.id.Spinner01);
//        //将可选内容与ArrayAdapter连接起来
//		ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
//        //设置下拉列表的风格
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //将adapter 添加到spinner中
//        spinner.setAdapter(adapter);
//        //添加事件Spinner事件监听  
//        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
         
        //设置默认值
//        spinner.setVisibility(View.VISIBLE);
        data = SearchUtil.getSearchData(SearchSauseActivity.this);
		if(data == null){
			if(!SearchUtil.isAvailableNetwork(SearchSauseActivity.this)){
				Toast.makeText(SearchSauseActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
				return;
			}
			params = new ArrayList<>();
			String str = SearchGlobal.net_one;
			String requestStr = "{\"opcode\":\""+str+"\",\"opdata\":{}}";
			params.add(new BasicNameValuePair("d", Base64Util.encodeByKey(Base64Util.encode(requestStr))));
			params.add(new BasicNameValuePair("c", SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(requestStr)))));
			thread = new Thread(new Runnable() {
				public void run() {
					try {
						String url = SearchGlobal.net_search + SearchGlobal.search;
						String result = SearchHttp.post(url, params);
						System.out.println("------返回1数据------" + result);
						if(result != null){
							info = new SearchSauseJ(result);
							if (info.success) {
								JSONObject json = new JSONObject();
								String time = SearchUtil.getTodayYMD();
								try {
									json.put(time, result);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								String new_result = json.toString();
								SearchPreference.setFiledString(SearchSauseActivity.this,
										SearchPreference.SEARCH_KEY_DATA, new_result);
								SearchUtil.deleteFile(cache1);
								if(!cache1.exists()){
						            cache1.mkdirs();
						        }
								Message msg = new Message();
								msg.what = 0; 
								handler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			thread.start();
		}else{
			info = new SearchSauseJ(data);
			init();
		}
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(msg.what == 0){
				//界面跳转代码。。。。。
					init();
				}
			};
		};
	}
	
	//使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
//        	view.setText("你的血型是："+m[arg2]);
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
	private void init(){
		inputTxt = (EditText) findViewById(R.id.search_edit);
		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		list_view = (ListView) findViewById(R.id.search_List);
		
		if(info.list1 != null && !info.list1.isEmpty()){
			
			int selectInt0 = SearchPreference.getFiledInt(SearchSauseActivity.this, SearchPreference.SEARCH_KEY_INT, 0);
			inputTxt.setHint(info.list1.get(selectInt0).sauseName);
			sadapter = new SearchSauseAdapter(SearchSauseActivity.this, info.list1, cache1);
			list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
			list_view.setAdapter(sadapter);
			list_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int selectInt = SearchPreference.getFiledInt(SearchSauseActivity.this, SearchPreference.SEARCH_KEY_INT, 0);
					if(selectInt != arg2){
						// 当前点击的CB
						boolean cu = !sadapter.isSelected.get(arg2);  
		                // 先将所有的置为FALSE  
		                for(Integer p : sadapter.isSelected.keySet()) {  
		                	sadapter.isSelected.put(p, false);  
		                }  
		                // 再将当前选择CB的实际状态  
		                sadapter.isSelected.put(arg2, cu); 
		                SearchPreference.setFiledInt(SearchSauseActivity.this, SearchPreference.SEARCH_KEY_INT, arg2);
		                sadapter.notifyDataSetChanged();  
		                sadapter.beSelectedData.clear();  
		                if(cu){
		                	sadapter.beSelectedData.add(info.list1.get(arg2));  
		                }
		                inputTxt.setHint(info.list1.get(arg2).sauseName);
					}
	                
				}
			});
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.search_btn:
			int selectInt0 = SearchPreference.getFiledInt(SearchSauseActivity.this, SearchPreference.SEARCH_KEY_INT, 0);
			String url = info.list1.get(selectInt0).link+inputTxt.getText().toString();
			Intent intent = new Intent(); 
			intent.setAction("android.intent.action.VIEW");    
			Uri content_url = Uri.parse(url); 
			intent.setData(content_url); 
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
