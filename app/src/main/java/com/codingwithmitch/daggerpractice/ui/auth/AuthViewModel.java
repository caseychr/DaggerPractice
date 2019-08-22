package com.codingwithmitch.daggerpractice.ui.auth;

import android.util.Log;

import com.codingwithmitch.daggerpractice.SessionManager;
import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.network.auth.AuthApi;

import javax.inject.Inject;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";

    private final AuthApi mAuthApi;

    /**
     * We have this authUser which is a Mediator of LiveData which we want to observe from the UI. We can observe that
     * through the observeUser() method which returns a LiveData object. We observe any changes through the LiveData object.
     *
     * Removing this now and injecting the session manager to handle authUser stuff.
     */
    // using the AuthResource wrapper class to wrap User
    //private MediatorLiveData<AuthResource<User>> authUser = new MediatorLiveData<>();

    private SessionManager mSessionManager;


    /**
     * This is a requirement for how we use multibindings
     */
    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.mAuthApi = authApi;
        this.mSessionManager = sessionManager;
        Log.i(TAG, "AuthViewModel: viewmodel injection is working...");

        /** Getting retrofit api and using RxJava Observable and Schedulers to do on other thread */
        authApi.getUser(1).toObservable().subscribeOn(Schedulers.io()).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(User user) {
                Log.i(TAG, "onNext: "+user.getEmail());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Utilizes the queryUserId() below and authenticateWithId from SessionManger to authenticate user
     * @param userId
     */
    public void authenticateWithId(int userId) {
        /**
         * This tells the UI that a request is attempting to be made.
         */
        Log.i(TAG, "authenticateWithId: attempting to login");
        mSessionManager.authenticateWithId(queryUserId(userId));
    }

    /**
     * Only responsible for querying and returning the userId and doing RxJava stuff.
     * @param userId
     * @return
     */
    private LiveData<AuthResource<User>> queryUserId(int userId) {
        return LiveDataReactiveStreams
                .fromPublisher(
                        /** We're still returning a Flowable here. We're also using 2 RxJava operators*/
                        mAuthApi.getUser(userId)
                                // Instead of calling onError, error just happens
                                .onErrorReturn(new Function<Throwable, User>() {
                                    @Override
                                    public User apply(Throwable throwable) throws Exception {
                                        User errorUser = new User();
                                        errorUser.setId(-1);
                                        return errorUser;
                                    }
                                })
                                .map(new Function<User, AuthResource<User>>() {
                                    @Override
                                    public AuthResource<User> apply(User user) throws Exception {
                                        if(user.getId() == -1) {
                                            return AuthResource.error("Could not authenticate", (User) null);
                                        }
                                        return AuthResource.authenticated(user);
                                    }
                                })
                                .subscribeOn(Schedulers.io()));
    }

    /**
     * Converts the MediatorLiveData to LiveData object
     */
    public LiveData<AuthResource<User>> observeAuthState() {
        return mSessionManager.getAuthUser();
    }
}
