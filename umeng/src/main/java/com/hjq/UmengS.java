package com.hjq;

import android.content.Context;
import android.util.Log;

import com.umeng.message.UTrack;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.umeng.message.UmengMessageCallbackHandlerService.TAG;
import static org.android.agoo.common.AgooConstants.MESSAGE_BODY;

public class UmengS {
    public static String getMessageBodyTitle(){
        return MESSAGE_BODY;
    }
    public static Map<String,String> getMessageMap(String Message, Context context){


        UMessage msg = null;
        try {
            msg = new UMessage(new JSONObject(Message));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UTrack.getInstance(context).trackMsgClick(msg);

        Map<String, String> extra =new HashMap<String, String>() ;
        extra.put("title",msg.title);
        extra.put("text",msg.text);

        Log.d(TAG, "message=" + msg.msg_id); //消息体

        Log.d(TAG, "custom=" + msg.custom); //自定义消息的内容

        Log.d(TAG, "title=" + msg.title); //通知标题

        Log.d(TAG, "text=" + msg.text); //通知内容

        return extra;



    }
}
