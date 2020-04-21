package com.example.fitnessapp.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.fitnessapp.LogActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.user.User;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private User user;
    private Button btnLogout;
    private Button btnUpdate;

    private Switch mealNotificationSwith;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        user = (User) getArguments().getSerializable(KeysBundle.USER_DATA_TO_FRAGMENT);
        System.out.println("Setting" + user);

        btnLogout = v.findViewById(R.id.settingFregment_btn_logout);
        btnUpdate = v.findViewById(R.id.btn_update);
        mealNotificationSwith = v.findViewById(R.id.meals_notification);

        if (getSharedPrefreranceMealNotification() == 1){
            mealNotificationSwith.setChecked(true);
        } else {
            mealNotificationSwith.setChecked(false);
        }


        btnLogout.setOnClickListener(btm->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LogActivity.class));
            getActivity().onBackPressed();
        });

        mealNotificationSwith.setOnClickListener(btn->{

            if (mealNotificationSwith.isChecked()){

                sharedPrefreranceMealNotification(1);

            }else {

                sharedPrefreranceMealNotification(0);
            }

        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void sharedPrefreranceMealNotification(int onOrOff){

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KeysSharedPrefercence.DIET_NOTIFICATION_SWITCH, onOrOff);
        editor.apply();


    }

    private int getSharedPrefreranceMealNotification(){

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KeysSharedPrefercence.DIET_NOTIFICATION_SWITCH, 1);


    }

}
