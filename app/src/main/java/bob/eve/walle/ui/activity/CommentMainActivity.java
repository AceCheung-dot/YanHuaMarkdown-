package bob.eve.walle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import bob.eve.walle.R;
import bob.eve.walle.bean.CommentBean;
import bob.eve.walle.bean.CommentDetailBean;
import bob.eve.walle.bean.ReplyDetailBean;
import bob.eve.walle.fitpopwindow.FitPopupUtil;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.pojo.Pinglun;
import bob.eve.walle.ui.adapter.CommentExpandAdapter;
import bob.eve.walle.ui.view.CommentExpandableListView;
import bob.eve.walle.util.MyUser;
import bob.eve.walle.util.OvalImageView;

public class CommentMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CommentMainActivity";
    public static final int select_pinglun=0;
    public static final char reply_pinglun='d';
    public static final char dianzan_pinglun='b';
    public static final char quxiao_pinglun='c';
    public static final char insert_pinglun='a';
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView bt_comment;
    Markdown m=null;
    List<Pinglun> list=null;
    int markdown=0;
    private MapView mapview;
    private BaiduMap mBaiduMap;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
ImageView bt=null;

    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        String s=i.getStringExtra("markdown");
        Gson gson=new Gson();
        Markdown m=(Markdown)gson.fromJson(s, Markdown.class);
       // m.setMid(537);
        markdown=m.getMid();
        this.m=m;

        setContentView(R.layout.activity_comment_main);

        mapinit();
        initView();
        bt=(ImageView)findViewById(R.id.detail_page_focus1);
        bt.setOnClickListener(this);
    }
    public void mapinit(){
        //requestPermission();
        mapview = (MapView) findViewById(R.id.mapview2);
        initMapStatus();
        //mBaiduMap.setMyLocationEnabled(true);//开启地图的定位图层
        /* 初始化起终点Marker */
        initOverlay();

    }
    void initMapStatus(){
        mBaiduMap = mapview.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        mBaiduMap.setCompassEnable(true);

        builder.target(new LatLng(m.getMlatitude(), m.getMlongtitude())).zoom(15);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
    void initOverlay(){
        View popMarker = View.inflate(this, R.layout.pop, null);

        popMarker.setDrawingCacheEnabled(true);
        OvalImageView iv=(OvalImageView) popMarker.findViewById(R.id.iv_title);
        TextView tv=(TextView) popMarker.findViewById(R.id.tv_title);
        iv.setImageBitmap(BitmapFactory.decodeByteArray(m.getMicon(),0,m.getMicon().length));
        iv.setStrokeColot(Color.argb(0.2F,1,1,1));
        iv.setStrokeWidth(9);
        tv.setText("   "+m.getMtitle());
        Bitmap bitmap1 = getViewBitmap(popMarker);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(new LatLng(m.getMlatitude(), m.getMlongtitude()))
                .title(""+m.getMid())
                .icon(bitmapDescriptor)
                .draggable(true);
        mBaiduMap.addOverlay(option);


    }
    private void sendRequestWithkHttpURLConnection2(final int markdown){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {

                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/PinglunServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectpinglun?mode=a&pmarkdown="+markdown;
                    URL url=new URL(s2);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d("info1111",new String(response));
                    final List<CommentDetailBean> list=generateTestData(response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initExpandableListView(list);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }
    private void sendRequestWithkHttpURLConnectionReply(final Pinglun ping,final char mode){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {

                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/PinglunServlet?mode="+mode+ping.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectpinglun?mode=a&pmarkdown=";
                    URL url=new URL(s);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);
                    InputStream is=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    if(mode==insert_pinglun){
                        sendRequestWithkHttpURLConnection2(m.getMid());
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try {
                            reader.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        //commentsList = generateTestData();
        sendRequestWithkHttpURLConnection2(m.getMid());
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        adapter.setPings(list);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(CommentMainActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2018/04/20
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        CommentDetailBean bean=new CommentDetailBean("123","HelloWorld","2019-9-18");
        bean.setId(1);
        bean.setImgId(""+R.id.comment_item_logo);
        bean.setUserLogo("http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png");
        bean.setReplyTotal(0);
        commentList.add(bean);
        return commentList;
    }

    private List<CommentDetailBean> generateTestData(String response){
        Gson gson = new Gson();
        Type type=new TypeToken<LinkedList<Pinglun>>(){}.getType();
        list=new Gson().fromJson(response,type);
        commentBean = gson.fromJson(testJson, CommentBean.class);
        commentsList = new LinkedList<>();
        for(int i=0;i<list.size();i++) {
            Pinglun ping=list.get(i);
            CommentDetailBean bean = new CommentDetailBean(ping.getPt1(),ping.getPcontent(), ping.getPtime().toString());
            bean.setId(commentsList.size());
            bean.setDianzan(ping.getPdianzan());
            bean.setNickName(ping.getPt1());
            bean.setImgId(ping.getPt1());
            bean.setUserLogo("http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png");
            StringTokenizer st=new StringTokenizer(ping.getPt2(),"<>");
            try {
                List<ReplyDetailBean>replybean=new LinkedList<>();
                for(;st.countTokens()>=3;)
                { ReplyDetailBean bean2;
                    if(st.countTokens()>=3) {
                        String name = st.nextToken();
                        Log.d("name",name);
                        String huifu = st.nextToken();
                        Log.d("huifu",huifu);
                        String dianzan=st.nextToken();
                        Log.d("dianzan",dianzan);
                        bean2=new ReplyDetailBean(name,huifu);
                        replybean.add(bean2);
                    }
                }
                bean.setReplyTotal(replybean.size());
                if(replybean.size()>0){
                    bean.setReplyList(replybean);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            commentsList.add(bean);
        }
        return commentsList;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
        if(view.getId() == R.id.detail_page_focus1){

            initPopup(bt);
        }
    }
    private void initPopup(View anchorView) {
        FitPopupUtil fitPopupUtil = new FitPopupUtil(this,markdown);
        fitPopupUtil.setOnClickListener(new FitPopupUtil.OnCommitClickListener() {
            @Override
            public void onClick(String reason) {
                Toast.makeText(CommentMainActivity.this,reason,Toast.LENGTH_SHORT).show();
            }
        });
        fitPopupUtil.showPopup(anchorView);
    }
    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    Pinglun pinglun=new Pinglun();
                    pinglun.setPicon("null".getBytes());
                    pinglun.setPcontent(commentContent);
                    //FIXME
                    pinglun.setPmarkdown(m.getMid());
                    pinglun.setPdafen(0);
                    pinglun.setPdianzan(0);
                    pinglun.setPuser(MyUser.getMe().getUid());
                    pinglun.setPtime(new Timestamp(System.currentTimeMillis()));
                    pinglun.setPt1(MyUser.getMe().getUnicheng());
                    pinglun.setPt2("");
                    pinglun.setPt3("0");
                    pinglun.setPt4("0");
                    pinglun.setPt5("0");
                    sendRequestWithkHttpURLConnectionReply(pinglun,insert_pinglun);


                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(MyUser.getMe().getUnicheng(), commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(CommentMainActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CommentMainActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        final int pid=commentsList.get(position).getId();
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(MyUser.getMe().getUnicheng(),replyContent);
                    Pinglun ping=list.get(pid);
                    StringBuffer pt2=new StringBuffer(ping.getPt2());
                    pt2.append(MyUser.getMe().getUnicheng()+">"+replyContent+">"+1+"<");
                    ping.setPicon("null".getBytes());
                    ping.setPt2(pt2.toString());
                    sendRequestWithkHttpURLConnectionReply(ping,reply_pinglun);

                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(CommentMainActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CommentMainActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

}
