package com.goockr.inductioncooker.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;

/**
 * 
 * @Description Toast统一管理类
 * @author wenhan
 * @date 2016.11.26
 * 
 */
public class MyToast {

	private MyToast() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;
	
	public static Toast toast  = null;

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (isShow)
		{
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (isShow)
		{
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow)
		{
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (isShow)
		{
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
		{
			Toast.makeText(context, message, duration).show();
		}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow)
		{
			Toast.makeText(context, message, duration).show();
		}
	}



	/**
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void showToastCustomerStyleText(Context context, int message, int duration) {
		LayoutInflater inflaterStyle = LayoutInflater.from(context);
		View view = inflaterStyle.inflate(R.layout.toast_bg, null);
		TextView text = (TextView) view.findViewById(R.id.tvContent);
		text.setText(context.getText(message));
		if (toast == null){
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(duration);
		}else{
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(duration);
		}
		if (isShow)
		{
			toast.show();
		}

	}
	
	/**
	 * 
	 * @param context
	 * @param message
	 * 
	 */
	@SuppressWarnings("unused")
	public static void showToastCustomerStyleText(Context context, String message) {
		LayoutInflater inflaterStyle = LayoutInflater.from(context);
		View view = inflaterStyle.inflate(R.layout.toast_bg, null);
		TextView text = (TextView) view.findViewById(R.id.tvContent);
		text.setText(message);
		if (toast == null){
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
		}else{
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		if (isShow)
		{
			toast.show();
		}

	}

	
	/**
	 * 
	 * @param context
	 *
	 */
	public static void showToastCustomerStyleText(Context context, int messageId) {
		LayoutInflater inflaterStyle = LayoutInflater.from(context);
		View view = inflaterStyle.inflate(R.layout.toast_bg, null);
		TextView text = (TextView) view.findViewById(R.id.tvContent);
		text.setText(messageId);
		if (toast == null){
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
		}else{
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		if (isShow)
		{
			toast.show();
		}

	}
}
