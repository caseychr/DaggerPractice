package com.codingwithmitch.daggerpractice;

import android.app.Application;


import com.codingwithmitch.daggerpractice.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * This case spans the life of the application itself. We're using DaggerApplication because it's compatible
 * with more Android versions. Specified in the manifest that this class will span the life of the app/
 */
public class BaseApplication extends DaggerApplication {

    /** returns our AppComponent builder. Now we don't have to Instantiate AppComponent in an onCreate somewhere. */
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
