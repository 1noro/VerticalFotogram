package net.a3do.app.verticalfotogram;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

        titleAnswerBox = findViewById(R.id.titleAnswerBox);

        titleAnswered = findViewById(R.id.titleAnswered);
        titleAnswered.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleAnswered.setSelected(true);
        titleAnswered.setSingleLine(true);

        generateViewPager();
        changeAnswerUIIfFrameIsAnswered();
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

    public void changeAnswerUIIfFrameIsAnswered() {
        boolean isFrameAnswered = levelObj.checkFrameAnswered(mViewPager.getCurrentItem());
        if (isFrameAnswered) {
            titleAnswered.setText(levelObj.getFrameTitleByLang(mViewPager.getCurrentItem(), Locale.getDefault().getLanguage()));
            setFABAnswered();
            titleAnswered.setVisibility(View.VISIBLE);
            titleAnswerBox.setVisibility(View.GONE);
        } else {
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
                changeAnswerUIIfFrameIsAnswered();
            }
        });
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
            }
        }
    }

}
