package com.equipa18.geoquest.map;

import android.os.CountDownTimer;

import androidx.lifecycle.ViewModel;

public class QuizzViewModel extends ViewModel {
    private CountDownTimer countdown;

    public QuizzViewModel(){
        countdown = new CountDownTimer(30000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void startCountdown(){
        countdown.start();
    }

}