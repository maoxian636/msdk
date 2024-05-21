package com.msdk.xsdk.impl;
public interface InitializationCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
