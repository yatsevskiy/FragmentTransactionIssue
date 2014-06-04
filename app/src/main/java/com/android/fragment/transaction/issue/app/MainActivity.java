package com.android.fragment.transaction.issue.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int ADD = 0;
    private static final int REPLACE = 1;
    private boolean mIsChecked;
    private int mNum;

    public static class MainFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int num = getArguments().getInt("num");
            View view = inflater.inflate(R.layout.f_main, container, false);
            view.setBackgroundColor(num % 2 == 0 ? Color.RED : Color.BLUE);

            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(Integer.toString(num));

            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.setMargins(0, (int) dpToPx(20 * num), 0, 0);

            return view;
        }

        private float dpToPx(float dp) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        if (savedInstanceState != null) {
            mNum = savedInstanceState.getInt("num");
        }


        findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment(ADD);
            }
        });

        findViewById(android.R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment(REPLACE);
            }
        });

        CheckBox cb = (CheckBox) findViewById(R.id.check_box);
        mIsChecked = cb.isChecked();
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                mIsChecked = isChecked;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("num", mNum);
    }

    private void newFragment(int op) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putInt("num", mNum++);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        switch (op) {
            case ADD:
                t.add(R.id.container, fragment);
                break;
            case REPLACE:
                t.replace(R.id.container, fragment);
                break;
        }

        if (mIsChecked) {
            t.addToBackStack("");
        }

        t.commit();
    }
}
