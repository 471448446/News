package com.better.news.support;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {
	private static DialogHelper instance;
	public static DialogHelper getInstance(){
		if(instance==null){
			instance= new DialogHelper();
		}
		return instance;
	}
	public Dialog getSingleChooseDialog(Context context,String title,CharSequence[] items, int checkedItem,
											   final DialogInterface.OnClickListener listener){
		return new AlertDialog.Builder(context).setTitle(title).setSingleChoiceItems(items,checkedItem,listener).create();
	}
}
