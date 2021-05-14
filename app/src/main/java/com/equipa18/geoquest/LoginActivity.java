package com.equipa18.geoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText email; //this is to validate later
    private EditText password; //this too
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    /*the parameters are unused for now, they will be used to
    verify the login once we have the serialized list of useres.*/
    private void login(String email, String password) {
        //TODO: implement verification of password+email
        Intent changeScreen = new Intent(this, MainActivity.class);
        startActivity(changeScreen);
    }

    private void register() {
        Intent changeScreen = new Intent(this, RegisterActivity.class);
        startActivity(changeScreen);
    }
}
