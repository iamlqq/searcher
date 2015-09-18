package com.zhihui.quicksearch.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihui.quicksearch.bean.RulesNavigationOne;
import com.zhihui.quicksearch.bean.RulesNavigationTwo;
import com.zhihui.quicksearch.main.R;
import com.zhihui.quicksearch.util.AsynLoadimg;

public class AddCustomAdapter extends BaseAdapter{

	Context ctx;
	List<RulesNavigationTwo> list;
	AsynLoadimg asyn = new AsynLoadimg();
	
	public AddCustomAdapter(Context ctx, List<RulesNavigationTwo> list){
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
		RulesNavigationTwo dataInfo = list.get(arg0);
		ViewHolder holder;
		if(arg1 == null){
			arg1 = LayoutInflater.from(ctx).inflate(R.layout.addcustom_listitem, null);

			holder = new ViewHolder();
			holder.navione_name = (TextView) arg1.findViewById(R.id.add_name);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		if(list != null){
			holder.navione_name.setText(dataInfo.naviName);
		}
		
		return arg1;
	}
	
	public static class ViewHolder{
//		ImageView soft_icon;
		TextView navione_name;
	}
}
