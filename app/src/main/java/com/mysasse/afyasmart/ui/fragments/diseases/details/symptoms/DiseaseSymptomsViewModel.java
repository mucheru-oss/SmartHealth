package com.mysasse.afyasmart.ui.fragments.diseases.details.symptoms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.models.DiseaseSymptom;
import com.mysasse.afyasmart.data.repositories.SymptomsRepository;

import java.util.List;

public class DiseaseSymptomsViewModel extends ViewModel implements SymptomsRepository.SymptomsTaskListener {

    private MutableLiveData<List<DiseaseSymptom>> _diseaseSymptoms = new MutableLiveData<>();
    private MutableLiveData<Exception> _exception = new MutableLiveData<>();

    public DiseaseSymptomsViewModel(Disease disease) {
        SymptomsRepository symptomsRepository = new SymptomsRepository(this);

        symptomsRepository.getDiseaseSymptoms(disease);
    }

    public LiveData<List<DiseaseSymptom>> getDiseaseSymptoms() {
        return _diseaseSymptoms;
    }

    public LiveData<Exception> getException() {
        return _exception;
    }

    @Override
    public void onBrowse(List<DiseaseSymptom> diseaseSymptoms) {
        _diseaseSymptoms.setValue(diseaseSymptoms);
    }

    @Override
    public void onError(Exception e) {
        _exception.setValue(e);
    }
}