package overdueinvoices.example.com.overdueinvoices;

///////////////////////////////////////////////////////////////////////////////////////////////
//  Copyright (c) 1/2/15 Intuit Inc. All rights reserved. Unauthorized reproduction is a
//  violation of applicable law. This material contains certain confidential and proprietary
//  information and trade secrets of Intuit Inc.
///////////////////////////////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.intuit.intuitwear.exceptions.IntuitWearException;
import com.intuit.intuitwear.notifications.IWearAndroidNotificationSender;
import com.intuit.intuitwear.notifications.IWearNotificationSender;
import com.intuit.intuitwear.notifications.IWearNotificationType;
import com.intuit.mobile.png.sdk.PushNotifications;


public class GCMIntentService extends GCMBaseIntentService {
    /**
     * Constructor for GCMIntentService.  This class sets up the communication with
     * the Push Notification Gateway.
     *
     * PROJECT_NUMBER must be your GCM Project number.  If you do not have a GCM project
     * number, please visit https://console.developers.google.com to create an App Project.
     */
    public GCMIntentService() {
        super(OverdueInvoicesActivity.PROJECT_NUMBER);
    }

    /**
     * This callback method is invoked after a successful registration with GCM.
     * Here we are passing the new registrationId to the PNG SDK. The SDK will
     * send the registrationId along with any user and userGroup mappings to the
     * PNG servers.
     */
    @Override
    protected void onRegistered(Context context, String regId) {
        PushNotifications.updateServer(context, regId);
    }

    /**
     * This callback method is invoked when GCM delivers a notification to the
     * device.  Here we are simply providing an example of how to display the
     * notification to the user. There are many other implementation options.
     * Older API versions of Android may need to use different classes and
     * methods.
     */
    @Override
    protected void onMessage(Context context, Intent intent) {

        String msgJson = intent.getStringExtra("payload");

        IWearNotificationSender.Factory iWearSender = IWearNotificationSender.Factory.getsInstance();
        IWearAndroidNotificationSender androidNotificationSender = null;
        try {
            androidNotificationSender = (IWearAndroidNotificationSender) iWearSender.createNotificationSender(IWearNotificationType.ANDROID, this, msgJson);
            androidNotificationSender.sendNotification(this);
        } catch (IntuitWearException e) {
            e.printStackTrace();
        }

    }

    /**
     * Callback called upon a GCM error.
     * @param context Application context
     * @param arg1 Error string
     */
    @Override
    protected void onError(Context context, String arg1) {
        Log.e(TAG, "Error related to GCM: " + arg1);
    }

    /**
     * Callback called when device is unregisterd from GCM.
     * @param context Application context
     * @param arg1 Unregister message
     */
    @Override
    protected void onUnregistered(Context context, String arg1) {
        Log.i(TAG, "Received unregistered call");
    }
}
