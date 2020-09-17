package com.mysasse.afyasmart.ui.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.repositories.DiseaseRepository;

import java.util.List;

public class HomeViewModel extends ViewModel implements DiseaseRepository.DiseaseTaskListener {

    private MutableLiveData<List<Disease>> _diseases = new MutableLiveData<>();
    private MutableLiveData<Exception> _exception = new MutableLiveData<>();

    public HomeViewModel() {
        DiseaseRepository diseaseRepository = new DiseaseRepository(this);

        diseaseRepository.getAllDiseases();
    }

    public LiveData<List<Disease>> getDiseases() {
        return _diseases;
    }

    public LiveData<Exception> getException() {
        return _exception;
    }

    @Override
    public void showDiseases(List<Disease> diseases) {
        _diseases.setValue(diseases);
    }

    @Override
    public void showError(Exception exception) {
        _exception.setValue(exception);
    }
}
