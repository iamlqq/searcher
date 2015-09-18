package com.zhihui.quicksearch.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihui.quicksearch.bean.RulesCustom;
import com.zhihui.quicksearch.main.R;

public class CustomAdapter extends BaseAdapter{

	List<RulesCustom> list;
	Context context;
	public Map<Integer, Boolean> isSelected; 
//	AsynImageLoader asynImageLoader;
	
	public CustomAdapter(Context context, List<RulesCustom> list){
		this.context = context;
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
		RulesCustom dataInfo = list.get(arg0);
		ViewHolder holder;
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.custom_listitem, null);

			holder = new ViewHolder();
//			holder.soft_icon = (ImageView) arg1.findViewById(R.id.custom_icon);
			holder.sause_name = (TextView) arg1.findViewById(R.id.custom_name);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		if(list != null){
			if(dataInfo.id == 9999){
//				holder.soft_icon.setImageResource(R.drawable.ic_launcher);
				holder.sause_name.setText("Add+");
			}else{
				holder.sause_name.setText(dataInfo.customName);
			}
		}
		
		return arg1;
	}
	
	public static class ViewHolder{
//		ImageView soft_icon;
		TextView sause_name;
	}
}
