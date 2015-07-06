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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This Android Wear sample app is a fully functioning example of how to use the IntuitWear SDK
 * for Android integrated with itDuzzit to build an app that sends a notification to an Android
 * Wear watch for the 3 top overdue invoices in my QBO account.  It illustrates how to use the
 * data in the Push notification to build a multi-page Android Wear application.  The app begins
 * by using itDuzzit to get quick access to QBO invoice information.  We create a Duzzit that
 * computes the total of the top 3 overdue invoice accounts, uses the BigText Style Notification
 * Duzzit provided with the SDK to format the content of the Notification into JSON, and then uses
 * the Push Notification Gateway Duzzit also provided as part of the SDK to deliver Push
 * Notifications to our Android phone app.  The phone application then uses the IntuitWear SDK to
 * receive the Push Notification and parse the JSON it receives as input in order to send an
 * Android Notification to the watch.
 *
 * The {@code OverdueInvoicesActivity} class extends the main Android {@link Activity} class to
 * provide the launch point for the Overdue Invoices application.  Its primary purpose to register
 * to receive PUSH notifications which will be received in the GCMIntentService class
 * {@link GCMIntentService}.
 */
public class OverdueInvoicesActivity extends Activity {

    /**
     * GCM Project Number constant.
     * Set this to your own GCM Project Number.
     */
    public static final String PROJECT_NUMBER = "INSERT_YOUR_GOOGLE_PROJECT_NUMBER_HERE";

    /**
     * Push Notification Gateway Sender Id constant.
     * Set this to your own PNG Sender Id.
     */
    public static final String pngSenderId = "INSERT-YOUR-SENDERID-HERE";

    /**
     * Your QBO email address OR some other unique name to identify who to send the PUSH to.
     */
    public final String username = "UNIQUE_ID_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_invoices);

        // Set up Push Notification Gateway to receive remote notifications
        setupPushGateway();
    }

    /*
	 * Invoke the PushGateway SDK to register the 'username' to receive
	 * notifications on their device
	 */
    private void setupPushGateway() {

        GCMIntentService.register(this, username, new String[]{"OverdueInvoices"});
    }

    /**
     * Use this method to instantiate your menu, and add your items to it. You
     * should return true if you have added items to it and want the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overdue_invoices_activiry, menu);
        return true;
    }

    /**
     * This method is called when one of the menu items to selected. These items
     * can be on the Action Bar, the overflow menu, or the standard options menu. You
     * should return true if you handle the selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
