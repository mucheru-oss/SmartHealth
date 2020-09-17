package com.mysasse.afyasmart.ui.fragments.diseases.admin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.repositories.DiseaseRepository;

import java.util.List;

public class AllDiseasesViewModel extends ViewModel implements DiseaseRepository.DiseaseTaskListener {
    private static final String TAG = "AllDiseasesViewModel";

    private MutableLiveData<List<Disease>> _disease;
    private DiseaseRepository diseaseRepository;

    public AllDiseasesViewModel() {
        _disease = new MutableLiveData<>();
        diseaseRepository = new DiseaseRepository(this);
    }

    @Override
    public void showDiseases(List<Disease> diseases) {
        _disease.setValue(diseases);
    }

    public LiveData<List<Disease>> getAllDiseases() {
        diseaseRepository.getAllDiseases();

        return _disease;
    }

    @Override
    public void showError(Exception exception) {
        Log.e(TAG, "showError: getting diseases", exception);
    }
}
