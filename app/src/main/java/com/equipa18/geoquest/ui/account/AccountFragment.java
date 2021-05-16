package com.equipa18.geoquest.ui.account;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.equipa18.geoquest.ChangePasswordActivity;
import com.equipa18.geoquest.PicassoManager;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.RegisterActivity;
import com.equipa18.geoquest.StatisticsActivity;
import com.equipa18.geoquest.player.PlayerManager;

import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment {
    private ImageView picture;
    private TextView username;
    private Button changePic;
    private Button changePass;
    private Button stats;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //standard stuff, getting the ids
        picture = view.findViewById(R.id.profilePictureImage);
        username = view.findViewById(R.id.userNameText);
        changePic = view.findViewById(R.id.changeProfilePictureButton);
        changePass = view.findViewById(R.id.changePasswordButton);
        stats = view.findViewById(R.id.statisticsButton);

        //here we start populating data, the fun stuff
        username.setText(PlayerManager.getCurrentPlayer().getUsername());
        ContextWrapper cw = new ContextWrapper(getActivity());
        PicassoManager.loadPaths(cw);
        PicassoManager.applyRoundPicture(picture);

        changePic.setOnClickListener(v -> {
            Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cam, 0);
        });

        changePass.setOnClickListener(v -> {
            goToChange();
        });

        stats.setOnClickListener(v -> {
            goToStats();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(PicassoManager.savePic(data)) {
            super.onActivityResult(requestCode, resultCode, data);
            PicassoManager.applyRoundPicture(picture);
        }
    }

    private void goToChange() {
        Intent changeScreen = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(changeScreen);
    }

    private void goToStats() {
        Intent changeScreen = new Intent(getActivity(), StatisticsActivity.class);
        startActivity(changeScreen);
    }
}
