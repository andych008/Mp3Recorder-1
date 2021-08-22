package com.jason.recordlibrary;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.jason.recordlibrary.utils.FileUtil;
import com.jason.recordlibrary.view.RecordViewDialog;

import java.io.File;

/**
 * Created by Admin on 2017-08-07.
 */

public class Mp3Recorder {
    private static Mp3Recorder instance;
    private String mp3Name = "temp.mp3";
    private RecordThread recordThread;
    private File file;
    private RecordViewDialog recordViewDialog;
    private RecordListener recordListener;
    private Context context;

    private Mp3Recorder() {
    }

    public static synchronized Mp3Recorder getInstance() {
        if (instance == null) {
            instance = new Mp3Recorder();
        }
        return instance;
    }

    public Mp3Recorder setListener(RecordListener listener) {
        recordListener = listener;
        return instance;
    }

    public void start(Context context) {
        this.context = context;
        recordViewDialog = new RecordViewDialog(context, R.style.Dialog, onClickListener);
        recordViewDialog.show();
    }

    private void recordStart() {
        if (recordThread != null) {
            recordStop();
        }
        file = new File(FileUtil.getCacheRootFile(context), mp3Name);
        recordThread = new RecordThread(file, handler);
        recordThread.start();
        recordViewDialog.recordStart();
    }

    private void recordStop() {
        if (recordThread != null) {
            recordThread.quit();
            recordThread = null;
        }
    }

    private void save() {
        recordListener.onComplete(file.getPath());
        ;
    }

    private void dismiss() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recordViewDialog != null) recordViewDialog.dismiss();
            }
        }, 100);
    }

    private boolean hasRecord = false;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.iv_record_record) {
                if (hasRecord) {
                    hasRecord = false;
                    recordStop();
                    recordViewDialog.recordStop();
                } else {
                    hasRecord = true;
                    recordStart();
                }
            } else if (i == R.id.iv_audio_save) {
                save();
                dismiss();
            } else if (i == R.id.delete) {
                recordStop();
                new File(FileUtil.getCacheRootFile(context), mp3Name).delete();
                dismiss();
                recordListener.onCancel();
            } else if (i == R.id.iv_audio_play) {
                play();
            }
        }
    };

    private MediaPlayer mPlayer;

    private void play() {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.stop();
                        recordViewDialog.play(true);
                    }
                });
            }

            boolean hasPlay =mPlayer.isPlaying();
            recordViewDialog.play(hasPlay);
            if (hasPlay) {
                mPlayer.stop();
            } else {
                mPlayer.reset();
                mPlayer.setDataSource(file.getPath());
                mPlayer.prepare();
                mPlayer.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (recordThread == null) return;
            if (recordViewDialog != null) {
                try {
                    recordViewDialog.setVolume(Integer.parseInt(new java.text.DecimalFormat("0").format(msg.obj)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
