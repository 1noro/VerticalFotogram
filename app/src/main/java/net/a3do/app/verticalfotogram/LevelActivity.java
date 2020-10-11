package net.a3do.app.verticalfotogram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;
import java.util.Objects;

public class LevelActivity extends AppCompatActivity {

    Level levelObj;
    ViewPager mViewPager;
    EditText titleAnswerBox;
    TextView titleAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        hideTitleBar();

        // Obtenemos la información del nivel desde el MainActivity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        levelObj = new Level(this, bundle.getInt("levelId", 0), bundle.getInt("levelItemJsonId", 0));

        titleAnswerBox = (EditText) findViewById(R.id.titleAnswerBox);

        titleAnswered = (TextView) findViewById(R.id.titleAnswered);
        titleAnswered.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleAnswered.setSelected(true);
        titleAnswered.setSingleLine(true);

        generateViewPager();
        changeFABIfFrameIsAnswered();
    }

    public void hideTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_level);
    }

    public void setFABAnswered() {
        FloatingActionButton buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonAnswer.setImageResource(R.drawable.check3);
        buttonAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.green)));
    }

    public void setFABNotAnswered() {
        FloatingActionButton buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonAnswer.setImageResource(R.drawable.question3);
        buttonAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.red)));
    }

    public void changeFABIfFrameIsAnswered() {
        boolean isFrameAnswered = levelObj.checkFrameAnswered(mViewPager.getCurrentItem());
        if (isFrameAnswered) {
            titleAnswered.setText(levelObj.getFrameTitleByLang(mViewPager.getCurrentItem(), Locale.getDefault().getLanguage()));
            setFABAnswered();
            titleAnswered.setVisibility(View.VISIBLE);
            titleAnswerBox.setVisibility(View.GONE);
        } else {
//            titleAnswered.setText(getResources().getString(R.string.noAnswer));
            titleAnswerBox.setText(levelObj.getLastFailedAnswer(mViewPager.getCurrentItem()));
            setFABNotAnswered();
            titleAnswerBox.setVisibility(View.VISIBLE);
            titleAnswered.setVisibility(View.GONE);
        }
    }

    public void generateViewPager() {
        mViewPager = findViewById(R.id.viewPagerMain);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(LevelActivity.this, levelObj.getFrameList());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                changeFABIfFrameIsAnswered();
            }
        });
    }

    public TextView generateAnswerDialogTitle() {
        TextView title = new TextView(this);
        title.setText(getResources().getString(R.string.answerTitle));
//        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 20, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        return title;
    }

    public void answerFAB(View view) {
        boolean isFrameAnswered = levelObj.checkFrameAnswered(mViewPager.getCurrentItem());
        if (!isFrameAnswered) {
            String titleToCheck = titleAnswerBox.getText().toString();
            boolean isThereAMatch = levelObj.checkTitle(mViewPager, titleToCheck);
            if (isThereAMatch) {
                titleAnswered.setText(levelObj.getFrameTitleByLang(mViewPager.getCurrentItem(), Locale.getDefault().getLanguage()));
                setFABAnswered();
                titleAnswered.setVisibility(View.VISIBLE);
                titleAnswerBox.setVisibility(View.GONE);
            } else {
                levelObj.setLastFailedAnswer(mViewPager.getCurrentItem(), titleToCheck);
//                titleAnswered.setText(getResources().getString(R.string.wrongAnswer));
            }

//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setCustomTitle(generateAnswerDialogTitle());
//
//            // input container for margins
//            FrameLayout container = new FrameLayout(this);
//            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 35;
//            params.rightMargin = 35;
//
//            // Set up the input
//            final EditText input = new EditText(this);
//            input.setText(levelObj.getLastFailedAnswer(mViewPager.getCurrentItem()));
//            input.setGravity(Gravity.CENTER);
//
//            input.setInputType(InputType.TYPE_CLASS_TEXT);
//            input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    input.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            InputMethodManager inputMethodManager = (InputMethodManager) LevelActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
//                        }
//                    });
//                }
//            });
//
//            input.setLayoutParams(params);
//            container.addView(input);
//
//            builder.setView(container);
//
//            // Set up the buttons
//            builder.setPositiveButton(getResources().getString(R.string.answerCheck), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String titleToCheck = input.getText().toString();
//                    boolean isFrameAnswered = levelObj.checkTitle(mViewPager, titleToCheck);
//                    if (isFrameAnswered) {
//                        setFABAnswered();
////                        titleAnswered.setText(levelObj.getFrameTitleByLang(mViewPager.getCurrentItem(), Locale.getDefault().getLanguage()));
//                    } else {
//                        levelObj.setLastFailedAnswer(mViewPager.getCurrentItem(), titleToCheck);
////                        titleAnswered.setText(getResources().getString(R.string.wrongAnswer));
//                    }
//
//                }
//            });
//
//            builder.show();
//            input.requestFocus();
        }
    }

}
