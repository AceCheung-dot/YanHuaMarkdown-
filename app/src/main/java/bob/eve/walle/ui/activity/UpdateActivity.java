package bob.eve.walle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

import bob.eve.walle.Adapter.MarkdownItemAdapter;
import bob.eve.walle.R;
import bob.eve.walle.pojo.Item;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.pojo.Xingji;
import bob.eve.walle.util.MyUser;
import bob.eve.walle.util.OvalImageView;
import bob.eve.walle.widget.StarBarView;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    public LinkedList<Item> items=new LinkedList<Item>();
    public LinkedList<Item> items2=new LinkedList<Item>();
    MarkdownItemAdapter adapter=null;
    Markdown m=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        int id=getIntent().getIntExtra("id",1);
        this.sendRequestWithkHttpURLConnection2(id);

//        items.add(new Item("id","1",0));
//        items.add(new Item("时间",new Timestamp(System.currentTimeMillis()).toString(),1));
//        items.add(new Item("类型","1",2));
//        items.add(new Item("用户","1",3));
//        items.add(new Item("地点","1",4));
//        items.add(new Item("经度","37",5));
//        items.add(new Item("纬度","117",6));
//        items.add(new Item("名称","11111",7));
//        items.add(new Item("其他信息","1",8));

        //  adapter=new MarkdownItemAdapter(UpdateActivity.this,R.layout.insert_update_markdown_layout,items);
        //    adapter.setItems(items);
        // ListView lv=(ListView)findViewById(R.id.listview);
        //lv.setAdapter(adapter);
        Button insert=(Button)findViewById(R.id.buttonupdate);
        insert.setOnClickListener(this);
       StarBarView mStarbar = (StarBarView) findViewById(R.id.sbv_starbar);
      // mStarbar.setOnClickListener(this);
       mStarbar.setEnabled(false);
       ImageView iv_pinglun=(ImageView)findViewById(R.id.iV_pinglun);
       iv_pinglun.setOnClickListener(this);
       sendRequestWithkHttpURLConnectionwithXingJi(id);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonupdate){
            Log.d("info3333","3333");
            sendRequestWithkHttpURLConnection(getMarkdown());
        }

        if(v.getId()==R.id.sbv_starbar) {
            Log.d("info3333", "3333");

        }
        if(v.getId()==R.id.iV_pinglun) {
            Log.d("info3333", "3333");
            Intent i=new Intent(UpdateActivity.this,CommentMainActivity.class);
            Gson gson=new Gson();
            String data=gson.toJson(m);
            i.putExtra("markdown",data);
            startActivity(i);
        }


    }
    public Markdown getMarkdown(){
        ListView lv=(ListView)findViewById(R.id.lv_2);
        items2=adapter.getItems();

        for(int j=1;j<items.size();j++){
            Log.d("lll",adapter.getItem(j).getValue());

        }
        Markdown m=new Markdown();
        m.setMid(new Integer(adapter.getItem(0).getValue()));
        Timestamp time2= new Timestamp(System.currentTimeMillis());

        time2.valueOf(adapter.getItem(1).getValue());
        m.setMtime(time2);
        OvalImageView iv_pic=(OvalImageView)findViewById(R.id.imageView3);
        m.setMicon("null".getBytes());

        m.setMtype(new Integer(adapter.getItem(2).getValue()));
        m.setMuser(new Integer(adapter.getItem(3).getValue()));
        m.setMplace(adapter.getItem(4).getValue());
        m.setMlongtitude(new Double(adapter.getItem(5).getValue()));
        m.setMlatitude(new Double(adapter.getItem(6).getValue()));

        m.setMtitle(adapter.getItem(7).getValue());
        String s="";
        for(int i=8;i<items.size();i++){
            s="<"+adapter.getItem(i).getKey()+">"+adapter.getItem(i).getValue();

        }
        m.setMt1(s);
        return m;
    }
    public Xingji getXingJi(){
        Xingji xing=new Xingji();
        xing.setXmarker(new Integer(adapter.getItem(0).getValue()));
        StarBarView mStarbar = (StarBarView) findViewById(R.id.sbv_starbar);
        xing.setXfenzhi(new Float(mStarbar.getStarRating()).intValue());
        xing.setXuser(MyUser.getMe().getUid());
        return xing;
    }
    private void sendRequestWithkHttpURLConnectionwithXingJi(final int mid){//修改用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever +":8080/AndroidSever/FenZhiServlet?mode=a";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectxingji?mode=a&mid="+mid;
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
    private void sendRequestWithkHttpURLConnection(final Markdown m){//修改用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever +":8080/AndroidSever/MarkdownServlet?mode=c&"+m.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectmarkdown";
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
                    Log.d("info1111",new String(response));

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
    //删除
    private void sendRequestWithkHttpURLConnection3(final Markdown m){//删除用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+"8080/AndroidSever/MarkdownServlet?mode=d&"+m.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectmarkdown";
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
                    Log.d("info1111",new String(response));

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
    private void sendRequestWithkHttpURLConnection2(final int id){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/MarkdownServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectmarkdown?mode=a&mid="+id;
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
                Type type=new TypeToken<LinkedList<Markdown>>(){}.getType();
                List<Markdown> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    m=list.get(i);
                    items=new LinkedList<Item>();
                    items.add(new Item("id",""+m.getMid(),0));
                    items.add(new Item("时间",""+m.getMtime(),1));
                    items.add(new Item("类型",""+m.getMtype(),2));
                    items.add(new Item("用户",""+m.getMuser(),3));
                    items.add(new Item("地点",""+m.getMplace(),4));
                    items.add(new Item("经度",""+m.getMlatitude(),5));
                    items.add(new Item("纬度",""+m.getMlongtitude(),6));
                    items.add(new Item("名称",""+m.getMtitle(),7));
                    StringTokenizer st=new StringTokenizer(m.getMt1(),"<>");

                    int k=7;
                    for(;st.hasMoreTokens();)
                    {
                        if(st.countTokens()>=2) {
                            String key = st.nextToken();
                            System.out.println(key);
                            String value = st.nextToken();
                            k += 1;
                            items.add(new Item(key, "" + value, k));
                        }

                    }
                    OvalImageView iv_pic=(OvalImageView)findViewById(R.id.imageView3);
                    if(m.getMicon()!=null)
                    {
                        try {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(m.getMicon(), 0, m.getMicon().length);
                            iv_pic.setImageBitmap(bitmap);
                            iv_pic.setStrokeWidth(10);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    adapter=new MarkdownItemAdapter(UpdateActivity.this,R.layout.insert_update_markdown_layout,items);
                    adapter.setItems(items);
                    ListView lv=(ListView)findViewById(R.id.lv_2);
                    lv.setAdapter(adapter);
                    System.out.println(m.toString());
                }

            }
        });

    }
    private void showResponse2(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gs=new Gson();
                //解析泛型
                Type type=new TypeToken<LinkedList<Xingji>>(){}.getType();
                List<Xingji> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
               Xingji x=list.get(i);
               UpdateActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                      int fen=x.getXfenzhi();
                       StarBarView mStarbar = (StarBarView) findViewById(R.id.sbv_starbar);
                       mStarbar.setStarRating(fen);
                   }
               });
                }

            }
        });

    }
}
