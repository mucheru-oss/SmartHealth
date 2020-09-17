package com.mysasse.afyasmart.ui.fragments.diseases.details.measures;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.models.DiseaseMeasure;
import com.mysasse.afyasmart.data.repositories.MeasuresRepository;

import java.util.List;

public class DiseaseMeasuresViewModel extends ViewModel implements MeasuresRepository.MeasuresTaskListener {

    private MutableLiveData<List<DiseaseMeasure>> _diseaseMeasures = new MutableLiveData<>();
    private MutableLiveData<Exception> _exception = new MutableLiveData<>();

    public DiseaseMeasuresViewModel(Disease disease) {

        MeasuresRepository measuresRepository = new MeasuresRepository(this);
        measuresRepository.getDiseaseMeasures(disease);
    }

    public LiveData<List<DiseaseMeasure>> getDiseaseMeasures() {
        return _diseaseMeasures;
    }

    public LiveData<Exception> getException() {
        return _exception;
    }

    @Override
    public void onBrowse(List<DiseaseMeasure> diseaseMeasures) {
        _diseaseMeasures.setValue(diseaseMeasures);
    }

    @Override
    public void onError(Exception e) {
        _exception.setValue(e);
    }
}