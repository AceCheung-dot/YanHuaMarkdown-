package com.hjq.umeng;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.umeng.message.UmengMessageCallbackHandlerService.TAG;
import static org.android.agoo.common.AgooConstants.MESSAGE_BODY;

public class UmengService extends UmengMessageService {

    public static Class<?> cls;

    public static Class<?> getCls() {
        return cls;
    }

    public static  void setCls(Class<?> cls) {
        UmengService.cls = cls;
    }

    @Override

    public void onMessage(Context context, Intent intent) {

        Intent data = new Intent(intent);

        data.setClass(context, cls);

//需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，否则无法启动Activity。

        data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(data);



    }




}