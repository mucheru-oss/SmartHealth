package com.mysasse.afyasmart.ui.fragments.profile;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private CircleImageView userAvatarCiv;
    private TextView userNameTv;
    private TextView userPhoneTv;
    private TextView userRoleTv;
    private TextView userBioTv;

    private Profile mProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register Views
        userAvatarCiv = view.findViewById(R.id.user_avatar_civ);
        userNameTv = view.findViewById(R.id.user_name_tv);
        userPhoneTv = view.findViewById(R.id.user_phone_tv);
        userRoleTv = view.findViewById(R.id.user_role_tv);
        userBioTv = view.findViewById(R.id.user_bio_tv);

        Button editAccountBtn = view.findViewById(R.id.edit_account_btn);

        editAccountBtn.setOnClickListener(v -> {
            ProfileFragmentDirections.ActionProfileFragmentToEditProfileFragment actionProfileFragmentToEditProfileFragment = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
            actionProfileFragmentToEditProfileFragment.setProfile(mProfile);

            Navigation.findNavController(v).navigate(actionProfileFragmentToEditProfileFragment);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            assert profile != null;

            mProfile = profile;

            Glide.with(this)
                    .load(profile.getAvatar())
                    .centerCrop()
                    .placeholder(R.drawable.ic_account_circle)
                    .into(userAvatarCiv);


            userNameTv.setText(profile.getName());
            userRoleTv.setText(profile.getRole());

            if (!TextUtils.isEmpty(profile.getPhone()))
                userPhoneTv.setText(profile.getPhone());

            if (!TextUtils.isEmpty(profile.getBio()))
                userBioTv.setText(profile.getBio());
        });
    }
}
