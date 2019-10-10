package bob.eve.walle.WeChat;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import blcs.lwb.lwbtool.PublicFragmentActivity;
import blcs.lwb.lwbtool.base.BaseFragment;

import blcs.lwb.lwbtool.utils.IntentUtils;
import bob.eve.walle.R;
import bob.eve.walle.WeChat.adapter.BaseDemoAdapter.MultipleItem;
import bob.eve.walle.WeChat.adapter.BaseDemoAdapter.NormalMultipleEntity;
import bob.eve.walle.WeChat.bean.MySection;
import bob.eve.walle.WeChat.bean.Status;
import bob.eve.walle.WeChat.bean.Video;
import bob.eve.walle.WeChat.manager.FramentManages;

public class MyUtils {
    private static final String CYM_CHAD = "CymChad";
    private static final String CHAY_CHAN = "ChayChan";
    private static final String HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460";
    /**
     * 获取数组
     * @param activity
     * @param a
     * @return
     */
    public static ArrayList<String> getArray(Activity activity, int a){
        ArrayList<String> lists = new ArrayList<>();
        lists.addAll(Arrays.asList(activity.getResources().getStringArray(a)));
        return lists;
    }

    /**
     * recycyler 数据
     * @return
     */
    public static List<MultipleItem> getMultipleItemData() {
        List<MultipleItem> list = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, CYM_CHAD));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN));
        }

        return list;
    }
    /**
     * recycyler 复杂数据
     * @return
     */
    public static List<NormalMultipleEntity> getNormalMultipleEntities() {
        List<NormalMultipleEntity> list = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            list.add(new NormalMultipleEntity(NormalMultipleEntity.SINGLE_IMG));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.SINGLE_TEXT,CHAY_CHAN));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CHAY_CHAN));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CYM_CHAD));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.TEXT_IMG,CHAY_CHAN));
        }
        return list;
    }
    /**
     * recycyler 分组数据
     * @return
     */
    public static List<MySection> getSampleData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "Section 1", true));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(true, "Section 2", false));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(true, "Section 3", false));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(true, "Section 4", false));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(true, "Section 5", false));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        return list;
    }

    public static List<Status> getSampleData(int lenth) {
        List<Status> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Status status = new Status();
            status.setUserName("Chad" + i);
            status.setCreatedAt("04/05/" + i);
            status.setRetweet(i % 2 == 0);
            status.setUserAvatar("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460");
            status.setText("BaseRecyclerViewAdpaterHelper https://www.recyclerview.org");
            list.add(status);
        }
        return list;
    }

    /**
     * 跳转Url
     */
    public static void toUrl(BaseFragment activity, String title, String url){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Item_Name,title);
        bundle.putString(Constants.URL, url);
        activity.addFrament(R.id.fr_contain,  FramentManages.Demo, bundle, true);
    }

    /**
     * 跳转Demo
     */
    public static void toDemo(Activity activity,Bundle bundle){
        IntentUtils.toActivity(activity, PublicFragmentActivity.createIntent(activity, FramentManages.Demo, bundle));
    }

    /**
     * 跳转指定Fragment
     */
    public static void toFragment(Activity activity,Bundle bundle,String tag){
        IntentUtils.toActivity(activity,PublicFragmentActivity.createIntent(activity,tag, bundle));
    }
}
