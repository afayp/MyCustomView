package com.pfh.mycustomview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.view.SlackLoadingView;

/**
 * Created by afayp on 2016/8/23.
 */
public class SlackLoadingActivity extends BaseActivity {
    private SlackLoadingView mLoadingView;
    private SeekBar mSbSize, mSbDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slack);
        setToolbarTitle("slackLoading");
        mLoadingView = (SlackLoadingView) findViewById(R.id.loading_view);
        mSbSize = (SeekBar) findViewById(R.id.sb_size);
        mSbDuration = (SeekBar) findViewById(R.id.sb_duration);

        mSbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mLoadingView.setLineLength(i / 100f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mLoadingView.setDuration(i / 100f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                mLoadingView.start();
                break;
            case R.id.btn_reset:
                mLoadingView.reset();
                break;
        }
    }

}
