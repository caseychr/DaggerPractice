package com.codingwithmitch.daggerpractice.di.auth;

import com.codingwithmitch.daggerpractice.di.ViewModelKey;
import com.codingwithmitch.daggerpractice.ui.auth.AuthViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * For injecting viewmodel into AuthActivity.
 */
@Module
public abstract class AuthViewModelsModule {

    /**
     * This ends up creating this dependency behind the scenes.
     * @param viewModel
     * @return
     */
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);
}
