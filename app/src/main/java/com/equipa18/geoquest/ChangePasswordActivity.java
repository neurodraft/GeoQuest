package com.equipa18.geoquest;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.equipa18.geoquest.player.Player;
import com.equipa18.geoquest.player.PlayerManager;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button change;
    private ImageButton back;
    private TextInputEditText oldPW, newPW, checkPW;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        change = findViewById(R.id.savePasswordChangesButton);
        back = findViewById(R.id.backArrowImageButtonChangePassword);

        change.setOnClickListener(v -> {
            getTextInFields();
            if(checkTextFields()) {
                changePassword(oldPW.getText().toString(), newPW.getText().toString(), checkPW.getText().toString());
                PlayerManager.savePlayers(new ContextWrapper(getApplicationContext()));
            }
        });

        back.setOnClickListener(v ->
            onBackPressed()
        );
    }

    private void getTextInFields() {
        oldPW = findViewById(R.id.currentPasswordTextInputEditText);
        newPW = findViewById(R.id.newPasswordTextInputEditText);
        checkPW = findViewById(R.id.confirmNewPasswordTextInputEditText);
    }

    private void changePassword(String oldPw, String newPW, String checkPW) {
        Player p = PlayerManager.getCurrentPlayer();

        //Toasts for feedback, because we want the user to feel special :)
        Toast success = Toast.makeText(getApplicationContext(), "Password changed!", Toast.LENGTH_SHORT);
        Toast failPWN = Toast.makeText(getApplicationContext(), "New passwords don't match.", Toast.LENGTH_SHORT);
        Toast failPW = Toast.makeText(getApplicationContext(), "The password doesn't match your account's password.", Toast.LENGTH_SHORT);

        if(!oldPw.equals(p.getPassword())) {
            failPW.show();
            return;
        }

        if(!newPW.equals(checkPW)) {
            failPWN.show();
            return;
        }

        p.setPassword(newPW);
        success.show();
    }

    private boolean checkTextFields() {
        boolean success = true;
        if(oldPW.getText() == null || newPW.getText() == null || checkPW.getText() == null) {
            success = false;
        }
        return success;
    }

}
