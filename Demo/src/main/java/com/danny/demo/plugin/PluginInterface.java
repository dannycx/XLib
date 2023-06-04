package com.danny.demo.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public interface PluginInterface {

    void attach(Activity activity);
    void onCreate(Bundle bundle);
    void setCreateView(View view);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
