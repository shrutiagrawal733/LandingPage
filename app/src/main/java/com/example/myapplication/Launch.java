package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Launch extends AppCompatActivity {

    private ViewPager screenPager;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button Next;
    int position=0;
    Button btn_GetStarted;
    Animation btn_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        //when this activity is about to launch we need to check if its opened before or not
        if(restorePrefData())
        {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }


        //ini views
        Next = findViewById(R.id.next);
        btn_GetStarted = findViewById(R.id.GetStarted);
        tabIndicator =findViewById(R.id.tab_indicators);
        btn_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);


        //hide the action bar
        //getSupportActionBar().hide();

        //fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("E-Rase your E-waste"," ",R.drawable.img1));
        mList.add(new ScreenItem("Some facts:"," ",R.drawable.img1));


        //setup Viewpager
        screenPager = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);


       //next button click listener
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    position=screenPager.getCurrentItem();
                    if(position<mList.size())
                    {
                        position++;
                        screenPager.setCurrentItem(position);
                    }

                    if(position==mList.size()-1)                //when we reach the last screeen
                    {
                        loadLastScreen();

                    }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==mList.size()-1)
                    loadLastScreen();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get Started button click listener
        btn_GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open Main Activity
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                //also we need to save a boolean value to storage so when the user runs the app we could know that he is already
                //checked the launch activity.

                savePrefsData();
                finish();
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        boolean isIntroActivityOpenedBefore = pref.getBoolean("isLaunchOpened",false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putBoolean("isLaunchOpened",true);
        editor.commit();

    }

    // show the GETSTARTED button and hide the indicator and the next button
    private void loadLastScreen()
    {
        Next.setVisibility(View.INVISIBLE);
        btn_GetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //setup Animation
        btn_GetStarted.setAnimation(btn_anim);
    }
}
