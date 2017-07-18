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
import java.util.ArrayList;

import org.opasha.eCompliance.ecompliance.DbOperations.ConfigurationOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.ECounselingOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.PatientsOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.TreatmentInStagesOperations;
import org.opasha.eCompliance.ecompliance.DbOperations.VideosCategoryOperations;
import org.opasha.eCompliance.ecompliance.Model.Master;
import org.opasha.eCompliance.ecompliance.util.ConfigurationKeys;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class DisplayVideosActivity extends Activity implements OnClickListener {

	VideoView videoView;
	Button playButton;
	Button backButton;
	SeekBar seekBarVideo;

	// Playback
	int count = 1;
	int stopPosition = -1;

	String TAG = "OPASHA";

	String treatmentId = "";
	String userId = "";

	long startTime;
	String cat, stage, isDaily;
	private MediaController mc;

	ArrayList<String> videos = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_display_videos);
		mc = new MediaController(this, false);
		videoView = (VideoView) findViewById(R.id.videoView1);
		playButton = (Button) findViewById(R.id.play);
		backButton = (Button) findViewById(R.id.back);
		backButton.setEnabled(false);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			treatmentId = extras.getString(IntentKeys.key_treatment_id);
			userId = extras.getString(IntentKeys.key_visitor_id);
		}

		Master r = TreatmentInStagesOperations.getPatientRegimen(treatmentId,
				this);

		cat = r.catagory;
		stage = r.stage;

		if (PatientsOperations.isInitialCounsellingPending(treatmentId, this)) {
			stage = "IP";// If Initial Counseling Pending, consider this as IP
							// Patient as his counseling is pending.
		}

		isDaily = "0";
		if (r.daysFrequency == 1) {
			isDaily = "1";
		}

		videos = VideosCategoryOperations.GetVideoList(cat, stage, isDaily,
				this); // Get Videos for Category (I or II) and Stage (IP -
						// Start of Treatment, CP - End of IP)

		if (videos.size() == 0) {
			this.finish();
		}

		playButton.setOnClickListener(this);

		videoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {

						playButton.setEnabled(true);

						ECounselingOperations.add(treatmentId, stage, isDaily,
								userId, count, startTime,
								System.currentTimeMillis(),
								System.currentTimeMillis(),
								DisplayVideosActivity.this);

						if (count == (videos.size())) {
							showFinishPopup();
							return;
						}
						count++;
					}
				});

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_enable_pause_counseling,DisplayVideosActivity.this)
						.equals("1")) {
					int topContainerId = getResources().getIdentifier(
							"mediacontroller_progress", "id", "android");
					seekBarVideo = (SeekBar) mc.findViewById(topContainerId);
					seekBarVideo
							.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
								@Override
								public void onProgressChanged(SeekBar seekBar,
										int progress, boolean fromUser) {

								}

								@Override
								public void onStartTrackingTouch(SeekBar seekBar) {
									seekBar.setEnabled(false);
								}

								@Override
								public void onStopTrackingTouch(SeekBar seekBar) {
									seekBar.setEnabled(false);
								}
							});
				}
			}
		});

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DisplayVideosActivity.this,
						HomeActivity.class));
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				DisplayVideosActivity.this.finish();
			}
		});

	}

	private void showFinishPopup() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.custom_dialog_box_yes_no);
		Button noButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		Button yesButton = (Button) dialog.findViewById(R.id.dialogButtonYes);
		TextView message = (TextView) dialog.findViewById(R.id.messageText);
		TextView title = (TextView) dialog.findViewById(R.id.text);
		message.setText("Counselling completed for treatment id - "
				+ treatmentId + ".");
		noButton.setVisibility(View.GONE);
		yesButton.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setText("Counselling");
		yesButton.setText(getResources().getString(R.string.ok));
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				// Finish this activity and go back to Home.
				startActivity(new Intent(DisplayVideosActivity.this,
						HomeActivity.class));
				overridePendingTransition(R.anim.right_side_in,
						R.anim.right_side_out);
				DisplayVideosActivity.this.finish();
			}
		});

		dialog.show();
		return;
	}

	@Override
	public void onBackPressed() {
		// do nothing.
	}

	@Override
	public void onPause() {
		Log.v(TAG, "onPause called");
		super.onPause();
		stopPosition = videoView.getCurrentPosition(); // stopPosition is an int
		videoView.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume called");

		if (stopPosition > 0) {
			videoView.seekTo(stopPosition);
			videoView.start(); // Or use resume() if it doesn't work. I'm not
								// sure
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play: {

			if (count <= videos.size()) {

				String fileName = videos.get(count - 1); // Starts at Index 0.
															// Since this is
															// sorted by
															// priority, we will
															// follow that order

				File sd;
				sd = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				String filePath = "//eCounseling//Videos//" + fileName + ".mp4";
				File file_to_play = new File(sd, filePath);
				Uri uri = Uri.fromFile(file_to_play);
				videoView.setVideoURI(uri);
				if (ConfigurationOperations.getKeyValue(
						ConfigurationKeys.key_enable_pause_counseling, this)
						.equals("1")) {
					videoView.setMediaController(mc);
					mc.setMediaPlayer(videoView);
				}
				videoView.requestFocus();
				videoView.start();
				startTime = System.currentTimeMillis();
				v.setEnabled(false);
			}
			break;
		}
		}
	}
}