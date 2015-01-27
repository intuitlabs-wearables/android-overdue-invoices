package overdueinvoices.example.com.overdueinvoices;

///////////////////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2015 Intuit Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
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

/**
 * The {@code GCMIntentService} class extends the main {@link GCMBaseIntentService} class
 * which provides the Push Notification Gateway callback methods to receive the Push notification
 * message and also to take action after a successful registration to the PNG service.
 */
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
