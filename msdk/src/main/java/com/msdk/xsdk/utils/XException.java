package com.msdk.xsdk.utils;

public class XException extends Exception{

    public XException() {
    }

    public XException(String message) {
        super(message);
    }

    public XException(String message, Throwable cause) {
        super(message, cause);
    }

    public XException(Throwable cause) {
        super(cause);
    }
}
