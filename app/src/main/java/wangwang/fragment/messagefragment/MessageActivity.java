package wangwang.fragment.messagefragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.tox.antox.R;

/**
 * Created by kihao on 16/10/21.
 */
public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    MsgRecordFragment msgRecordFragment;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.ancivity_message);
        ButterKnife.bind(this);
        if (fragmentContainer != null){
            if (savedInstanceState != null){
                return;
            }
            msgRecordFragment = new MsgRecordFragment();

            msgRecordFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer , msgRecordFragment).commit();
        }
    }
}
