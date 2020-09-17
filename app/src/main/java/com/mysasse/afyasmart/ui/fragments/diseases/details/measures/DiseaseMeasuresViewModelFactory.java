package com.mysasse.afyasmart.ui.fragments.diseases.details.measures;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mysasse.afyasmart.data.models.Disease;

import java.lang.reflect.InvocationTargetException;

public class DiseaseMeasuresViewModelFactory implements ViewModelProvider.Factory {

    private Disease disease;

    public DiseaseMeasuresViewModelFactory(Disease disease) {
        this.disease = disease;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        try {
            return modelClass.getConstructor(Disease.class).newInstance(disease);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}
