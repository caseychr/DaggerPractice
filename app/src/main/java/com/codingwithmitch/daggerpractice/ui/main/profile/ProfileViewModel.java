package com.codingwithmitch.daggerpractice.ui.main.profile;

import android.util.Log;

import com.codingwithmitch.daggerpractice.SessionManager;
import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.ui.auth.AuthResource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";

    private final SessionManager mSessionManager;

    @Inject
    public ProfileViewModel(SessionManager sessionManager) {
        mSessionManager = sessionManager;
        Log.i(TAG, "ProfileViewModel: viewmodel is ready...");
    }

    /**
     * Now we're doing this in the ViewModel instead of MainActivity or BaseActivity. Why??? Because this would break
     * MVVM Architecture. Here we can observe the Data and make it Life cycle aware so it is immune to UI and Config changes.
     * Don't just try to extract the values but always try and observe the values.
     * @return
     */
    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return mSessionManager.getAuthUser();
    }
}
