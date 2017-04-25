package edu.umd.cmsc436.mstestsuite;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import edu.umd.cmsc436.mstestsuite.model.UserManager;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int layouts[];
    Button next;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private UserManager mUserManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserManager = new UserManager(this.getApplicationContext());
        if (mUserManager.getCurUserID() != null) {
            Log.i("Login", "There was a user");
            startMainActivity();
        }
        setContentView(R.layout.activity_intro);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout)findViewById(R.id.layoutDots);
        next = (Button)findViewById(R.id.btn_next);
        layouts = new int[]{R.layout.activity_intro_1, R.layout.activity_intro_2, R.layout.activity_intro_3};
        addBottomDots(0);
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current<layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    saveUser();
                    startMainActivity();
                }
            }
        });

    }

    private void startMainActivity() {
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private void addBottomDots(int position){
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dotActive);
        int[] colorInActive = getResources().getIntArray(R.array.dotInactive);
        dotsLayout.removeAllViews();
        for (int i=0; i<dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInActive[position]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(colorActive[position]);
        }
    }

    private void saveUser(){
        EditText name = (EditText) findViewById(R.id.patient_id_box);
        EditText dob = (EditText) findViewById(R.id.dob);
        Spinner handSpin = (Spinner) findViewById(R.id.handedness_spinner);
        Spinner genderSpin = (Spinner) findViewById(R.id.gender_spinner);
        Log.i("Log", "USER Added");
        UserManager.initWithUser(getApplicationContext(), name.getText().toString(),
                UserManager.Handedness.valueOf(handSpin.getSelectedItem().toString()),
                dob.getText().toString(), UserManager.Gender.valueOf(genderSpin.getSelectedItem().toString()));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem()+ i;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length-1) {
                next.setText(R.string.introscreen_continue);
            } else {
                next.setText(R.string.introscreen_next_text);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            if (position == 2) {
                Spinner hand_spinner = (Spinner) findViewById(R.id.handedness_spinner);
                ArrayAdapter<CharSequence> handArrayAdapter = ArrayAdapter.createFromResource(getBaseContext(), R
                        .array.handedness_array, android.R.layout.simple_spinner_item);
                handArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hand_spinner.setAdapter(handArrayAdapter);
                Spinner gender_spinner = (Spinner) findViewById(R.id.gender_spinner);
                ArrayAdapter<CharSequence> genderArrayAdapter = ArrayAdapter.createFromResource(getBaseContext(), R
                        .array.gender_array, android.R.layout.simple_spinner_item);
                genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gender_spinner.setAdapter(genderArrayAdapter);
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }
}
