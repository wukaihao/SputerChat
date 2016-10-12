package wangwang.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chat.tox.antox.R;
import chat.tox.antox.activities.PublishActivity;
import chat.tox.antox.adapter.FriendAdapter;
import chat.tox.antox.applaction.MyApplaction;
import chat.tox.antox.entity.Friendster;
import chat.tox.antox.wrapper.FriendInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    //private WebView webView;
    private DropDownListView dropDownListView;
    private ImageView header_imageView2;
    private List<Friendster> myData;
    private FriendAdapter friendAdapter;
    private SharedPreferences pref;
    private TextView textView;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.header, container, false);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
//        if(result.equals("ok")){
//            Friend();
//            friendAdapter.notifyDataSetChanged();
//        }
//    }

/*lby 2016/8/30  上面布局改成了header
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        dropDownListView=(DropDownListView)view.findViewById(R.id.list_view);
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.header, null);
        dropDownListView.addHeaderView(v);

        textView=(TextView)v.findViewById(R.id.textView1);

        header_imageView2=(ImageView)v.findViewById(R.id.header_imageView2);
        header_imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });
        pref= PreferenceManager.getDefaultSharedPreferences(getContext());
        String avatar1=pref.getString("avatar", "");
        String nickname=pref.getString("nickname", "");
        textView.setText(nickname);
        Log.d(avatar1,avatar1);
        File avatar = new File(avatar1);
        if (avatar.exists()) {
            header_imageView2.setImageURI(Uri.fromFile(avatar));
        } else {
            header_imageView2.setImageResource(R.drawable.ic_action_contact);
        }
        myData=new ArrayList<>();
        Friend();
        friendAdapter=new FriendAdapter(getContext(),myData);
        dropDownListView.setAdapter(friendAdapter);
        //允许使用底部样式
        dropDownListView.setOnBottomStyle(false);

        dropDownListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                SystemClock.sleep(2000);
                Friend();
                friendAdapter.notifyDataSetChanged();
                dropDownListView.onDropDownComplete();
            }
        });

//        // set on bottom listener
//        dropDownListView.setOnBottomListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
    public void Friend(){
        String url="http://icoral.cn:1199/twitter/?toxid=";
        List<FriendInfo> list= MyApplaction.getIntance().getFriendInfoList();
        for(int i=0;i<list.size();i++){
            String b=list.get(i).key().toString();
            url=url+b+",";
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userkey=pref.getString("tox_id", "");
        userkey=userkey.substring(0,64);
        String url1=url+userkey;
        //创建okHttpClient对象
        Log.d("url1=====",url1);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url1)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(getContext(),"网络异常....",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(final Response response) throws IOException
            {
                String s=response.body().string();
                Log.d("s=====",s);
                if(s.equals("[]")){
//                    Toast.makeText(getContext(),"没有任何数据", Toast.LENGTH_SHORT).show();
                }else {
                    Gson gson = new Gson();
                    ArrayList<Friendster> data = gson.fromJson(s, new TypeToken<ArrayList<Friendster>>() {
                    }.getType());
                    if (data != null && data.size() > 0) {
                        myData.clear();
                        myData.addAll(data);
                    }
                }
            }
        });
    }
    private class GetDataTask extends AsyncTask<Void, Void, List> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown){
            this.isDropDown = isDropDown;
        }

        @Override
        protected List doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            return myData;
        }

        @Override
        protected void onPostExecute(List result) {

            if (isDropDown) {
                Friend();
                friendAdapter.notifyDataSetChanged();

            } else {
//                listItems.add("Added after on bottom");
//                adapter.notifyDataSetChanged();
//
//                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
//                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
*/
}
