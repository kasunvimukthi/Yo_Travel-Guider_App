package com.yo_travel.yo_travelguider.Slider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yo_travel.yo_travelguider.Login.Login;
import com.yo_travel.yo_travelguider.R;

public class Slider extends AppCompatActivity {

    private ViewPager mSlideView;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNext;
    private Button mBack;
    private Button mCont;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        mSlideView = findViewById(R.id.slide_View);
        mDotLayout = findViewById(R.id.dots_layout);

        mNext = findViewById(R.id.slide_Next);
        mBack = findViewById(R.id.slide_back);
        mCont = findViewById(R.id.slide_Continu);

        sliderAdapter = new SliderAdapter(this);

        mSlideView.setAdapter(sliderAdapter);

        addDotIndicator(0);

        mSlideView.addOnPageChangeListener(viewListner);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideView.setCurrentItem(mCurrentPage + 1);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideView.setCurrentItem(mCurrentPage - 1);
            }
        });

        mCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Slider.this, Login.class));
                finish();
            }
        });
    }

    public void addDotIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotIndicator(i);
            mCurrentPage = i;
            if(i == 0){
                mNext.setEnabled(true);
                mBack.setEnabled(false);
                mCont.setEnabled(false);

                mBack.setVisibility(View.INVISIBLE);
                mCont.setVisibility(View.INVISIBLE);

                mNext.setText("Next");
                mBack.setText("");
                mCont.setText("");

            }else if(i == mDots.length - 1){
                mNext.setEnabled(false);
                mBack.setEnabled(true);
                mCont.setEnabled(true);

                mNext.setVisibility(View.INVISIBLE);
                mBack.setVisibility(View.VISIBLE);
                mCont.setVisibility(View.VISIBLE);

                mNext.setText("");
                mBack.setText("Back");
                mCont.setText("Continue");

            }else {
                mNext.setEnabled(true);
                mBack.setEnabled(true);
                mCont.setEnabled(false);

                mNext.setVisibility(View.VISIBLE);
                mBack.setVisibility(View.VISIBLE);
                mCont.setVisibility(View.INVISIBLE);

                mNext.setText("next");
                mBack.setText("Back");
                mCont.setText("");

            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}