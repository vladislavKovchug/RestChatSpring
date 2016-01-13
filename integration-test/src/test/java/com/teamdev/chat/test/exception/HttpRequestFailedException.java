package com.teamdev.chat.test.exception;

import org.apache.http.StatusLine;

public class HttpRequestFailedException extends RuntimeException {

    private StatusLine statusLine;

    public HttpRequestFailedException(StatusLine statusLine) {
        super(statusLine.toString());
        this.statusLine = statusLine;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }
}
