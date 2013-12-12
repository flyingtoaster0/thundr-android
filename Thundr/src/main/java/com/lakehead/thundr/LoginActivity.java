package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Observable;
import java.util.Observer;


public class LoginActivity extends Activity implements OnTaskCompleted {
    String deptCode;
    JSONArray jArray;

    EditText emailText;
    EditText passText;

    Button loginButton;
    Button toRegButton;

    SharedPreferences prefs;
    ProgressBar bar;

    String tokenString;
    JSONObject rememberToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.email_field);
        passText = (EditText) findViewById(R.id.pass_field);
        loginButton = (Button) findViewById(R.id.login_button);
        toRegButton = (Button) findViewById(R.id.no_account_button);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
        tokenString = prefs.getString("remember_token","");
        if(!tokenString.equals(""))
        {
            goToCalendar();
        }
        bar = (ProgressBar) findViewById(R.id.loader);
        bar.setVisibility(View.INVISIBLE);
    }

    //Get the login info, encode the email address, and then put the info into a String.
    public void login(View v)
    {
        String emailAddress = emailText.getText().toString();
        String password = passText.getText().toString();

        if(emailAddress.equals("") || password.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email address or password", Toast.LENGTH_SHORT);
            toast.show();
            passText.setText("");
        }
        else
        {
            try
            {
                emailAddress = URLEncoder.encode(emailAddress, "utf-8");
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            String url = "http://www.thundr.ca/api/login";

            hideUI();
            new LoginTask(this).execute(url, emailAddress, password);
        }
    }

    public void goToRegister(View v)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void goToCalendar()
    {
        Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        if(obj == null)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT);
            toast.show();
            showUI();
            passText.setText("");
        }
        else
        {
            this.rememberToken = (JSONObject)obj;
            String token = "";

            try
            {
                token = rememberToken.getString("token");
            }
            catch(JSONException e)
            {
                Log.d("Exceptions", e.toString());
            }

            prefs.edit().putString("remember_token", token).commit();
            bar.setVisibility(View.GONE);
            goToCalendar();
        }
    }
    private void hideUI()
    {
        emailText.setVisibility(View.INVISIBLE);
        passText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        toRegButton.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }

    private void showUI()
    {
        emailText.setVisibility(View.VISIBLE);
        passText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        toRegButton.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    }
}