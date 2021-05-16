package com.equipa18.geoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.equipa18.geoquest.player.Player;
import com.equipa18.geoquest.player.PlayerManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText name;
    private Button goToLogin;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_GeoQuest_NoActionBar);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GeoQuest_PopupOverlay);
        setContentView(R.layout.fragment_register);

        goToLogin = findViewById(R.id.button_gotologin);
        register = findViewById(R.id.button_createAccount);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        name = findViewById(R.id.register_name);

        //Listeners for the register buttons
        register.setOnClickListener(v -> register(name.getText().toString(), email.getText().toString(), password.getText().toString()));
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    /**
     * Registers the user in the """"""database"""""" and redirects to the main activity.
     * @param name Username to register
     * @param email Email to register
     * @param password Password to register
     */
    private void register(String name, String email, String password) {
        //Toasts for feedback, because we want the user to feel special :)
        Toast success = Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT);
        Toast fail = Toast.makeText(getApplicationContext(), "Hm... It seems this email already exists!", Toast.LENGTH_SHORT);

        if(PlayerManager.insertPlayer(new Player(name, email, password))) {
            PlayerManager.savePlayers(getApplicationContext());
            success.show();
            goToMainActivity();
        } else {
            fail.show();
        }
    }

    /**
     * Redirects to main activity
     */
    private void goToMainActivity() {
        Intent changeScreen = new Intent(this, MainActivity.class);
        startActivity(changeScreen);
    }

    /**
     * Redirects to login activity
     */
    private void goToLogin() {
        Intent changeScreen = new Intent(this, LoginActivity.class);
        startActivity(changeScreen);
    }
}
