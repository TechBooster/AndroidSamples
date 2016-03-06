package org.techbooster.sample.intentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView) findViewById(R.id.text);
        Intent i = getIntent();

        if (i != null) {
            String name = i.getStringExtra("Name");
            tv.setText("Welcome " + name);

            i.removeExtra("Name");

            // 一覧の取得例
            Set<String> keys = i.getExtras().keySet();
            for (String key : keys) {
                // key list
            }
        }

    }
}
