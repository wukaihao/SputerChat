package wangwang.fragment;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chat.tox.antox.R;
import chat.tox.antox.activities.Main4Activity;
import chat.tox.antox.activities.UserSettingsActivity;
import wangwang.activity.HelpCenterActivity;
import wangwang.adapter.ListViewAdapter;
import wangwang.entity.ListViewItemModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private ListView listView1;
    private ListViewAdapter adapter;
    private List<ListViewItemModel> listItems;
    private String[] titles = {"账号", "手机" };
    private int[] rids = {R.drawable.find_lv_1, R.drawable.find_lv_2 };

    private ListView listView2;
    private ListViewAdapter adapter2;
    private List<ListViewItemModel> listItems2;
    private String[] titles2 = {"通知", "好友", "照片、视屏和表情", "隐私对话" };
    private int[] rids2 = {R.drawable.find_lv_1, R.drawable.find_lv_2, R.drawable.find_lv_3, R.drawable.find_lv_4 };


    private ListView listView3;
    private ListViewAdapter adapter3;
    private List<ListViewItemModel> listItems3;
    private String[] titles3 = {"切换用户", "报告问题", "帮助", "隐私与条款" };
    private int[] rids3 = {R.drawable.find_lv_1, R.drawable.find_lv_2, R.drawable.find_lv_3, R.drawable.find_lv_4 };


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();

        listView1 = (ListView)view.findViewById(R.id.userFragment_listView1);
        listItems = new ArrayList<ListViewItemModel>();
        for (int i = 0; i < titles.length; i++) {
            ListViewItemModel findListItemModel = new ListViewItemModel();
            findListItemModel.setTitle(titles[i]);
            findListItemModel.setRid(rids[i]);
            listItems.add(findListItemModel);
        }
        adapter = new ListViewAdapter(getActivity());
        adapter.setListItems(listItems);
        listView1.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView1);  //调用该方法设定该ListView的高度
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                switch(position)
                {
                    case 0:
                        Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });


        listView2 = (ListView)view.findViewById(R.id.userFragment_listView2);
        listItems2 = new ArrayList<ListViewItemModel>();
        for (int i = 0; i < titles2.length; i++) {
            ListViewItemModel findListItemModel = new ListViewItemModel();
            findListItemModel.setTitle(titles2[i]);
            findListItemModel.setRid(rids2[i]);
            listItems2.add(findListItemModel);
        }
        adapter2 = new ListViewAdapter(getActivity());
        adapter2.setListItems(listItems2);
        listView2.setAdapter(adapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        setListViewHeightBasedOnChildren(listView2);   //调用该方法设定该ListView的高度



        listView3 = (ListView)view.findViewById(R.id.userFragment_listView3);
        listItems3 = new ArrayList<ListViewItemModel>();
        for (int i = 0; i < titles3.length; i++) {
            ListViewItemModel findListItemModel = new ListViewItemModel();
            findListItemModel.setTitle(titles3[i]);
            findListItemModel.setRid(rids3[i]);
            listItems3.add(findListItemModel);
        }
        adapter3 = new ListViewAdapter(getActivity());
        adapter3.setListItems(listItems3);
        listView3.setAdapter(adapter3);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub

            }
        });
        setListViewHeightBasedOnChildren(listView3);    //调用该方法设定该ListView的高度
    }



    //该方法设定每个ListView总体的高度，否则每个ListView只会显示一行
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListViewAdapter listAdapter = (ListViewAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }




}
