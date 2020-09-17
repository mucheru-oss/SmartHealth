package com.mysasse.afyasmart.ui.fragments.notifications;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Notification;
import com.mysasse.afyasmart.utils.UIHelpers;

public class NotificationsFragment extends Fragment implements NotificationsAdapter.NotificationClickListener {

    private RecyclerView notificationsRecyclerView;
    private NotificationsViewModel mViewModel;

    private NavController mNavController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNavController = Navigation.findNavController(view);

        //Register the necessary views
        notificationsRecyclerView = view.findViewById(R.id.notification_recycler_view);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsRecyclerView.setHasFixedSize(true);

        notificationsRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        mViewModel.getAllNotifications().observe(getViewLifecycleOwner(), notifications -> {
            NotificationsAdapter adapter = new NotificationsAdapter(this, notifications);
            notificationsRecyclerView.setAdapter(adapter);
        });

    }

    @Override
    public void onClick(Notification notification) {
        CharSequence[] items = new CharSequence[]{"Switch User Role", "Delete notification"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:

                    NotificationsFragmentDirections.ActionNotificationsFragmentToSwitchRoleFragment action = NotificationsFragmentDirections.actionNotificationsFragmentToSwitchRoleFragment(notification.getUserId());
                    mNavController.navigate(action);

                    break;
                case 1:

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                    alertDialog.setTitle("Deleting a notification");
                    alertDialog.setMessage("Are sure you want to delete the notification " + notification.getTitle());

                    alertDialog.setPositiveButton("Sure", ((dialog1, which1) -> mViewModel.deleteNotification(notification)));
                    alertDialog.setNegativeButton("Cancel", ((dialog1, which1) -> UIHelpers.toast("Operation cancelled")));
                    alertDialog.show();


                    break;
                default:
                    UIHelpers.toast("Select from the available options please");
            }
        });

        builder.show();

    }
}
