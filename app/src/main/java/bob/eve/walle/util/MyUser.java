package bob.eve.walle.util;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

import java.nio.ByteBuffer;

import bob.eve.walle.R;
import bob.eve.walle.pojo.Gexing;
import bob.eve.walle.pojo.User;

public class MyUser {
    //存放用户信息的常量
   public static User me;

    public static Gexing getMygexing() {
        return mygexing;
    }

    public static void setMygexing(Gexing mygexing) {
        MyUser.mygexing = mygexing;
    }

    public static Gexing mygexing;
    public static final String DEBUG_SEVER="172.20.10.3";//Apple手机
    public static final String DEBUG_SEVER2="192.168.137.1";//笔记本热点
    public static final String FORMAL_SEVER="47.94.216.2";//正式服务器
    public static String my_sever=DEBUG_SEVER2;
    public static int mstate=1;
    public static boolean isInsert=true;
    public static double mlongtitude;
    public static double mlatitude;
    public static String myplace="";

    public static boolean isIsInsert() {
        return isInsert;
    }

    public static void setIsInsert(boolean isInsert) {
        MyUser.isInsert = isInsert;
    }

    public static int getMstate() {
        return mstate;
    }

    public static void setMstate(int mstate) {
        MyUser.mstate = mstate;
    }

    public static double getMlongtitude() {
        return mlongtitude;
    }

    public static void setMlongtitude(double mlongtitude) {
        MyUser.mlongtitude = mlongtitude;
    }

    public static double getMlatitude() {
        return mlatitude;
    }

    public static void setMlatitude(double mlatitude) {
        MyUser.mlatitude = mlatitude;
    }

    public static String getMyplace() {
        return myplace;
    }

    public static void setMyplace(String myplace) {
        MyUser.myplace = myplace;
    }

    public static boolean isB() {
        return b;
    }


    public static void setB(boolean b) {
        MyUser.b = b;
    }

static    boolean b=false;

    public static User getMe() {
        return me;
    }

    public static void setMe(User me) {
        MyUser.me = me;
    }

    static{
        me=new User();
        me.setUid(1);
        me.setUusername("0");
        me.setUjingyan(1);
        me.setUnicheng("Hello");
        me.setUpassword("0");
        mygexing=new Gexing();
        mygexing.setGid(1);
        mygexing.setGjianjie("Hello,World!");
        mygexing.setGsex("M");
        mygexing.setGuser(1);
        mygexing.setGzhuti(1);

    }

}
