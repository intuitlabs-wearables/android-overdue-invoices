package overdueinvoices.example.com.overdueinvoices;

///////////////////////////////////////////////////////////////////////////////////////////////
//  Copyright (c) 1/2/15 Intuit Inc. All rights reserved. Unauthorized reproduction is a
//  violation of applicable law. This material contains certain confidential and proprietary
//  information and trade secrets of Intuit Inc.
///////////////////////////////////////////////////////////////////////////////////////////////

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.intuit.mobile.png.sdk.PushNotifications;
import com.intuit.mobile.png.sdk.UserTypeEnum;

public class OverdueInvoicesActivity extends Activity {

    /**
     * GCM Project Number constant.
     * Set this to your own GCM Project Number.
     */
    public static final String PROJECT_NUMBER = "270204895990";

    /**
     * Push Notification Gateway Sender Id constant.
     * Set this to your own PNG Sender Id.
     */
    public final String pngSenderId = "b394f11c-34f0-4f14-ac97-fd96bc505d10";

    /**
     * Your QBO email address.
     */
    public final String username = "cosmon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_invoices);

        // Set up Push Notification Gateway to receive remote notifications
        setupPushGateway();
    }

    /*
	 * Invoke the PushGateway SDK to register the current user to receive
	 * notifications on their device
	 */
    private void setupPushGateway() {

        PushNotifications.register(this, PROJECT_NUMBER,
                username, null, UserTypeEnum.OTHER, pngSenderId,
                false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overdue_invoices_activiry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
