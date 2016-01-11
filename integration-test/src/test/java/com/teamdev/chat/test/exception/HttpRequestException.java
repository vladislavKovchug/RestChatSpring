package com.teamdev.chat.test.exception;

import org.apache.http.StatusLine;

public class HttpRequestException extends RuntimeException {

    private StatusLine statusLine;

    public HttpRequestException(StatusLine statusLine) {
        super(statusLine.toString());
        this.statusLine = statusLine;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }
}
