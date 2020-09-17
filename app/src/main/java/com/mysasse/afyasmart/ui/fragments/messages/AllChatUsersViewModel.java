package com.mysasse.afyasmart.ui.fragments.messages;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.data.repositories.ProfilesRepository;

import java.util.List;

public class AllChatUsersViewModel extends ViewModel implements ProfilesRepository.ProfilesTaskListener {
    private static final String TAG = "AllChatUsersViewModel";

    private ProfilesRepository profilesRepository;

    private MutableLiveData<List<Profile>> _profiles;

    public AllChatUsersViewModel() {
        _profiles = new MutableLiveData<>();

        profilesRepository = new ProfilesRepository(this);
    }

    public LiveData<List<Profile>> getProfiles() {

        profilesRepository.getDoctors();

        return _profiles;
    }

    @Override
    public void showProfiles(List<Profile> profileList) {
        _profiles.setValue(profileList);
    }

    @Override
    public void showErrors(Exception error) {
        Log.e(TAG, "showErrors: getting profiles", error);
    }
}
