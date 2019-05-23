package com.site.vs.videostation.kit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.site.vs.videostation.kit.conversationlist.notification.StatusNotification;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StatusNotificationType {
    Class<? extends StatusNotification> value();
}
