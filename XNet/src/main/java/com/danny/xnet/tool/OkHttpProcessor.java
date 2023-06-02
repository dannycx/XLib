package com.danny.xnet.tool;

import android.os.Handler;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpProcessor /*implements INetworkProcessor*/ {
//    private Handler handler;
//    private OkHttpClient client;
//
//    public OkHttpProcessor() {
//        handler = new Handler();
//        client = new OkHttpClient();
//    }
//
//    @Override
//    public void post(String url, Map<String, Object> params, Callback callback) {
//        FormBody.Builder builder = new FormBody.Builder();
//        if (params == null || params.isEmpty()) {
//            builder.build();
//        }
//
//        for (Map.Entry<String, Object> entry : params.entrySet()){
//            builder.add(entry.getKey(), entry.getValue().toString());
//        }
//        RequestBody body = builder.build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
////                        String result = response.body().string();
//                    }
//                });
//            }
//        });
//    }
}
