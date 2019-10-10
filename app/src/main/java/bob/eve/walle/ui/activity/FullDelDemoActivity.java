package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import bob.eve.walle.R;
import bob.eve.walle.pojo.BordingContent;
import bob.eve.walle.pojo.FullDelDemoAdapter;
import bob.eve.walle.pojo.SwipeBean;
import bob.eve.walle.pojo.User;
import bob.eve.walle.util.MyUser;

public class FullDelDemoActivity extends Activity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private FullDelDemoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<SwipeBean> mDatas;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_del_demo);
        mRv = (RecyclerView) findViewById(R.id.rv);
fab=(FloatingActionButton)findViewById(R.id.fab_fu_add_task);
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(FullDelDemoActivity.this,AddTaskActivity.class);
        startActivity(i);
    }
});
       // initDatas();
        mAdapter = new FullDelDemoAdapter(this, mDatas);
        mAdapter.setOnDelListener(new FullDelDemoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < mDatas.size()) {
                    Toast.makeText(FullDelDemoActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    mDatas.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
                sendRequestWithkHttpURLConnection2(MyUser.getMe().getUid());
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < mDatas.size()) {
                    SwipeBean swipeBean = mDatas.get(pos);
                    mDatas.remove(swipeBean);
                    mAdapter.notifyItemInserted(0);
                    mDatas.add(0, swipeBean);
                    mAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRv.scrollToPosition(0);
                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                }
            }
        });
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 2));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
        sendRequestWithkHttpURLConnection2(MyUser.getMe().getUid());
    }
    private void sendRequestWithkHttpURLConnection2(final int id){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/BordingContentServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectbording?mode=a&buser="+id;
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
                    showResponse(response.toString());

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
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gs=new Gson();
                //解析泛型
                Type type=new TypeToken<LinkedList<BordingContent>>(){}.getType();
                List<BordingContent> list=new Gson().fromJson(response,type);
             ArrayList<SwipeBean> li= new ArrayList<>();
                for(int i=0;i<list.size();i++) {
                    BordingContent bc = list.get(i);
                    StringTokenizer st = new StringTokenizer(bc.getBcontent(), "<>_");
                    String user = st.nextToken();
                    String title = st.nextToken();
                    String content = st.nextToken();
                    String time = bc.getBctime().toString();
                    SwipeBean sb = new SwipeBean(user, time, title, content);

                        li.add(sb);
                }
                   FullDelDemoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initDatas(li);

                        }
                    });



            }
        });

    }
    private void initDatas(ArrayList<SwipeBean> li) {
        mDatas =  li;
        mRv = (RecyclerView) findViewById(R.id.rv);

        // initDatas();
        mAdapter = new FullDelDemoAdapter(this, mDatas);
        mAdapter.setOnDelListener(new FullDelDemoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < mDatas.size()) {
                    Toast.makeText(FullDelDemoActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    mDatas.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < mDatas.size()) {
                    SwipeBean swipeBean = mDatas.get(pos);
                    mDatas.remove(swipeBean);
                    mAdapter.notifyItemInserted(0);
                    mDatas.add(0, swipeBean);
                    mAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRv.scrollToPosition(0);
                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                }
            }
        });
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 1));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

    }
}
