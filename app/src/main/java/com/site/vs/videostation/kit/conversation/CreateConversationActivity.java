package com.site.vs.videostation.kit.conversation;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import com.site.vs.videostation.kit.contact.model.UIUserInfo;
import com.site.vs.videostation.kit.contact.pick.PickConversationTargetActivity;
import com.site.vs.videostation.kit.group.GroupViewModel;
import com.site.vs.videostation.kit.third.utils.UIUtils;
import com.site.vs.videostation.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.GroupInfo;

public class CreateConversationActivity extends PickConversationTargetActivity {
    private GroupViewModel groupViewModel;

    @Override
    protected void afterViews() {
        super.afterViews();
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onContactPicked(List<UIUserInfo> newlyCheckedUserInfos) {
        if (newlyCheckedUserInfos.size() == 1) {

            Intent intent = new Intent(this, ConversationActivity.class);
            Conversation conversation = new Conversation(Conversation.ConversationType.Single, newlyCheckedUserInfos.get(0).getUserInfo().uid);
            intent.putExtra("conversation", conversation);
            startActivity(intent);
            finish();
        } else {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .content("创建中...")
                    .progress(true, 100)
                    .build();
            dialog.show();

            groupViewModel.createGroup(this, newlyCheckedUserInfos).observe(this, result -> {
                dialog.dismiss();
                if (result.isSuccess()) {
                    UIUtils.showToast(UIUtils.getString(R.string.create_group_success));
                    Intent intent = new Intent(CreateConversationActivity.this, ConversationActivity.class);
                    Conversation conversation = new Conversation(Conversation.ConversationType.Group, result.getResult(), 0);
                    intent.putExtra("conversation", conversation);
                    startActivity(intent);
                } else {
                    UIUtils.showToast(UIUtils.getString(R.string.create_group_fail));
                }
                finish();
            });
        }

    }

    @Override
    public void onGroupPicked(List<GroupInfo> groupInfos) {
        Intent intent = new Intent(this, ConversationActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Group, groupInfos.get(0).target);
        intent.putExtra("conversation", conversation);
        startActivity(intent);
        finish();
    }
}
