package com.cutepics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private static Bitmap bitmap;
	private static boolean isLocked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		relativeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				createThreadToDoStuff();
				return false;
			}
		});
		createThreadToDoStuff();
	}

	private void createThreadToDoStuff(){
		if (isLocked){
			return;
		}
		isLocked = true;
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					getAndSetImage();
				} catch (Exception e) {
					isLocked = false;
				}
			}
		});

		thread.start();
		try {
			thread.join();
			RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
			relativeLayout.setBackground(new BitmapDrawable(getResources(),bitmap));
		} catch (Exception e) {
			isLocked = false;
		}
		isLocked = false;
	}

	private void getAndSetImage(){
		try{
			URL url = new URL(getImageUrl());

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
		} catch (Exception e) {
			Log.w(e.getMessage(), e.getMessage());
		}
	}

	private String getImageUrl(){
		try {
			String imgurString = getHtmlFromImgur();

			ArrayList<String> matches = new ArrayList<String>();

			String[] splits = imgurString.split("<img");

			for(String item : splits){

				Pattern r = Pattern.compile("i.(imgur.com/[\\w\\d]+.(gif|jpg|png)?)");

				Matcher m = r.matcher(item); 
				if (m.find()){
					matches.add("http://i." + m.toMatchResult().group(1));
				}
			}

			int random = (int)(Math.random() * matches.size());
			return matches.get(random);
		} catch (IOException e) {
		}
		return null;
	}

	private String getHtmlFromImgur() throws IOException{
		URL url = new URL("http://imgur.com/r/aww/top?desktop=1");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		String html = "";
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
		{
			str.append(line);
		}
		in.close();
		html = str.toString();
		return html;
	}
}
