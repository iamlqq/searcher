package com.zhihui.quicksearch.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhihui.quicksearch.bean.RulesSearchSause;
import com.zhihui.quicksearch.http.SearchPreference;
import com.zhihui.quicksearch.main.R;
import com.zhihui.quicksearch.util.AsynLoadimg;
import com.zhihui.quicksearch.util.MD5;

public class SearchSauseAdapter extends BaseAdapter{

	List<RulesSearchSause> list;
	Context context;
	public Map<Integer, Boolean> isSelected; 
	public List beSelectedData = new ArrayList();
	private File cache;
	AsynLoadimg asyn = new AsynLoadimg();
	
	public SearchSauseAdapter(Context context, List<RulesSearchSause> list, File cache){
		this.context = context;
		this.list = list;
		this.cache = cache;
		initSelect();
	}
	
	public void initSelect(){
		int search_int = SearchPreference.getFiledInt(context, SearchPreference.SEARCH_KEY_INT, 0);
        if (isSelected != null){
        	isSelected = null;
        }  
        isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++) { 
			if(search_int == i){
				isSelected.put(i, true);
			}else{
				isSelected.put(i, false);  
			}
      	}  
      	// 清除已经选择的项  
      	if (beSelectedData.size() > 0) {  
    	    beSelectedData.clear();  
      	} 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RulesSearchSause dataInfo = list.get(arg0);
		ViewHolder holder;
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.searchsause_listitem, null);

			holder = new ViewHolder();
			holder.soft_icon = (ImageView) arg1.findViewById(R.id.soft_icon);
			holder.sause_name = (TextView) arg1.findViewById(R.id.sause_name);
			holder.cb = (CheckBox) arg1.findViewById(R.id.sause_box);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		if(list != null){
			holder.sause_name.setText(dataInfo.sauseName);
			holder.cb.setChecked(isSelected.get(arg0));  
			// 异步的加载图片 (线程池 + Handler ) ---> AsyncTask
			 
			asyn.asyncloadImage(holder.soft_icon, dataInfo.icon, cache);
		}
		
		return arg1;
	}
	
	public static class ViewHolder{
		ImageView soft_icon;
		TextView sause_name;
		CheckBox cb;
	}
	
}
