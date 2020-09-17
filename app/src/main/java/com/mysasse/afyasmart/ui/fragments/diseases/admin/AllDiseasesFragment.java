package com.mysasse.afyasmart.ui.fragments.diseases.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.utils.UIHelpers;

public class AllDiseasesFragment extends Fragment implements AllDiseasesAdapter.DiseaseItemListener {

    private RecyclerView allDiseasesRecyclerView;

    public AllDiseasesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_diseases_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Register the views
        allDiseasesRecyclerView = view.findViewById(R.id.all_diseases_recycler_view);
        allDiseasesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        allDiseasesRecyclerView.setHasFixedSize(true);

        allDiseasesRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AllDiseasesViewModel mViewModel = new ViewModelProvider(this).get(AllDiseasesViewModel.class);

        mViewModel.getAllDiseases().observe(getViewLifecycleOwner(), diseases -> {
            AllDiseasesAdapter adapter = new AllDiseasesAdapter(diseases, this);
            allDiseasesRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onItemClick(Disease disease) {

    }

    @Override
    public void onMoreOptionClicked(View view, Disease disease) {
        //Show a popup menu there
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();

        menuInflater.inflate(R.menu.admin_disease_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete_option) {
                UIHelpers.toast("Should delete the disease" + disease.getName());
            }

            return true;
        });

        popupMenu.show();
    }
}
