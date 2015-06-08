package com.davifantasia.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button mFixedAspectRatioButton;
    private Button mAdaptiveAspectRatioButton;
    private Button mFixedAspectRatioInstagramButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mAdaptiveAspectRatioButton = (Button) findViewById(R.id.adaptive_aspect_ratio_button);
        mAdaptiveAspectRatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdaptiveAspectRatioActivity.class);
                startActivity(intent);
            }
        });

        mFixedAspectRatioButton = (Button) findViewById(R.id.fixed_aspect_ratio_button);
        mFixedAspectRatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FixedAspectRatioActivity.class);
                startActivity(intent);
            }
        });

        mFixedAspectRatioInstagramButton = (Button) findViewById(R.id.fixed_aspect_ratio_instagram_button);
        mFixedAspectRatioInstagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FixedAspectRatioInstagramActivity.class);
                startActivity(intent);
            }
        });

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
