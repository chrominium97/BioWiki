package org.wordpress.biowiki.mocks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import org.mockito.stubbing.Answer;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.AppLog.T;
import kr.kdev.dg1s.biowiki.util.SystemServiceFactoryAbstract;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class SystemServiceFactoryTest implements SystemServiceFactoryAbstract {
    public static Answer sNotificationCallback;

    public Object get(Context context, String name) {
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
        if (Context.NOTIFICATION_SERVICE.equals(name)) {
            NotificationManager notificationManager = mock(NotificationManager.class);
            if (sNotificationCallback != null) {
                doAnswer(sNotificationCallback).when(notificationManager).notify(anyInt(), any(Notification.class));
                doAnswer(sNotificationCallback).when(notificationManager).cancel(anyInt());
            }
            return notificationManager;
        } else {
            AppLog.e(T.TESTS, "SystemService:" + name + "No supported in SystemServiceFactoryTest");
        }
        return null;
    }


}