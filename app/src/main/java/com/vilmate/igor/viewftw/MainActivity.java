package com.vilmate.igor.viewftw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ExampleView layoutExampleView = (ExampleView) findViewById(R.id.example);
        layoutExampleView.setTitleTxt("instantiated from layout");

        ExampleView codeExampleView = new ExampleView(this, "instantiated from code");
        container.addView(codeExampleView);

        ExampleView dialogExampleView = new ExampleView(this, "dialog example view");
        dialogExampleView.showAsDialog();
    }
}
