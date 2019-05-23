package com.site.vs.videostation.kit.conversationlist.notification;

import java.util.HashMap;
import java.util.Map;

import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.annotation.StatusNotificationType;
import com.site.vs.videostation.kit.conversationlist.notification.viewholder.ConnectionNotificationViewHolder;
import com.site.vs.videostation.kit.conversationlist.notification.viewholder.PCOlineNotificationViewHolder;
import com.site.vs.videostation.kit.conversationlist.notification.viewholder.StatusNotificationViewHolder;

public class StatusNotificationManager {
    private static StatusNotificationManager instance;
    private Map<Class<? extends StatusNotification>, Class<? extends StatusNotificationViewHolder>> notificationViewHolders;

    public synchronized static StatusNotificationManager getInstance() {
        if (instance == null) {
            instance = new StatusNotificationManager();
        }
        return instance;
    }

    private StatusNotificationManager() {
        init();
    }

    private void init() {
        notificationViewHolders = new HashMap<>();
        registerNotificationViewHolder(PCOlineNotificationViewHolder.class);
        registerNotificationViewHolder(ConnectionNotificationViewHolder.class);
    }

    public void registerNotificationViewHolder(Class<? extends StatusNotificationViewHolder> holderClass) {
        StatusNotificationType notificationType = holderClass.getAnnotation(StatusNotificationType.class);
        LayoutRes layoutRes = holderClass.getAnnotation(LayoutRes.class);
        if (notificationType == null || layoutRes == null) {
            throw new IllegalArgumentException("missing annotation");
        }
        notificationViewHolders.put(notificationType.value(), holderClass);
    }

    public Class<? extends StatusNotificationViewHolder> getNotificationViewHolder(StatusNotification notification) {
        return notificationViewHolders.get(notification.getClass());
    }
}
