package com.lakehead.thundr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;


public class RegisterActivity extends Activity {
    String deptCode;
    JSONArray jArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        setContentView(R.layout.activity_register);

    }

    public void register(View v)
    {

    }

    public void goToLogin(View v)
    {
        finishActivity();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity()
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}