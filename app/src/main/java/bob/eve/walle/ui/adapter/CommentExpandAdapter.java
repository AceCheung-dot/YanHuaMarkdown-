package bob.eve.walle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bob.eve.walle.R;
import bob.eve.walle.bean.CommentDetailBean;
import bob.eve.walle.bean.ReplyDetailBean;
import bob.eve.walle.pojo.Pinglun;
import bob.eve.walle.util.MyUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: Moos
 * E-mail: moosphon@gmail.com
 * Date:  18/4/20.
 * Desc: 评论与回复列表的适配器
 */

public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentDetailBean> commentBeanList;
    private List<Pinglun> pings;


    public List<Pinglun> getPings() {
        return pings;
    }

    public void setPings(List<Pinglun> pings) {
        this.pings = pings;
    }

    private List<ReplyDetailBean> replyBeanList;
    public static final char dianzan_pinglun='b';
    public static final char quxiao_pinglun='c';
    private Context context;
    private int pageIndex = 1;

    public CommentExpandAdapter(Context context, List<CommentDetailBean> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    @Override
    public int getGroupCount() {
        return commentBeanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(commentBeanList.get(i).getReplyList() == null){
            return 0;
        }else {
            return commentBeanList.get(i).getReplyList().size()>0 ? commentBeanList.get(i).getReplyList().size():0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return commentBeanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentBeanList.get(i).getReplyList().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Glide.with(context).load(R.drawable.user_other)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);
        groupHolder.tv_name.setText(commentBeanList.get(groupPosition).getNickName());
        groupHolder.tv_time.setText(commentBeanList.get(groupPosition).getCreateDate());
        groupHolder.tv_content.setText(commentBeanList.get(groupPosition).getContent());
        groupHolder.tv_likeperson.setText(""+commentBeanList.get(groupPosition).getDianzan());

        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  int count=commentBeanList.get(groupPosition).getCountDianzan();
                if(count!=0&&count%5==0){
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#aaaaaa"));

                    commentBeanList.get(groupPosition).setDianzan( commentBeanList.get(groupPosition).getDianzan()-4);
                    groupHolder.tv_likeperson.setText(""+(commentBeanList.get(groupPosition).getDianzan()));
                    Pinglun ping=pings.get(groupPosition);
                        ping.setPicon("null".getBytes());
                    ping.setPdianzan(commentBeanList.get(groupPosition).getDianzan());
                  sendRequestWithkHttpURLConnectionReply(ping,quxiao_pinglun);


                }else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                    commentBeanList.get(groupPosition).setDianzan( commentBeanList.get(groupPosition).getDianzan()+1);
                    groupHolder.tv_likeperson.setText(""+(commentBeanList.get(groupPosition).getDianzan()));

                    Pinglun ping=pings.get(groupPosition);
                    ping.setPicon("null".getBytes());
                    ping.setPdianzan(commentBeanList.get(groupPosition).getDianzan());
                    sendRequestWithkHttpURLConnectionReply(ping,dianzan_pinglun);
                }
                commentBeanList.get(groupPosition).setCountDianzan(commentBeanList.get(groupPosition).getCountDianzan()+1);
            }
        });


        return convertView;
    }
    private void sendRequestWithkHttpURLConnectionReply(final Pinglun ping, final char mode){//按id查询用
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {

                    String s="http://"+ MyUser.my_sever+":8080/AndroidSever/PinglunServlet?mode="+mode+ping.toString();
                    System.out.println(s);
                    String s2="http://"+MyUser.my_sever+":8080/AndroidSever/selectpinglun?mode=a&pmarkdown=537";
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




                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (Exception e) {
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
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        String replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }else {
            childHolder.tv_name.setText("无名"+":");
        }

        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time,tv_likeperson;
        private ImageView iv_like;
        public GroupHolder(View view) {
            logo = (CircleImageView) view.findViewById(R.id.comment_item_logo);
            tv_content = (TextView) view.findViewById(R.id.comment_item_content);
            tv_name = (TextView) view.findViewById(R.id.comment_item_userName);
            tv_time = (TextView) view.findViewById(R.id.comment_item_time);
            tv_likeperson = (TextView) view.findViewById(R.id.comment_item_like_person);
            iv_like = (ImageView) view.findViewById(R.id.comment_item_like);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }


    /**
     * by moos on 2018/04/20
     * func:评论成功后插入一条数据
     * @param commentDetailBean 新的评论数据
     */
    public void addTheCommentData(CommentDetailBean commentDetailBean){
        if(commentDetailBean!=null){

            commentBeanList.add(commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * by moos on 2018/04/20
     * func:回复成功后插入一条数据
     * @param replyDetailBean 新的回复数据
     */
    public void addTheReplyData(ReplyDetailBean replyDetailBean, int groupPosition){
        if(replyDetailBean!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetailBean.toString() );
            if(commentBeanList.get(groupPosition).getReplyList() != null ){
                commentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
            }else {
                List<ReplyDetailBean> replyList = new ArrayList<>();
                replyList.add(replyDetailBean);
                commentBeanList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * by moos on 2018/04/20
     * func:添加和展示所有回复
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
        if(commentBeanList.get(groupPosition).getReplyList() != null ){
            commentBeanList.get(groupPosition).getReplyList().clear();
            commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
        }else {

            commentBeanList.get(groupPosition).setReplyList(replyBeanList);
        }

        notifyDataSetChanged();
    }

}
