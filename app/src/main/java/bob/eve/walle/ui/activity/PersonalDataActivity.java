package bob.eve.walle.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hjq.base.BaseDialog;
import com.hjq.dialog.AddressDialog;
import com.hjq.dialog.DateDialog;
import com.hjq.dialog.InputDialog;
import com.hjq.dialog.MenuDialog;
import com.hjq.dialog.MessageDialog;
import com.hjq.umeng.Platform;
import com.hjq.umeng.UmengShare;
import com.hjq.widget.SettingBar;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.pojo.Gexing;
import bob.eve.walle.pojo.Markdown;
import bob.eve.walle.pojo.User;
import bob.eve.walle.ui.ShareDialog;
import bob.eve.walle.util.MyUser;
import bob.eve.walle.util.getPahtUtil;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static bob.eve.walle.ui.activity.insertActivity.CROP_PHOTO;
import static bob.eve.walle.ui.activity.insertActivity.TAKE_PHOTO;

public final class PersonalDataActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.iv_person_data_head)
    ImageView mHeadView;
    @BindView(R.id.sb_person_data_zhiye)
    SettingBar mZhiyeView;
    @BindView(R.id.sb_person_data_birth)
    SettingBar mBirthView;
    @BindView(R.id.sb_person_data_address)
    SettingBar mAddressView;
    @BindView(R.id.sb_person_data_phone)
    SettingBar mPhoneView;
    @BindView(R.id.sb_person_data_sex)
    SettingBar mSexView;
    private static final int TAKE_PHOTO_RC = 2;
    private static final int CHOOSE_PHOTO = 3;
    boolean hasImage=false;


    private static final int RC_CEMERA_AND_LOCATION = 1;
    private static final String VERA_FILEPROVIDER = "bob.eve.walle.fileprovider";
    //    照片所在的uri地址
    private Uri mImageUri;
    private Uri finalUri;
    private String imagepath;
    File file;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_personal_data_title;
    }

    @Override
    protected void initView() {
        initPhotoError();
        String[] perms={
                Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (EasyPermissions.hasPermissions(this,perms)){
            init();
        }else{
            EasyPermissions.requestPermissions(this,getString(R.string.library_permissionsdispatcher_libraryWebsite),RC_CEMERA_AND_LOCATION,perms);
        }


    }
    private void init() {
        initdata();
    }


    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_person_data_head, R.id.sb_person_data_birth,R.id.sb_person_data_sex,R.id.sb_person_data_zhiye ,R.id.sb_person_data_address, R.id.sb_person_data_phone,R.id.commit2,R.id.share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_data_head:
               new MessageDialog.Builder(this)
                        .setTitle("图片") // 标题可以不用填写
                        .setMessage("请选择图片来源")
                        .setConfirm("拍照")
                        .setCancel("相册") // 设置 null 表示不显示取消按钮
                      //  .setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(Dialog dialog) {
                                takePhoto();
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                choosePhoto();
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_sex:
                new MessageDialog.Builder(this)
                        .setTitle("设置性别") // 标题可以不用填写
                      //  .setContent(mSexView.getRightText())
                      //  .setHint(getResources().getString(R.string.personal_data_name_hint))
                        .setMessage(" ")
                        .setConfirm("男")
                        .setCancel("女") // 设置 null 表示不显示取消按钮
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(Dialog dialog) {

                                    mSexView.setRightText("M");

                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                mSexView.setRightText("F");
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_birth:
                new DateDialog.Builder(this)
                        .setTitle("请选择日期")

                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(Dialog dialog, int year, int month, int day) {
                                mBirthView.setRightText(year + "年" + month + "月" + day + "日");
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_address:
                new AddressDialog.Builder(this)
                        .setTitle("选择地区")
                       // .setProvince("天津市") // 设置默认省份
                       // 设置默认城市（必须要先设置默认省份）
                        //.setIgnoreArea()
                       // .setIgnoreArea() // 不选择县级区域
                        .setListener(new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(Dialog dialog, String province, String city, String area) {
                                if (!mAddressView.getRightText().equals(province + city)) {
                                    mAddressView.setRightText(province + city);
                                }
                            }

                            @Override
                            public void onCancel(Dialog dialog) {}
                        })
                        .show();
                break;
            case R.id.sb_person_data_zhiye:
                 List<String> data = new ArrayList<>();
               data.add("学生");
                data.add("教师");
                data.add("教工");
                data.add("其他");
                new MenuDialog.Builder(this)
                        .setCancel("取消") // 设置 null 表示不显示取消按钮
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setList(data)
                        .setListener(new MenuDialog.OnListener() {

                            @Override
                            public void onSelected(Dialog dialog, int position, String text) {
                                mZhiyeView.setRightText( text);
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                toast("取消了");
                            }
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
                        .show();
                break;
            case R.id.sb_person_data_phone:
                // 先判断有没有设置过手机号
                if (true) {
                    startActivity(PhoneVerifyActivity.class);
                } else {
                   // startActivity(PhoneResetActivity.class);
                }
                break;
            default:
                break;
            case R.id.share:
                new ShareDialog.Builder(this)
                        .setShareTitle("分享标题")
                        .setShareDescription("分享描述")
                        .setShareUrl("https://github.com/getActivity/AndroidProject")
                        .setShareLogo("https://www.baidu.com/img/bd_logo1.png")
                        .setListener(new UmengShare.OnShareListener() {

                            @Override
                            public void onSucceed(Platform platform) {
                                toast("分享成功");
                            }

                            @Override
                            public void onError(Platform platform, Throwable t) {
                                toast("分享出错");
                            }

                            @Override
                            public void onCancel(Platform platform) {
                                toast("分享取消");
                            }
                        })
                        .show();
                break;
            case R.id.commit2:
                setdata();
                    break;
        }
    }
    private void takePhoto() {
//        创建file对象，用于存储拍照后的对象
        File f = new File(Environment.getExternalStorageDirectory(),
                +System.currentTimeMillis()+ ".jpg");
        try {
            if (f.exists()) {

            }else {
               f.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageUri = Uri.fromFile(f);
        file=f;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
        System.out.println(mImageUri.toString());

//        启动相机程序
   //      Intent intent = new Intent("com.android.camera.action.CROP");
        //                    intent.setDataAndType(imageUri, "image/*");
        //                    intent.putExtra("scale", true);
        //                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                          }
    private void choosePhoto() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try {
                        Intent intent = new Intent("com.android.camera.action.CROP");
                                           intent.setDataAndType(mImageUri, "image/*");
                                            intent.putExtra("scale", true);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                            startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap  bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(mImageUri));
                        this.finalUri=mImageUri;
                        mHeadView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                handleImageOn19(data);
                this.finalUri=data.getData();
                    break;

        }
    }

    private void handleImageBefore10(Intent intent) {
        Uri uri=intent.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOn19(Intent intent) {
        String imagePath=null;
        Uri uri=intent.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
//            如果是document类型的uri，通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if (uri.getAuthority().equals("com.android.providers.media.documents")){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if (uri.getAuthority().equals("com.android.providers.downloads.documents")){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
//            如果是content类型的uri，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
//            如果是file类型的uri，直接获取图片路径
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath!=null){
            file=new File(imagePath);
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            mHeadView.setImageBitmap(bitmap);
          this.imagepath=imagePath;
        }else{
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }
    public static void verifyStoragePermissions(final Activity activity) {
        // Check if we have write permission
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            String[]s={ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions( activity,s, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path=null;
//        通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    void setdata(){
        EditText et_nicheng=(EditText)findViewById(R.id.eTnicheng);
        String nicheng=et_nicheng.getText().toString();
        String sex=mSexView.getRightText().toString();
        String birth=mBirthView.getRightText().toString();
        String area=mAddressView.getRightText().toString();
        String zhiye=mZhiyeView.getRightText().toString();
        User user= MyUser.getMe();
        user.setUnicheng(nicheng);
        Gexing gexing=MyUser.getMygexing();
        gexing.setGuser(user.getUid());
        //gexing.setGicon(BitmapFactory.decodeFile(f));
        gexing.setGsex(sex);
        gexing.setGt1(birth);
        gexing.setGt2(area);
        gexing.setGt3(zhiye);
        gexing.setGzhuti(1);

        if(MyUser.isIsInsert()&&this.hasImage){
            //有图新增
            insertGexingUsingImage(gexing);



        }
      else  if(!(MyUser.isIsInsert())&&this.hasImage){
            //有图修改
            updateGexingUsingImage(gexing);

        }
        else  if((MyUser.isIsInsert())&&!(this.hasImage)){
            //无图新增
            insertGexingWithoutImage(gexing);


        }
        else{
            //无图修改
            updateGexingWithoutImage(gexing);
        }
        sendRequestWithkHttpURLConnection2(user);

    }
    void initdata(){

        User user= MyUser.getMe();
        EditText et_nicheng = (EditText) findViewById(R.id.eTnicheng);
        et_nicheng.setText(user.getUnicheng());
        EditText et_id=(EditText)findViewById(R.id.eTuserID);
        et_id.setText(""+user.getUid());

        Gexing gexing=MyUser.getMygexing();
        if(!MyUser.isIsInsert()) {

            mSexView.setRightText(gexing.getGsex());
            mBirthView.setRightText(gexing.getGt1());
            mAddressView.setRightText(gexing.getGt2());
            if(MyUser.getMygexing().getGicon().length!=0&&new String("null").compareTo(new String(MyUser.getMygexing().getGicon()))!=0)
            {
                mHeadView.setImageBitmap(BitmapFactory.decodeByteArray(gexing.getGicon(),0,gexing.getGicon().length));
            }
        }

    }
    private void insertGexingUsingImage(final Gexing g){//新增用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/GexingServlet?mode=a&"+g.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing";
                    URL url=new URL(s);//构造网页地址

                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                    multipartBuilder.setType(MultipartBody.FORM)
                            .addFormDataPart("img", "temp.jpg",    //第一个参数是标识  服务器端通过该标识拿到照片
                                    RequestBody.create(MediaType.parse("image/jpg"),file));
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
                    PersonalDataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonalDataActivity.this,"请先拍摄一张图片",Toast.LENGTH_LONG).show();
                        }
                    });

                }finally{

                }
            }
        }).start();//令线程在jvm的就绪队列中等候执行。



    }
    private void updateGexingUsingImage(final Gexing g){//新增用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/GexingServlet?mode=c&"+g.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing";
                    URL url=new URL(s);//构造网页地址

                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                    multipartBuilder.setType(MultipartBody.FORM)
                            .addFormDataPart("img", "temp.jpg",    //第一个参数是标识  服务器端通过该标识拿到照片
                                    RequestBody.create(MediaType.parse("image/jpg"),file));
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
                    PersonalDataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonalDataActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                }finally{

                }
            }
        }).start();//令线程在jvm的就绪队列中等候执行。



    }
    private void updateGexingWithoutImage(final Gexing g){//删除用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/GexingServlet?mode=c&"+g.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing";
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
    private void insertGexingWithoutImage(final Gexing g){//删除用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+MyUser.my_sever+":8080/AndroidSever/GexingServlet?mode=a&"+g.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectgexing";
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
    private void sendRequestWithkHttpURLConnection2(User u){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/UserServlet?mode=c&"+u.toString();
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectuser?mode=a&uid=";
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
                    //showResponse(response.toString());
                    //sendRequestWithkHttpURLConnection3(MyUser.getMe().getUid());
                    PersonalDataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(PersonalDataActivity.this,MainActivity.class);
                            startActivity(i);
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


};
 class ImgUtil {

    public static void saveImageToGallery(Context context, Bitmap bitmap){
//        首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "vgmap");
        if (!appDir.exists()){
            appDir.mkdirs();
        }
        String filename=System.currentTimeMillis()+".jpg";
        File file=new File(appDir,filename);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),filename,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }




}