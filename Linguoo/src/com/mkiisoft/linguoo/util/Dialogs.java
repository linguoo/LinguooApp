package com.mkiisoft.linguoo.util;

import com.mkiisoft.linguoo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.util.Linkify;

public class Dialogs {
	public static void showAlertDialog(Context context, String title, String message, String btnText) {
		final SpannableString s = new SpannableString(message);
	    Linkify.addLinks(s, Linkify.ALL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setIcon(R.drawable.linguoo)
		.setMessage(s)
		.setCancelable(false)
		.setNegativeButton(btnText,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}

}
