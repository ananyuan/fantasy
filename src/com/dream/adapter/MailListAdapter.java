package com.dream.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dream.R;
import com.dream.db.model.MailBean;

public class MailListAdapter extends BaseAdapter {

	private Activity activity;
	private List<MailBean> data;
	private static LayoutInflater inflater = null;
	
	public MailListAdapter(Activity a, List<MailBean> datalist) {
		activity = a;
		data = datalist;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		if (data != null && data.size() != 0) {
			return data.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.list_mail_item, null);
			mHolder = new ViewHolder();
			mHolder.mail_from = (TextView)view.findViewById(R.id.mail_from);
			mHolder.mail_sendTime = (TextView)view.findViewById(R.id.mail_sendTime);
			mHolder.mail_subject = (TextView)view.findViewById(R.id.mail_subject);
			
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		MailBean mailbean = (MailBean) getItem(position);
		mHolder.mail_from.setText(mailbean.getFrom());
		mHolder.mail_sendTime.setText(mailbean.getSendTime());
		mHolder.mail_subject.setText(mailbean.getSubject());
		
		return view;
	}

	static class ViewHolder {
		TextView mail_from;
		TextView mail_sendTime;
		TextView mail_subject;
	}
}
