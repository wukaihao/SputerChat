package wangwang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import wangwang.activity.FirstActivity;
import wangwang.fragment.ContactsFragment;
import wangwang.fragment.FindFragment;
import wangwang.fragment.FriendsFragment;
import wangwang.fragment.MessageFragment;
import wangwang.fragment.UserFragment;
import wangwang.fragment.messagefragment.MsgRecordFragment;


/**
 * Created by Administrator on 2015/10/12.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 5;
    private MsgRecordFragment myFragment1 = null;
    private ContactsFragment myFragment2 = null;
    private FriendsFragment myFragment3=null;
    private FindFragment myFragment4 = null;
    private UserFragment myFragment5 = null;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new MsgRecordFragment();
        myFragment2 = new ContactsFragment();
        myFragment3 = new FriendsFragment();
        myFragment4 = new FindFragment();
        myFragment5 = new UserFragment();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return fragementList.get(position).getName();//````
//    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case FirstActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case FirstActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case FirstActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
            case FirstActivity.PAGE_FOUR:
                fragment = myFragment4;
                break;
            case FirstActivity.PAGE_FIVE:
                fragment = myFragment5;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
