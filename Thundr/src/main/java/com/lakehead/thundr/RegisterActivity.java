package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends Activity implements OnTaskCompleted {
    String deptCode;
    JSONArray jArray;

    EditText emailText;
    EditText userText;
    EditText passText;
    EditText passConfText;
    Button registerButton;
    Button toLoginButton;

    ProgressBar bar;

    JSONObject rememberToken;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        setContentView(R.layout.activity_register);


        emailText = (EditText) findViewById(R.id.email_register_field);
        userText = (EditText) findViewById(R.id.user_register_field);
        passText = (EditText) findViewById(R.id.register_pass_field);
        passConfText = (EditText) findViewById(R.id.register_confirm_pass_field);

        registerButton = (Button) findViewById(R.id.register_button);
        toLoginButton = (Button) findViewById(R.id.already_account_button);

        bar = (ProgressBar) findViewById(R.id.loader);
        bar.setVisibility(View.INVISIBLE);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
    }

    public void register(View v)
    {
        String email;
        String user;
        String pass;
        String passConfirm;

        email = emailText.getText().toString();
        user = userText.getText().toString();
        pass = passText.getText().toString();
        passConfirm = passConfText.getText().toString();


        if(!validEmail(email))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(!user.matches("[A-Za-z0-9_-]*"))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Valid username characters include letters, numbers, _, and -", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(!pass.equals(passConfirm))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_SHORT);
            toast.show();
            passText.setText("");
            passConfText.setText("");
        }
        else
        {
            String url = "http://thundr.ca/api/register";
            new RegisterTask(this).execute(url, email, user, pass, passConfirm);
        }
    }

    public void goToLogin(View v)
    {
        finishActivity();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        if(obj == null)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Server Error. Could not register.", Toast.LENGTH_SHORT);
            toast.show();
            showUI();
            //passText.setText("");
            //passConfText.setText("");
        }
        else if(obj.getClass() == String.class && ((String)obj).equals(RegisterTask.USER_OR_EMAIL_ALREADY_EXISTS))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "A user with that email address or username already exists.", Toast.LENGTH_SHORT);
            toast.show();
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
            goToMySchedules();
        }
    }


    private void finishActivity()
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void hideUI()
    {
        emailText.setVisibility(View.INVISIBLE);
        passText.setVisibility(View.INVISIBLE);
        userText.setVisibility(View.INVISIBLE);
        passConfText.setVisibility(View.INVISIBLE);
        toLoginButton.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }

    private void showUI()
    {
        emailText.setVisibility(View.VISIBLE);
        passText.setVisibility(View.VISIBLE);
        userText.setVisibility(View.VISIBLE);
        passConfText.setVisibility(View.VISIBLE);
        toLoginButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);

        bar.setVisibility(View.GONE);
    }

    private void goToMySchedules()
    {
        Intent intent = new Intent(RegisterActivity.this, ScheduleActivity.class);
        startActivity(intent);
    }
}