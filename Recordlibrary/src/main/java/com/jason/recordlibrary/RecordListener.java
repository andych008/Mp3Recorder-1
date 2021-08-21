package com.jason.recordlibrary;

/**
 * Created by Admin on 2017-08-07.
 */

public interface RecordListener {
    void onComplete(String path);
    void onCancel();
}
