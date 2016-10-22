package wangwang.fragment.messagefragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import chat.tox.antox.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kihao on 16/10/21.
 */
public class CommonFriendRecyclerAdapter extends RecyclerView.Adapter{

    public RoomRecylerViewHolder roomRecylerViewHolder;
    List<HashMap<String,Object>> listReccler;

    public void initData(List<HashMap<String, Object>> list) {
        listReccler = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        roomRecylerViewHolder =
                new RoomRecylerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler ,parent ,false));
        return roomRecylerViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        roomRecylerViewHolder.setData(listReccler.get(position));
    }

    @Override
    public int getItemCount() {
        return listReccler.size();
    }

    private class RoomRecylerViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView head;
        public TextView greenDot;
        public TextView name;

        public RoomRecylerViewHolder(View layout) {
            super(layout);
            head = (CircleImageView) layout.findViewById(R.id.avatar);
            greenDot = (TextView) layout.findViewById(R.id.icon_recycler);
            name = (TextView) layout.findViewById(R.id.tv_recycler_name);
        }


        public void setData(HashMap<String, Object> mapRecycler) {
            if (mapRecycler == null)return;
            name.setText(mapRecycler.get("name") + "");
        }
    }

    
}
