package com.mysasse.afyasmart.ui.fragments.doctors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.data.repositories.DoctorsRepository;

import java.util.List;

public class DoctorsViewModel extends ViewModel implements DoctorsRepository.DoctorsTaskCompleteListener {

    private MutableLiveData<List<Profile>> _profiles = new MutableLiveData<>();
    private MutableLiveData<Exception> _exception = new MutableLiveData<>();

    public DoctorsViewModel() {
        DoctorsRepository repository = new DoctorsRepository(this);
        repository.getDoctorsFromFirebase();
    }

    public LiveData<List<Profile>> getDoctors() {
        return _profiles;
    }

    public LiveData<Exception> getException() {
        return _exception;
    }

    @Override
    public void onComplete(List<Profile> profiles) {
        _profiles.setValue(profiles);
    }

    @Override
    public void onDoctorAdded() {

    }

    @Override
    public void onError(Exception error) {
        _exception.setValue(error);
    }
}
