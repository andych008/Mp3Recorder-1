package com.jason.recordlibrary.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.jason.recordlibrary.R;

/**
 * Created by Admin on 2017-08-07.
 */

public class RecordViewDialog extends Dialog {
    private Context context;
    private VoiceLineView voicLine;
    private View record_actions;
    private View play_actions;
    private ImageView ok, delete, reset;
    private Chronometer chronometer;
    private View.OnClickListener listener;

    public RecordViewDialog(Context context, int theme, View.OnClickListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        this.setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        chronometer.stop();
    }

    public void setVolume(int volume) {
        voicLine.setVolume(volume);
    }

    public void recordStart() {
        ok.setImageResource(R.drawable.aar_ic_stop);
        voicLine.setVisibility(View.VISIBLE);
        chronometer.start();
        record_actions.setVisibility(View.VISIBLE);
        play_actions.setVisibility(View.GONE);
    }

    public void recordStop() {
        ok.setImageResource(R.drawable.hollow_circle_bg);
        voicLine.setVisibility(View.GONE);
        chronometer.stop();
        chronometer.setVisibility(View.GONE);
        record_actions.setVisibility(View.GONE);
        play_actions.setVisibility(View.VISIBLE);
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.record_view_dialog, null);
        setContentView(view);
        voicLine = (VoiceLineView) view.findViewById(R.id.voicLine);
        record_actions = view.findViewById(R.id.record_actions);
        play_actions = view.findViewById(R.id.play_actions);
        ok = (ImageView) view.findViewById(R.id.iv_record_record);
        delete = (ImageView) view.findViewById(R.id.delete);
        reset = (ImageView) view.findViewById(R.id.iv_reset);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);

        view.findViewById(R.id.iv_audio_play).setOnClickListener(listener);
        view.findViewById(R.id.iv_audio_save).setOnClickListener(listener);
        ok.setOnClickListener(listener);
        delete.setOnClickListener(listener);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok.setImageResource(R.drawable.hollow_circle_bg);
                voicLine.setVisibility(View.GONE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.setVisibility(View.VISIBLE);
                record_actions.setVisibility(View.VISIBLE);
                play_actions.setVisibility(View.GONE);
            }
        });
        //设置dialog大小，这里是一个小赠送，模块好的控件大小设置
        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);//设置对话框位置
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(params);
    }

}