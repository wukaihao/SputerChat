package wangwang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import chat.tox.antox.R;
import wangwang.entity.ListViewItemModel;

/**
 * Created by 24582 on 2016/8/26.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<ListViewItemModel> listItems;
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public List<ListViewItemModel> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListViewItemModel> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
// TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        ListItemView listItemView;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = layoutInflater
                    .inflate(R.layout.fragment_find_item, null);
            listItemView.setTv_title((TextView) convertView
                    .findViewById(R.id.find_item_names));
            listItemView.setIv_image((ImageView) convertView
                    .findViewById(R.id.find_item_images));
            convertView.setTag(listItemView);   //绑定object到view
        } else {
            listItemView = (ListItemView) convertView.getTag();  //取得object从view
        }

        //设置数据
        listItemView.getTv_title()
                .setText(listItems.get(position).getTitle());
        listItemView.getIv_image()
                .setImageResource(listItems.get(position).getRid());

        return convertView;
    }

    class ListItemView {
        private TextView tv_title;
        private ImageView iv_image;

        public TextView getTv_title() {
            return tv_title;
        }

        public void setTv_title(TextView tv_title) {
            this.tv_title = tv_title;
        }

        public ImageView getIv_image() {
            return iv_image;
        }

        public void setIv_image(ImageView iv_image) {
            this.iv_image = iv_image;
        }
    }
}
