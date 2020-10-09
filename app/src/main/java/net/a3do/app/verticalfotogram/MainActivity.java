package net.a3do.app.verticalfotogram;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitamos la barra del titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        try {
            JSONObject parameters = new JSONObject("{\"levelId\" : 1, \"levelFileJSONId\" : " + R.raw.level1 + "}");
            Button level1 = findViewById(R.id.buttonLevel1);
            level1.setOnClickListener(new MyOnClickListener(parameters) {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intentLevel = new Intent(getApplicationContext(), LevelActivity.class);
                        intentLevel.putExtra("levelItemJsonId", this.parameters.getInt("levelFileJSONId"));
                        startActivity(intentLevel);
                    } catch (JSONException e) {
                        Log.d("##### EXCPETION","this.params.getInt(\"levelFileJSON\")");
                        e.printStackTrace();
                    }
                }
            });

            Button level2 = findViewById(R.id.buttonLevel2);
            level2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.notImplementedYet), Toast.LENGTH_SHORT).show();
                }
            });

            Button level3 = findViewById(R.id.buttonLevel3);
            level3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.notImplementedYet), Toast.LENGTH_SHORT).show();
                }
            });

            Button level4 = findViewById(R.id.buttonLevel4);
            level4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.notImplementedYet), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.d("##### EXCPETION", "jsonResponse.get || new JSONObject(...)");
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
    }

}