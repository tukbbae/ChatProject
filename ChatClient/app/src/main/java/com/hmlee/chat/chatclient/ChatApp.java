package com.hmlee.chat.chatclient;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.hmlee.chat.chatclient.db.RecipientIdCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by HM on 2016. 7. 18..
 */
public class ChatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader(getApplicationContext());
        Firebase.setAndroidContext(this);
        RecipientIdCache.init(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }
}
