package com.wjhgw.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.utils.GalleryUtils;


public class GalleryDialog {
	Activity activity;
	public Dialog dialog1;
	
	public GalleryDialog(Activity context) {
		activity = context;
	}

	/**
	 * 
	 * @author 获取相片
	 *
	 */
	public void Get_pictures_Dialog() {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.photo_dialog, null);
		final Dialog dialog = new Dialog(activity, R.style.dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.show();
		LinearLayout taking_pictures = (LinearLayout) view.findViewById(R.id.taking_pictures);
		LinearLayout photo_album = (LinearLayout) view.findViewById(R.id.photo_album);
		LinearLayout cancel = (LinearLayout) view.findViewById(R.id.cancel);

		taking_pictures.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				GalleryUtils.getInstance().onCameraGetPictureClick(activity);
			}
		});

		photo_album.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				GalleryUtils.getInstance().selectPicture(activity);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	public void dismiss() {
		dialog1.dismiss();
	}
			
}
