package com.site.vs.videostation.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.site.vs.videostation.third.location.data.LocationData;
import com.site.vs.videostation.third.location.ui.activity.MyLocationActivity;
import com.site.vs.videostation.kit.annotation.ExtContextMenuItem;
import com.site.vs.videostation.kit.conversation.ext.core.ConversationExt;
import com.site.vs.videostation.R;
import cn.wildfirechat.message.TypingMessageContent;
import cn.wildfirechat.model.Conversation;

import static android.app.Activity.RESULT_OK;

public class LocationExt extends ConversationExt {

    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "位置")
    public void pickLocation(View containerView, Conversation conversation) {
        Intent intent = new Intent(context, MyLocationActivity.class);
        startActivityForResult(intent, 100);
        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_LOCATION);
        conversationViewModel.sendMessage(content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            LocationData locationData = (LocationData) data.getSerializableExtra("location");
            conversationViewModel.sendLocationMessage(locationData);
        }
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_func_location;
    }

    @Override
    public String title(Context context) {
        return "位置";
    }
}
