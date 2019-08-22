package com.codingwithmitch.daggerpractice.ui.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithmitch.daggerpractice.R;
import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.ui.auth.AuthResource;
import com.codingwithmitch.daggerpractice.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

/**
 * We're extending by DaggerFragment to take advantage of some of the imports and boilerplate code to help.
 * Injecting a ViewModel into a Fragment happens just the same as we did in Activity.
 *
 * Fragment lifecycles can be a bit tricky when using Dagger
 */
public class ProfileFragment extends DaggerFragment {
    private static final String TAG = "ProfileFragment";

    private ProfileViewModel mProfileViewModel;
    TextView mEmailTv, mUsernameTv, mWebsiteTv;

    @Inject ViewModelProviderFactory mViewModelProviderFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mEmailTv = view.findViewById(R.id.email);
        mUsernameTv = view.findViewById(R.id.username);
        mWebsiteTv = view.findViewById(R.id.website);
        Toast.makeText(getActivity(), TAG, Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "ProfileFragment was created.");
        mProfileViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(ProfileViewModel.class);
        subscribeObservers();
    }

    /**
     * Since fragments have their own lifecycle we need to remove any previous observers before creating new ones.
     * Very important to do this otherwise it'll cause issues.
     */
    private void subscribeObservers() {
        mProfileViewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());
        mProfileViewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case AUTHENTICATED: {
                            setUserDetails(userAuthResource.data);
                            break;
                        }
                        case ERROR: {
                            setErrorDetails(userAuthResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setErrorDetails(String message) {
        mEmailTv.setText(message);
        mUsernameTv.setText("ERROR");
        mWebsiteTv.setText("ERROR");
    }

    private void setUserDetails(User data) {
        mEmailTv.setText(data.getEmail());
        mUsernameTv.setText(data.getUsername());
        mWebsiteTv.setText(data.getWebsite());
    }
}
