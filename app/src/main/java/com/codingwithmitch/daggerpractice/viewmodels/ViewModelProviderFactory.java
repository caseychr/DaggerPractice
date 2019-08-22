package com.codingwithmitch.daggerpractice.viewmodels;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * SINCE DAGGER 2 and VIEWMODELS don't get along well, as of now, we have to create this Factory as a workaround
 * so we can inject dependencies into the viewmodel. Since we cannot pass parameters through any constructor for any
 * class that extends ViewModel we have to create this custom viewmodel factory which is why we have this. The inability
 * to pass parameters through the constructor is the root issue here.
 *
 * No need to know exactly what's going on here. This is a widely used workaround.
 */

public class ViewModelProviderFactory implements ViewModelProvider.Factory {

    private static final String TAG = "ViewModelProviderFactor";

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    /** Notice here the parameter is a Map<(Some class that extends ViewModel), (Provider of ViewModel)>*/
    @Inject
    public ViewModelProviderFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) { // if the viewmodel has not been created

            // loop through the allowable keys (aka allowed classes with the @ViewModelKey)
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {

                // if it's allowed, set the Provider<ViewModel>
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }

        // if this is not one of the allowed keys, throw exception
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        // return the Provider
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
