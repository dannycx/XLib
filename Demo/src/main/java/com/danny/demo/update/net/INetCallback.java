package com.danny.demo.update.net;

public interface INetCallback {
    void success(String response);

    void failed(Throwable throwable);
}
