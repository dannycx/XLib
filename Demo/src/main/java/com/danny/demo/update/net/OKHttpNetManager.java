package com.danny.demo.update.net;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpNetManager implements INetManager {
    private static OkHttpClient sInstance;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS);
        sInstance = builder.build();

        // https 自签名  OKHttp握手错误，使用下面方法，设置证书相关
//        builder.sslSocketFactory();

    }

    @Override
    public void get(String url, INetCallback callback, Object tag) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().tag(tag).build();
        Call call = sInstance.newCall(request);
//        Response response = call.execute();// 同步
        call.enqueue(new Callback() {// 异步
            @Override
            public void onFailure(Call call, IOException e) {
                // 非ui线程
                sHandler.post(() -> callback.failed(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    sHandler.post(() -> callback.success(result));
                } catch (Throwable throwable) {
                    callback.failed(throwable);
                }
            }
        });
    }

    @Override
    public void download(String url, File targetFile, INetDownloadCallback callback, Object tag) {
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().tag(tag).build();
        Call call = sInstance.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sHandler.post(() -> callback.failed(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                OutputStream os = null;

                long totalLen = response.body().contentLength();

                try {
                    is = response.body().byteStream();
                    os = new FileOutputStream(targetFile);

                    byte[] buffer = new byte[8 * 1024];
                    long curLen = 0;

                    int bufferLen = 0;
                    while (!call.isCanceled() && (bufferLen = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bufferLen);
                        os.flush();
                        curLen += bufferLen;

                        long finalCurLen = curLen;
                        sHandler.post(() -> {
                            callback.progress((int) (finalCurLen * 1.0f / totalLen * 100));
                        });
                    }

                    if (call.isCanceled()) {
                        return;
                    }

                    try {
                        // 不存sd卡中，需设置可执行，可读，可写
                        targetFile.setExecutable(true, false);
                        targetFile.setReadable(true, false);
                        targetFile.setWritable(true, false);
                    } catch (Exception e) {

                    }

                    sHandler.post(() -> {
                       callback.success(targetFile);
                    });


                } catch (Throwable throwable) {
                    if (call.isCanceled()) {
                        return;
                    }
                    sHandler.post(() -> {
                        callback.failed(throwable);
                    });
                } finally {
                    if (is != null) {
                        is.close();
                    }

                    if (os != null) {
                        os.close();
                    }
                }
            }
        });
    }

    @Override
    public void cancel(Object tag) {
        List<Call> calls = sInstance.dispatcher().queuedCalls();
        if (calls != null) {// 排队call
            for (Call call : calls) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }

        List<Call> callList = sInstance.dispatcher().runningCalls();
        if (callList != null) {// 正在执行call
            for (Call call : callList) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
