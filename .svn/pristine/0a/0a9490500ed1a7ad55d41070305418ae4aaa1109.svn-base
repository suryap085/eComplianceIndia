/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;

import java.io.File;

import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;


public class DisplayStaticVideoActivity extends Activity implements OnClickListener {

	VideoView videoView;
	Button play_button;
	Button back_button;
	String filename = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_videos);
		videoView = (VideoView) findViewById(R.id.videoView1);
		play_button = (Button) findViewById(R.id.play);
		back_button = (Button) findViewById(R.id.back);
		Bundle extras = getIntent().getExtras();
		if(extras!=null)
		{
			filename = extras
					.getString("filename");
			
		}

		play_button.setOnClickListener(this);

		

		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DisplayStaticVideoActivity.this,DisplayStaticVideosListActivity.class));
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
			}
		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play: {		
				File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);	
				String path = "//eCounseling//Videos//"+filename+".mp4";	
				File file_to_play = new File(sd,path);		
				Uri uri = Uri.fromFile(file_to_play);							
				videoView.setVideoURI(uri);			
				videoView.requestFocus();
				videoView.start();
				v.setEnabled(false);
			
			break;
		}
		}
	}
}