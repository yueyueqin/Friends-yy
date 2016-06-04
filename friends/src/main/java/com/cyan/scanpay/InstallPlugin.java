package com.cyan.scanpay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class InstallPlugin {
	
	public static final String ASSETS_PLUGIN = "BmobPayPlugin.apk";

	public static void installBmobPayPlugin(Activity activity,String fileName) {
		try {
			InputStream is = activity.getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			activity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}