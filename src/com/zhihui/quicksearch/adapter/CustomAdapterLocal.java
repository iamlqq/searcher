package com.zhihui.quicksearch.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihui.quicksearch.bean.RulesCustomLocal;
import com.zhihui.quicksearch.main.R;

public class CustomAdapterLocal extends BaseAdapter{

	List<RulesCustomLocal> list;
	Context context;
	
	public CustomAdapterLocal(Context context, List<RulesCustomLocal> list){
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
		System.out.println("-----------" + list.size());
		RulesCustomLocal dataInfo = list.get(arg0);
		System.out.println("listcustom"+list.get(arg0).customName);
		ViewHolder holder;
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.custom_listitem, null);

			holder = new ViewHolder();
			holder.sause_name = (TextView) arg1.findViewById(R.id.custom_name);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		if(list != null){
			if(dataInfo.id_c == 9999){
				holder.sause_name.setText("Add+");
			}else{
				holder.sause_name.setText(dataInfo.customName);
			}
		}
		
		return arg1;
	}
	
	public static class ViewHolder{
		TextView sause_name;
	}
}