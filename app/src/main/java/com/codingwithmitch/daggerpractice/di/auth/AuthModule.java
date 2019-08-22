package com.codingwithmitch.daggerpractice.di.auth;

import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.network.auth.AuthApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    /**
     * We can access the retrofit instance inside the AuthModule because this Module is inside the Subcomponent of
     * the AppComponent; and of course we instantiated the Retrofit instance in the AppComponent.
     * @param retrofit
     * @return
     */
    @AuthScope
    @Provides
    static AuthApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

    /**
     * @Named -> If an Object is returned more than once Dagger does not like that and throws an error. But we can user
     * @Named("somestring") to differentiate between the methods and inject these different objects. Note that when actually
     * injecting into a class still use the @Named("somestring") to specify which object you want to inject.
     * @return
     */
    @Singleton
    @Provides
    @Named("auth_user")
    static User someUser(){
        return new User();
    }
}
