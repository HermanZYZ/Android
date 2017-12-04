package com.example.zyz.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.reflect.Array.getInt;

public class MainActivity extends AppCompatActivity {
    private EditText newPassword;
    private EditText confirm;
    private Button OK;
    private Button Clear;

    private EditText password;
    private Button register;
    private Button Clear_login;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ELemBinding();
        EvenBinding();
        sharedPreferences = this.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int status = sharedPreferences.getInt("STATUS", 0);
        setVisible(status);
    }

    private void ELemBinding()
    {
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirm = (EditText) findViewById(R.id.confirm);
        register = (Button) findViewById(R.id.register);
        Clear = (Button) findViewById(R.id.clear);

        password = (EditText) findViewById(R.id.password);
        OK = (Button) findViewById(R.id.ok);
        Clear_login = (Button) findViewById(R.id.clear_login);
    }

    private void EvenBinding()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPassword.getText().toString();
                String confirm_password = confirm.getText().toString();
                if(password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                else if(confirm_password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Password needs confirming", Toast.LENGTH_SHORT).show();
                else if(!password.equals(confirm_password))
                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                else
                {
                    editor.putString("PASSWORD", password);
                    editor.putInt("STATUS", 1);
                    editor.commit();
                    setVisible(1);
                }
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setText("");
                confirm.setText("");
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStored = sharedPreferences.getString("PASSWORD",null);
                String passwordIn = password.getText().toString();
                if(!passwordIn.equals(passwordStored))
                    Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                else
                {
                    Intent intent = new Intent().setClass(MainActivity.this, FileEdit.class);
                    startActivity(intent);
                }
            }
        });

        Clear_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });
    }

    private void setVisible(int status)
    {
        if(status == 1)
        {
            newPassword.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);
            register.setVisibility(View.INVISIBLE);
            Clear.setVisibility(View.INVISIBLE);

            password.setVisibility(View.VISIBLE);
            OK.setVisibility(View.VISIBLE);
            Clear_login.setVisibility(View.VISIBLE);
        }
    }
}
