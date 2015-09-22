package com.zhihui.quicksearch.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhihui.quicksearch.bean.UserInfoJ;
import com.zhihui.quicksearch.http.SearchGlobal;
import com.zhihui.quicksearch.http.SearchHttp;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.util.Base64Util;
import com.zhihui.quicksearch.util.SearchUtil;
import com.zhihui.quicksearch.util.SecurityUtils;

@SuppressWarnings("deprecation")
public class SearchMainActivity extends Activity {

	Button btn_user;
	Context context = null;
	LocalActivityManager manager = null;
	ViewPager pager = null;

	// 包裹小圆点的LinearLayout
	private ViewGroup group;
	private ImageView imageView;
	private ImageView[] imageViews;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = SearchMainActivity.this;
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);

//		String ccc = "{\"uid\":\"37\",\"sn\":{\"idfv\":\"\",\"openudid\":\"\",\"mac\":\"90:4e:2b:ac:7c:9c\",\"udid\":\"\",\"idfa\":\"\"},\"devicetype\":\"0\",\"token\":\"\",\"deviceos\":\"4.2.2\",\"opdata\":{\"sno\":\"0123456789ABCDEF\",\"cp\":\"3\",\"mno\":\"HUAWEI Y511-T00\",\"itime\":\"2015-09-04 04:57:27\",\"imei\":\"861800002065416\",\"androidId\":\"f260df0cfa12ea7f\",\"appid\":\"18\",\"mac\":\"90:4e:2b:ac:7c:9c\",\"pno\":\"4.2.2\",\"ptype\":\"HUAWEI\"},\"opcode\":\"1001\"}";
//		
//		System.out.println("加密"+ Base64Util.encode(ccc));
//		System.out.println("加密d=" + Base64Util.encodeByKey(Base64Util.encode(ccc)) + "&c=" + SecurityUtils.Get32CodeModel(Base64Util.encodeByKey(Base64Util.encode(ccc))));
		init();
		initPagerViewer();
		initImageView();
		SearchPreference.ISGONE = false;
		sendBroadcast(new Intent("com.zhihui.search.custom"));
	}
	MyPagerAdapter adapterPage;
	
	private void init(){
		btn_user = (Button) findViewById(R.id.btn_user);
		final String uid = SearchPreference.getFiledString(SearchMainActivity.this, SearchPreference.SEARCH_UID, null);
		final String token = SearchPreference.getFiledString(SearchMainActivity.this, SearchPreference.SEARCH_TOKEN, null);
		btn_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (uid != null && !"".equals(uid) && token != null && !"".equals(token)) {
					System.out.println("------已经执行-----");
//					if(islogin(uid, token)){
//						Intent intent = new Intent(SearchMainActivity.this, UserSettingActivity.class);
//						startActivity(intent);
//					}else{
//						Intent intent = new Intent(SearchMainActivity.this, RegisterActivity.class);
//						startActivity(intent);
//					}
					islogin(uid, token);
				}else{
					System.out.println("------已经执行3-----");
					Intent intent = new Intent(SearchMainActivity.this, RegisterActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		pager = (ViewPager) findViewById(R.id.viewpage);
		final ArrayList<View> list = new ArrayList<View>();
		Intent intent2 = new Intent(context, SearchSauseActivity.class);
		list.add(getView("A", intent2));
		Intent intent = new Intent(context, NavigationActivity.class);
		list.add(getView("B", intent));
		
		adapterPage = new MyPagerAdapter(list); 
		pager.setAdapter(adapterPage);
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化小圆点
	 */
	private void initImageView(){
		group = (ViewGroup) findViewById(R.id.viewGroup);
		
		imageViews = new ImageView[2];
		for (int i = 0; i < imageViews.length; i++) {
			imageView = new ImageView(SearchMainActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			

			group.addView(imageViews[i]);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * 通过activity获取视图
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * Pager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter{
		List<View> list =  new ArrayList<View>();
		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);

				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}
	UserInfoJ info;
	List<NameValuePair> isparams;
	public void islogin(String uid, String token){
		if(!SearchUtil.isAvailableNetwork(SearchMainActivity.this)){
			Toast.makeText(SearchMainActivity.this, getResources().getString(R.string.netfalse), Toast.LENGTH_LONG).show();
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
						info = new UserInfoJ(SearchMainActivity.this, result);
						if(info.success){
							Message msg = new Message();
							msg.what = 0; 
							userA.sendMessage(msg);
						}else{
							Message msg = new Message();
							msg.what = 1; 
							userA.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		isthread.start();
	}
	
	Handler userA = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				System.out.println("------已经执行1-----");
				Intent intent = new Intent(SearchMainActivity.this, UserSettingActivity.class);
				startActivity(intent);
			}else{
				System.out.println("------已经执行2-----");
				Intent intent = new Intent(SearchMainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		};
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			init();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}