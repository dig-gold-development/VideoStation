package com.site.vs.videostation.kit.conversationlist.notification.viewholder;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.annotation.StatusNotificationType;
import com.site.vs.videostation.kit.conversationlist.notification.PCOnlineNotification;
import com.site.vs.videostation.kit.conversationlist.notification.StatusNotification;
import com.site.vs.videostation.R;

@LayoutRes(resId = R.layout.conversationlist_item_notification_pc_online)
@StatusNotificationType(PCOnlineNotification.class)
public class PCOlineNotificationViewHolder extends StatusNotificationViewHolder {
    public PCOlineNotificationViewHolder(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBind(View view, StatusNotification notification) {

    }
}
