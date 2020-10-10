package net.a3do.app.verticalfotogram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class LevelActivity extends AppCompatActivity {

    Level levelObj;
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

        levelObj = new Level(this, bundle.getInt("levelId", 0), bundle.getInt("levelItemJsonId", 0), "[]");

        mViewPager = findViewById(R.id.viewPagerMain);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(LevelActivity.this, levelObj.getFrameList());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                // Check if this is the page you want.
                boolean answered = levelObj.checkFrameAnswered(mViewPager.getCurrentItem());
                if (answered) setFABAnswered();
                else setFABNotAnswered();
            }
        });

    }

    public void setFABAnswered() {
        FloatingActionButton buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonAnswer.setImageResource(R.drawable.check2);
        buttonAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.green)));
    }

    public void setFABNotAnswered() {
        FloatingActionButton buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonAnswer.setImageResource(R.drawable.question2);
        buttonAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.red)));
    }

    public void responder(View view) {
        boolean answered = levelObj.checkFrameAnswered(mViewPager.getCurrentItem());
        if (!answered) {
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
                            InputMethodManager inputMethodManager = (InputMethodManager) LevelActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    boolean answered = levelObj.checkTitle(mViewPager, titleToCheck);
                    if (answered) setFABAnswered();
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
        } else {
            Toast answeredToast = Toast.makeText(this, levelObj.getFrameTitleByLang(mViewPager.getCurrentItem(), "en"), Toast.LENGTH_SHORT);
            answeredToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 40);
            answeredToast.show();
        }
    }

}
