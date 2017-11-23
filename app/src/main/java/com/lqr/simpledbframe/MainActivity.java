package com.lqr.simpledbframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lqr.simpledbframe.android.AndroidSqliteActivity;
import com.lqr.simpledbframe.customer.CustomerDbFrameActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void android_sqlite(View view) {
        AndroidSqliteActivity.launch(this);
    }

    public void customer_db_frame(View view) {
        CustomerDbFrameActivity.launch(this);
    }
}
