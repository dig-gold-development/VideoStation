package com.site.vs.videostation.kit.conversation;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.site.vs.videostation.kit.WfcBaseActivity;
import com.site.vs.videostation.R;
import cn.wildfirechat.model.ConversationInfo;

public class ConversationInfoActivity extends WfcBaseActivity {

    private ConversationInfo conversationInfo;

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected void afterViews() {
        conversationInfo = getIntent().getParcelableExtra("conversationInfo");
        Fragment fragment = null;
        switch (conversationInfo.conversation.type) {
            case Single:
                fragment = SingleConversationInfoFragment.newInstance(conversationInfo);
                break;
            case Group:
                fragment = GroupConversationInfoFragment.newInstance(conversationInfo);
                break;
            case ChatRoom:
                // TODO
                break;
            case Channel:
                fragment = ChannelConversationInfoFragment.newInstance(conversationInfo);
                break;
            default:
                break;
        }
        if (fragment == null) {
            Toast.makeText(this, "todo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
