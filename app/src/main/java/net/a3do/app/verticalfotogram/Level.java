package net.a3do.app.verticalfotogram;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Level extends AppCompatActivity {

    JSONArray levelArray;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Quitamos la barra del titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_level);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int levelItemJsonId = bundle.getInt("levelItemJsonId", 0);

        try {
            levelArray = new JSONArray(readJsonFile(levelItemJsonId));
        } catch (Exception e) {
            Log.d("##### EXCPETION","readJsonFile || new JSONArray(levelData)");
            e.printStackTrace();
        }

        try {
            assert levelArray != null;
//            int frameId0 = getResources().getIdentifier(levelArray.getJSONObject(0).getString("frame"), "drawable", getPackageName());
//            int frameId1 = getResources().getIdentifier(levelArray.getJSONObject(1).getString("frame"), "drawable", getPackageName());
//            int frameId2 = getResources().getIdentifier(levelArray.getJSONObject(2).getString("frame"), "drawable", getPackageName());
//            int frameId3 = getResources().getIdentifier(levelArray.getJSONObject(3).getString("frame"), "drawable", getPackageName());
//
//            // images array
//            int[] frameList = {frameId0, frameId1, frameId2, frameId3};

            int[] frameList = new int[levelArray.length()];
            for (int i = 0; i < frameList.length; i++) {
                frameList[i] = getResources().getIdentifier(levelArray.getJSONObject(i).getString("frame"), "drawable", getPackageName());
            }

            // Initializing the ViewPager Object, Initializing the ViewPagerAdapter, Adding the Adapter to the ViewPager
            mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(Level.this, frameList);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {}
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                public void onPageSelected(int position) {
                    // Check if this is the page you want.
                    FloatingActionButton buttonAnswer = findViewById(R.id.buttonAnswer);
                    buttonAnswer.setImageResource(R.drawable.check2);
                    buttonAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.green)));
                    // Toast.makeText(Level.this, "cambio de imagen", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.d("##### EXCPETION", "ALGUNA COSA EN EL TRATAMIENTO DEL JSON EN EL CREATE");
            e.printStackTrace();
        }
    }

    private String readJsonFile(int id) throws Exception {
        InputStream is = getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }

    public void responder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.answerTitle) + " - " + mViewPager.getCurrentItem());

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) Level.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(getResources().getString(R.string.answerCheck), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleToCheck = input.getText().toString();
                try {
                    if (GameUtils.checkTitle(levelArray.getJSONObject(mViewPager.getCurrentItem()).getJSONArray("title"), titleToCheck)) {
                        Toast.makeText(Level.this, "CORRECTO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Level.this, "FALLO", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        input.requestFocus();
    }

}
