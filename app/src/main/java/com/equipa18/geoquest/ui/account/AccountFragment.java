package com.equipa18.geoquest.ui.account;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.equipa18.geoquest.PicassoManager;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.player.PlayerManager;

import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment {
    private ImageView picture;
    private TextView username;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        picture = view.findViewById(R.id.profilePictureImage);
        username = view.findViewById(R.id.userNameText);
        username.setText(PlayerManager.getCurrentPlayer().getUsername());
        ContextWrapper cw = new ContextWrapper(getActivity());
        PicassoManager.loadPaths(cw);
        PicassoManager.applyRoundPicture(picture);
        return view;
    }

    /*
    public AccountFragment() {
        super(R.layout.fragment_account);
        picture = getView().findViewById();
        ContextWrapper cw = new ContextWrapper(getActivity());
        PicassoManager.loadPaths(cw);
        PicassoManager.applyRoundPicture();
    }
*/

}
