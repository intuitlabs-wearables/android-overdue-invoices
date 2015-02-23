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
import com.intuit.mobile.png.sdk.PushNotificationsV2;
import com.intuit.mobile.png.sdk.UserTypeEnum;
import com.intuit.mobile.png.sdk.callback.RegisterUserCallback;

/**
 * The {@code GCMIntentService} class extends the main {@link GCMBaseIntentService} class
 * which provides the Push Notification Gateway callback methods to receive the Push notification
 * message and also to take action after a successful registration to the PNG service.
 */
public class GCMIntentService extends GCMBaseIntentService {
    private static final String LOG_TAG = GCMIntentService.class.getSimpleName();
    private static final String REG_URL = "https://png.d2d.msg.intuit.com";

    private static String userid;
    private static String[] groups;

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
     * Register with GCM, which will eventually trigger {@link #onRegistered} to be called.
     *
     * @param context {@link Context} Application context
     * @param userid  {@link String} how your app refers to this user
     * @param groups  {@link String[]} may be null
     */
    public static void register(final Context context, String userid, final String[] groups) {
        GCMIntentService.userid = userid;
        GCMIntentService.groups = groups;

        PushNotificationsV2.URL_OVERRIDE = REG_URL;
        PushNotificationsV2.Environment environment = PushNotificationsV2.Environment.SANDBOX;
        PushNotificationsV2.initialize(OverdueInvoicesActivity.pngSenderId, OverdueInvoicesActivity.PROJECT_NUMBER, environment);
        PushNotificationsV2.setLogging(true);
        PushNotificationsV2.registerForGCMNotifications(context);
    }

    /**
     * Google will call this method, providing you a unique registrationId for this device.
     * We recommended to save the registrationId to local preferences for later use.
     * e.g. saveRegistrationId(registrationId);
     *
     * @param context        {@link Context} Application context
     * @param registrationId {@link String} unique registrationId for this device
     */
    @Override
    protected void onRegistered(final Context context, final String registrationId) {
        Log.i(TAG, "onRegistered called.  registrationId = " + registrationId);

        PushNotificationsV2.registerUser(
                this,
                GCMIntentService.userid,
                UserTypeEnum.OTHER,
                GCMIntentService.groups,
                registrationId,
                new RegisterUserCallback() {


                    @Override
                    public void onUserRegistered() {
                        Log.i(LOG_TAG, "Registration call to PNG servers was accepted");
                    }

                    @Override
                    public void onError(String code, String description) {
                        Log.i(LOG_TAG, String.format("Received error callback from PNG. Error code= %s, description= %s", code, description));
                    }
                });
    }

    /**
     * This callback method is invoked when GCM delivers a notification to the
     * device.
     *
     * Assuming that the json encoded message is a valid (see IntuitWear JSONSchema) document,
     * we acquire an instance of a {@link IWearNotificationSender.Factory} to create a NotificationSender,
     * which will send the generated notification to the wearable device.
     *
     * @param context {@link Context} Application context
     * @param intent {@link Intent} received with the push notification
     */
    @Override
    protected void onMessage(final Context context, final Intent intent) {

        String msgJson = intent.getStringExtra("payload");

        IWearNotificationSender.Factory iWearSender = IWearNotificationSender.Factory.getsInstance();
        try {
            IWearAndroidNotificationSender androidNotificationSender = (IWearAndroidNotificationSender) iWearSender.createNotificationSender(IWearNotificationType.ANDROID, this, msgJson);
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
