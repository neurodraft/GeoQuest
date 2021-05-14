package com.equipa18.geoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText email; //this is to validate later
    private EditText password; //this too
    private EditText name; //this... too
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        login = (Button) findViewById(R.id.button_gotologin);
        register = (Button) findViewById(R.id.button_createAccount);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        name = (EditText) findViewById(R.id.register_name);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(name.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    /*the parameters are unused for now, they will be used to
    verify the registration once we have the serialized list of users.*/
    private void register(String name, String email, String password) {
        Intent changeScreen = new Intent(this, MainActivity.class);
        startActivity(changeScreen);
    }

    private void goToLogin() {
        Intent changeScreen = new Intent(this, LoginActivity.class);
        startActivity(changeScreen);
    }
}
