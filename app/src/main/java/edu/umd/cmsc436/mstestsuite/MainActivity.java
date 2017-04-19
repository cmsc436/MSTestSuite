package edu.umd.cmsc436.mstestsuite;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        BottomSheetBehavior b = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
