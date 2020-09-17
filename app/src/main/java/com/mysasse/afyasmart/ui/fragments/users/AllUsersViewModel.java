package com.mysasse.afyasmart.ui.fragments.users;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.data.repositories.ProfilesRepository;

import java.util.List;

public class AllUsersViewModel extends ViewModel implements ProfilesRepository.ProfilesTaskListener {
    private static final String TAG = "AllUsersViewModel";
    private MutableLiveData<List<Profile>> _profiles;
    private ProfilesRepository profilesRepository;


    public AllUsersViewModel() {
        _profiles = new MutableLiveData<>();
        profilesRepository = new ProfilesRepository(this);
    }

    public LiveData<List<Profile>> getProfiles() {
        profilesRepository.getAllProfiles();

        return _profiles;
    }

    @Override
    public void showProfiles(List<Profile> profileList) {
        Log.d(TAG, "showProfiles: count" + profileList.size());
        _profiles.setValue(profileList);
    }

    @Override
    public void showErrors(Exception error) {
        Log.e(TAG, "showErrors: ", error);
    }
}
