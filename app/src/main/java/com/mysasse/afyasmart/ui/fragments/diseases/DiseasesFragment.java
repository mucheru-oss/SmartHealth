package com.mysasse.afyasmart.ui.fragments.diseases;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.utils.UIHelpers;

public class DiseasesFragment extends Fragment implements DiseaseAdapter.DiseaseItemListener {

    private RecyclerView diseasesRecyclerView;
    private NavController mNavController;

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore mDb;

    private static final String TAG = "DiseasesFragment";
    private String role;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.diseases_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get the NavigationController and assign to a global field
        mNavController = Navigation.findNavController(view);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDb = FirebaseFirestore.getInstance();

        FloatingActionButton addDiseaseFab = view.findViewById(R.id.add_disease_fab);
        addDiseaseFab.setOnClickListener(v -> mNavController.navigate(R.id.addDiseaseFragment));

        diseasesRecyclerView = view.findViewById(R.id.diseases_recycler_view);
        diseasesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        diseasesRecyclerView.setHasFixedSize(true);
        diseasesRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        mDb.collection(Constants.PROFILES_NODE).document(mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    role = documentSnapshot.getString("role");

                    assert role != null;

                    if (role.equalsIgnoreCase("Doctor"))
                        addDiseaseFab.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> Log.e(TAG, "getting profile", e));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DiseasesViewModel mViewModel = new ViewModelProvider(this).get(DiseasesViewModel.class);

        mViewModel.getDiseases().observe(getViewLifecycleOwner(), diseases -> {
            DiseaseAdapter adapter = new DiseaseAdapter(diseases, this);
            diseasesRecyclerView.setAdapter(adapter);
        });

        mViewModel.getException().observe(getViewLifecycleOwner(), exception -> {
            Log.e(TAG, "onActivityCreated: getting diseases", exception);
            UIHelpers.toast(exception.getLocalizedMessage());
        });
    }


    @Override
    public void onClick(Disease disease) {

        mDb.collection(Constants.PROFILES_NODE).document(mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    String role = documentSnapshot.getString("role");
                    assert role != null;

                    if (role.equalsIgnoreCase("Doctor")) {
                        CharSequence[] alertItems = {"Browse", "Add Measure", "Add Symptom"};

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());

                        alertDialog.setItems(alertItems, ((dialog, which) -> {
                            switch (which) {
                                case 1:
                                    DiseasesFragmentDirections.ActionAddDiseaseMeasure action1 =
                                            DiseasesFragmentDirections.actionAddDiseaseMeasure(disease);
                                    mNavController.navigate(action1);
                                    break;
                                case 2:
                                    DiseasesFragmentDirections.ActionAddDiseaseSymptom action2 =
                                            DiseasesFragmentDirections.actionAddDiseaseSymptom(disease);
                                    mNavController.navigate(action2);
                                    break;
                                default:
                                    navToReadDisease(disease);
                            }
                        }));

                        alertDialog.show();

                    } else {

                        navToReadDisease(disease);

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getting profile", e);
                    UIHelpers.toast("Can't assert whether you are a doctor");
                    navToReadDisease(disease);
                });

    }

    private void navToReadDisease(Disease disease) {
        DiseasesFragmentDirections.ActionViewDiseaseDetails action = DiseasesFragmentDirections.actionViewDiseaseDetails(disease);
        mNavController.navigate(action);
    }
}
