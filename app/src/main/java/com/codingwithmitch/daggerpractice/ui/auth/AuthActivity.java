package com.codingwithmitch.daggerpractice.ui.auth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigator;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.codingwithmitch.daggerpractice.ui.main.MainActivity;
import com.codingwithmitch.daggerpractice.R;
import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;
import javax.inject.Named;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AuthActivity";

    @Inject String someString;
    @Inject boolean isAppNull;

    private AuthViewModel mAuthViewModel;
    private EditText mUserId;
    private ProgressBar mProgressBar;
    @Inject ViewModelProviderFactory mProviderFactory;

    @Inject Drawable logo;
    @Inject RequestManager mRequestManager;

    @Inject @Named("app_user") User mUser1;
    @Inject @Named("auth_user") User mUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mUserId = findViewById(R.id.user_id_input);
        findViewById(R.id.login_button).setOnClickListener(this);
        mProgressBar = findViewById(R.id.progress_bar);

        mAuthViewModel = ViewModelProviders.of(this, mProviderFactory).get(AuthViewModel.class);

        setLogo();
        subscribeObservers();
        Log.i(TAG, "onCreate: "+someString);
        Log.i(TAG, "onCreate: is app null "+isAppNull); // this returns false indicating the application is not null
        Log.i(TAG, "onCreate: "+mUser1);
        Log.i(TAG, "onCreate: "+mUser2);
    }

    /**
     * Calls the observeUser method we created in the viewmodel, instantiates the owner of the lifecycle, which is AuthActivity
     * and then creates a new Observer to catch changes in the LiveData.
     *
     * Now we're observing the user information completely through the SessionManager now via the ViewModel.
     */
    private void subscribeObservers() {
        mAuthViewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if(userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case LOADING: {
                            showProgressBar(true);
                            break;
                        }
                        case AUTHENTICATED:{
                            showProgressBar(false);
                            Log.i(TAG, "onChanged: LOGIN SUCCESS: "+userAuthResource.data.getEmail());
                            onLoginSuccess();
                            break;
                        }
                        case NOT_AUTHENTICATED: {
                            showProgressBar(false);
                            break;
                        }
                        case ERROR: {
                            showProgressBar(false);
                            Toast.makeText(AuthActivity.this, userAuthResource.message
                                    +"\nDid you enter a number between 1 & 10?", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        if(isVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setLogo() {
        mRequestManager.load(logo).into((ImageView)findViewById(R.id.login_logo));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:{
                attemptLogin();
                break;
            }
        }
    }

    private void attemptLogin() {
        if(TextUtils.isEmpty(mUserId.getText().toString())) {
            return;
        }
        mAuthViewModel.authenticateWithId(Integer.parseInt(mUserId.getText().toString()));
    }
}
