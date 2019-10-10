package bob.eve.walle.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
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
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

import bob.eve.walle.Adapter.MarkdownItemAdapter;
import bob.eve.walle.R;
import bob.eve.walle.pojo.Item;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.pojo.Ziduan;
import bob.eve.walle.util.MyUser;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class insertActivity extends AppCompatActivity implements View.OnClickListener {
    public LinkedList<Item> items=new LinkedList<Item>();
    public LinkedList<Item> items2=new LinkedList<Item>();
    MarkdownItemAdapter adapter=null;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button takePhoto;
    private ImageView picture;
    private Uri imageUri;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    int id=1;
    int mstate;
    double mlongtitude;
    double mlatitude;
    Bitmap bitmap;
    File outputImage;
    String myplace;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        //在这里决定你想要什么参数
        //items的使用：key：参数的名字
        //value：参数的默认值，最好写上“0”
        //items列表：参数列表
        mstate=getIntent().getIntExtra("mstate",1);
        sendRequestWithkHttpURLConnection(mstate);
        verifyStoragePermissions(this);
        initPhotoError();
        picture=(ImageView)findViewById(R.id.imageView2);
        picture.setOnClickListener(this);

        mlongtitude=getIntent().getDoubleExtra("mlongtitude",117.39);
        mlatitude=getIntent().getDoubleExtra("mlatitude",39.112);
        myplace=getIntent().getStringExtra("myplace");

    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save away the original text, so we still have it if the activity needs to be killed while paused.
        savedInstanceState.putInt("id", id);

        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttoninsert){
            Log.d("info3333","3333");
            sendRequestWithkHttpURLConnection(getMarkdown());
        }
        else if(v.getId()==R.id.button_goupdate){
            id=1;
          //  Intent i=new Intent(insertActivity.this,UpdateActivity.class);
            //i.putExtra("id",10);
            //startActivity(i);
        }
        else if(v.getId()==R.id.imageView2)
        {
            outputImage = new File(Environment.getExternalStorageDirectory(),
                    +System.currentTimeMillis()+ ".jpg");
            try {
                if (outputImage.exists()) {

                }else {
                    outputImage.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);

        }

    }
    //在这里获得参数
    public Markdown getMarkdown(){
        ListView lv=(ListView)findViewById(R.id.listview);
        items2=adapter.getItems();

//        for(int j=1;j<items.size();j++){
//            Log.d("lll",adapter.getItem(j).getValue());
//
//        }
        Markdown m=new Markdown();
        m.setMid(1);
        Timestamp time2= new Timestamp(System.currentTimeMillis());

        time2.valueOf(adapter.getItem(1).getValue());
        m.setMtime(time2);

        m.setMtype(new Integer(adapter.getItem(2).getValue()));
m.setMuser(MyUser.getMe().getUid());
     //   m.setMuser(new Integer(adapter.getItem(3).getValue()));
        m.setMplace(adapter.getItem(4).getValue());
        m.setMlongtitude(mlongtitude);
        //m.setMlongtitude(new Double(adapter.getItem(5).getValue()));
        m.setMlatitude(mlatitude);
        //m.setMlatitude(new Double(adapter.getItem(6).getValue()));
//        System.out.println("m=1111111"+m.toString());
        picture.setDrawingCacheEnabled(true);
        // Bitmap bitmap = Bitmap.createBitmap(picture.getDrawingCache());
//        Bitmap newbitmap=scaleMatrix(bitmap,40,40);
   //     BASE64Encoder encoder=new BASE64Encoder();

        //picture.setDrawingCacheEnabled(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //  newbitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
      //  String imagestring=encoder.encode(baos.toByteArray());
        m.setMicon("123456".getBytes());
        System.out.println(m.getMicon().toString());

        m.setMtitle(adapter.getItem(5).getValue());
        m.setMt1(adapter.getItem(6).getValue());
        String s=new String();
        for(int i=9;i<items.size();i++){
            s="<"+adapter.getItem(i).getKey()+">"+adapter.getItem(i).getValue();

        }
        m.setMt1(s);
        return m;
    }
    //可以吧这个类型换成自己的。（比方说我是Markdown，您是User，pinglun。。。。）
    private void sendRequestWithkHttpURLConnection(final Markdown m){//新增用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/MarkdownServlet?mode=a&"+m.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectmarkdown";
                    URL url=new URL(s);//构造网页地址

                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                    multipartBuilder.setType(MultipartBody.FORM)
                            .addFormDataPart("img", "temp.jpg",    //第一个参数是标识  服务器端通过该标识拿到照片
                                    RequestBody.create(MediaType.parse("image/jpg"), outputImage));
                    RequestBody requestBody = multipartBuilder.build();
                    Request request = new Request.Builder()
                            .url(url)     //服务器URL
                            .post(requestBody)
                            .build();
                    Call call = new OkHttpClient().newCall(request);
                    call.execute();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                    insertActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(insertActivity.this,"请先拍摄一张图片",Toast.LENGTH_LONG).show();
                        }
                    });

                }finally{

                }
            }
        }).start();//令线程在jvm的就绪队列中等候执行。
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"trace.txt"); //trace.txt �����ֻ���Ŀ¼�µ��ļ�
                    //�ļ��ϴ�
                    OkHttpUtils.post()


                            .addFile("file", "1.jpg", outputImage)  //����addFile���Զ��ļ��ϴ�
                            .url("http://"+MyUser.my_sever+":8080/") //�����Լ���ip��ַ
                            .build()
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                intentToMap();
            }
        });

    }
    private void sendRequestWithkHttpURLConnection(final int user){//全部查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/MarkdownServlet";
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectziduan?mode=b&zstate="+user;
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
    public void intentToMap(){
        Intent i=new Intent(insertActivity.this, MainActivity.class);
        MyUser.setB(true);
        i.putExtra("info",true);
        startActivity(i);
        ;

    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {//注：这个县城用于修改TextView的信息、跳转等凡是跟UI油管的操作
                Ziduan z=null;
                Gson gs=new Gson();
                //解析泛型
                items=new LinkedList<Item>();
                //这里的类型（凡是有Markdown的地方）也可以换成自己的。
               // items.add(new Item("id","1",0,0));
                items.add(new Item("时间",new Timestamp(System.currentTimeMillis()).toString(),0,3));
                items.add(new Item("类型",""+mstate,1,0));
               // items.add(new Item("用户",""+ MyUser.getMe().getUid(),3,0));
                items.add(new Item("地点",myplace,2,1));
              //  items.add(new Item("经度",""+mlongtitude,5,4));
            //    items.add(new Item("纬度",""+mlatitude,6,4));
                items.add(new Item("名称","11111",3,1));
                items.add(new Item("其他信息","1",4,1));
                Type type=new TypeToken<LinkedList<Ziduan>>(){}.getType();
                List<Ziduan> list=new Gson().fromJson(response,type);
                for(int i=0;i<list.size();i++){
                    z=list.get(i);
                    items.add(new Item(z.getZname(),MyUser.getMe().getUnicheng(),4+i,new Integer(z.getZt1())));

                    System.out.println(z.toString());
                }
                adapter=new MarkdownItemAdapter(insertActivity.this,R.layout.insert_update_markdown_layout,items);
                adapter.setItems(items);
                ListView lv=(ListView)findViewById(R.id.listview);
                lv.setAdapter(adapter);
                Button insert=(Button)findViewById(R.id.buttoninsert);
                insert.setOnClickListener(insertActivity.this);
                Button update=(Button)findViewById(R.id.button_goupdate);
                update.setOnClickListener(insertActivity.this);


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }






    }
    public static void verifyStoragePermissions(final Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    public static Bitmap scaleMatrix(Bitmap bitmap, int width, int height){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scaleW = 0.2F;
        float scaleH = 0.2F;
        System.out.println("w"+w+"h"+h+"scaleW"+scaleW+"scaleH"+scaleH+","+width+","+height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH); // 长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
