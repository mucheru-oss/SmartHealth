package com.mysasse.afyasmart.ui.fragments.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.data.repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel implements ProfileRepository.UserProfileListener {
    private static final String TAG = "ProfileViewModel";

    private MutableLiveData<Profile> _profile;
    private ProfileRepository profileRepository;

    public ProfileViewModel() {
        _profile = new MutableLiveData<>();
        profileRepository = new ProfileRepository(this);
    }

    public LiveData<Profile> getProfile() {
        profileRepository.getAuthenticatedUserProfile();

        return _profile;
    }

    @Override
    public void showProfile(Profile profile) {
        _profile.setValue(profile);
    }

    @Override
    public void showError(Exception e) {
        Log.e(TAG, "showError: Getting the authenticated user profile", e);
    }
}
