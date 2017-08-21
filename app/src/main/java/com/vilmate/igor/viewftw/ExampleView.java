package com.vilmate.igor.viewftw;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.vilmate.igor.viewsftw.lib.ViewFtw;

public class ExampleView extends ViewFtw {
    private static final String TAG = ExampleView.class.getName();
    private static final String TITLE_KEY = TAG + "TITLE_KEY";

    private String title;

    private TextView titleTxt;

    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleView(Context context, String title) {
        super(context, null);
        this.title = title;
    }

    @Override
    public void onViewCreated(Bundle savedState) {
        super.onViewCreated(savedState);
        if (null != savedState) {
            String savedStateTitle = savedState.getString(TITLE_KEY);
            Log.d(TAG, "onViewCreated with title = " + title + " and savedStateTitle = " + savedStateTitle);
            title = savedStateTitle;
        }

        getLayoutInflater().inflate(R.layout.example, this);
        titleTxt = (TextView) findViewById(R.id.title);
        titleTxt.setText(title);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart with title = " + title);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop with title = " + title);
    }

    @Override
    protected Bundle onSaveViewInstanceState() {
        Log.d(TAG, "onSaveViewInstanceState with title = " + title);
        Bundle state = super.onSaveViewInstanceState();
        state.putString(TITLE_KEY, title);

        return state;
    }

    public void setTitleTxt(String title) {
        this.title = title;
        if (null != titleTxt) {
            titleTxt.setText(title);
        }
    }
}
