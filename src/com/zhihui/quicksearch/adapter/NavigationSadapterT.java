package com.zhihui.quicksearch.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihui.quicksearch.adapter.NavigationAdapterTwo.ViewHolder;
import com.zhihui.quicksearch.bean.NavigationTwo;
import com.zhihui.quicksearch.bean.RulesNavigationTwo;
import com.zhihui.quicksearch.main.R;

public class NavigationSadapterT extends BaseAdapter{

	Context ctx;
	List<NavigationTwo> list;
	
	public NavigationSadapterT(Context ctx, List<NavigationTwo> list){
		this.ctx = ctx;
		this.list = list;
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
		NavigationTwo dataInfo = list.get(arg0);
		ViewHolder holder;
		if(arg1 == null){
			arg1 = LayoutInflater.from(ctx).inflate(R.layout.navigationtwos_listitem, null);

			holder = new ViewHolder();
			holder.navitwo_name = (TextView) arg1.findViewById(R.id.navitwo_name);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		if(list != null){
			holder.navitwo_name.setText(dataInfo.naviTwoName);
		}
		
		return arg1;
	}
	
	public static class ViewHolder{
		TextView navitwo_name;
	}
}