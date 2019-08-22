package com.codingwithmitch.daggerpractice.di;

import com.codingwithmitch.daggerpractice.viewmodels.ViewModelProviderFactory;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    /**
     * Now here we could have also just done this:
     * @Provides
     * static ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory) {
     *     return modelProviderFactory;
     * }
     * The only difference here is that this is slightly more efficient. Using @Provides would accomplish the same thing.
     * All we want here to provide an instance of the ViewModelProviderFactory.
     * @param modelProviderFactory
     * @return
     */
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);
}
