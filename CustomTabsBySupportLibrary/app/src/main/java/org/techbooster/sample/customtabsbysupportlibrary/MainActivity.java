package org.techbooster.sample.customtabsbysupportlibrary;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String TAG = "CustomTabs";

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
                // lunchCustomTabsWithAction("https://google.co.jp/");
                // lunchCustomTabsWithMenuItem("https://google.co.jp/");
            }
        });
    }

    private void lunchCustomTabs(String url) {
        // ビルダーを使って表示方法を指定する
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

        // CustomTabsでURLをひらくIntentを発行
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void lunchCustomTabsWithAction(String url) {

        // ビルダーを使って表示方法を指定する
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

        // アクションボタン押下時に発行するIntent
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("test", "Action Button Pushed!");
        // PendingIntentを用意する
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* REQUEST_CODE */, intent, 0 /* flags */);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setActionButton(icon, "action", pendingIntent);

        // CustomTabsでURLをひらくIntentを発行
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void lunchCustomTabsWithMenuItem(String url) {

        // ビルダーを使って表示方法を指定する
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

        // メニュー押下のときのIntentをpendingIntentにいれて設定
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Send from Chrome Custom Tabs!");
        // PendingIntentを用意する
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* REQUEST_CODE */, intent, 0 /* flags */);
        builder.addMenuItem("ACTION_SEND", pendingIntent);

        // CustomTabsでURLをひらくIntentを発行
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        if( i != null && i.getStringExtra("test") != null){
            Log.d(TAG, i.getStringExtra("test") );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 返り値の取得
        Log.d(TAG, intent.getStringExtra("test") );
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Action Button Pushed!
                Log.d(TAG, intent.getStringExtra("test") );
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Log.d(TAG, "canceled");
            }
        }
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
