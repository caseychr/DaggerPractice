package com.codingwithmitch.daggerpractice;

import android.util.Log;

import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.ui.auth.AuthResource;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

@Singleton
public class SessionManager {
    private static final String TAG = "SessionManager";

    /**
     * We're using MediatorLiveData here so we can observe the authenticated user from any class that we inject
     * the SessionManager into. So in the case that we logout we can react to that change immediately. Now with MediatorLiveData
     * is not holding just a raw object but an observable object
     */
    private MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {
    }

    /**
     * Similar to AuthViewModel where we authenticate the cachedUser inside the SessionManager here.
     * @param source
     */
    public void authenticateWithId(final LiveData<AuthResource<User>> source) {
        if(cachedUser != null) {
            cachedUser.setValue(AuthResource.loading((User) null));
            cachedUser.addSource(source, new Observer<AuthResource<User>>() {
                @Override
                public void onChanged(AuthResource<User> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);
                }
            });
        } else {
            Log.i(TAG, "authenticateWithId: previous session detected. Retrieving user from cache. ");
        }

    }

    /**
     * Removes cached user object from MediatorLiveData
     */
    public void logout() {
        Log.i(TAG, "logout: logging out...");
        cachedUser.setValue(AuthResource.<User>logout());
    }

    public LiveData<AuthResource<User>> getAuthUser() {
        return cachedUser;
    }


}
