package org.techbooster.sample.customtabs;

import android.content.ComponentName;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class MainActivity extends AppCompatActivity{

    private String TAG = "CustomTabs";

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URLをひらく
                lunchCustomTabs("https://google.co.jp/");
            }
        });
        //
        bindCustomTabsService();
    }

    @Override
    protected void onDestroy() {
        unbindCustomTabsService();
        super.onDestroy();
    }

    private void bindCustomTabsService() {
        if (mClient != null) return;

        //　接続先はChromeのバージョンによって異なる
        // "com.android.chrome", "com.chrome.beta",
        // "com.chrome.dev",  "com.google.android.apps.chrome"
        mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
        if (mPackageNameToBind == null) return;

        // ブラウザ側と接続したときの処理
        mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name,
                                                     CustomTabsClient client) {
                // セッションを確立する
                mClient = client;
                mCustomTabsSession = mClient.newSession(new CustomTabsCallback() {
                    @Override
                    public void onNavigationEvent(int navigationEvent, Bundle extras) {
                        Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
                    }
                });
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        // バインドの開始
        CustomTabsClient.bindCustomTabsService(this,
                mPackageNameToBind,
                mConnection);
    }

    private void unbindCustomTabsService() {
        if (mConnection == null) return;
        unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
    }

    private void lunchCustomTabs(String url){
        // ビルダーを使って表示方法を指定する
        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder(mCustomTabsSession);
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
        // CustomTabsでURLをひらくIntentを発行
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this,  Uri.parse(url));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
