package wangwang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chat.tox.antox.R;
import chat.tox.antox.fragments.AddPaneFragment;
import chat.tox.antox.fragments.MainDrawerFragment;
import chat.tox.antox.fragments.RecentFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends RecentFragment {

    public MessageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
