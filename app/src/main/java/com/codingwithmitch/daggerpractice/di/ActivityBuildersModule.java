package com.codingwithmitch.daggerpractice.di;

import com.codingwithmitch.daggerpractice.di.auth.AuthScope;
import com.codingwithmitch.daggerpractice.di.main.MainFragmentBuildersModule;
import com.codingwithmitch.daggerpractice.di.main.MainModule;
import com.codingwithmitch.daggerpractice.di.main.MainScope;
import com.codingwithmitch.daggerpractice.di.main.MainViewModelsModule;
import com.codingwithmitch.daggerpractice.network.main.MainApi;
import com.codingwithmitch.daggerpractice.ui.main.MainActivity;
import com.codingwithmitch.daggerpractice.di.auth.AuthModule;
import com.codingwithmitch.daggerpractice.di.auth.AuthViewModelsModule;
import com.codingwithmitch.daggerpractice.ui.auth.AuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Modules (relatively speaking) are a place for specified dependencies to live and then be added to components.
 *
 * This is abstract because we're using @ContributeAndroidInjector. Now no need to specify inject() into the Activity
 * itself. It's already happening here. Just need to @Inject some field. Also important note whatever Activity we
 * @Inject into they must extend DaggerAppCompatActivity. Otherwise you have to call AndroidInjection.inject() in
 * onCreate().
 *
 * For architectural reason we're only putting Activity injection declarations in here.
 *
 * NOTE that whatever Activities have to use this must be instantiated here
 */
@Module
public abstract class ActivityBuildersModule {

    /** Add the ViewModelsModule for whatever ViewModel you're injecting into the view as a modules dependency here.
     *  Technically speaking this is creating a subcomponent. Now we add AuthModule to be able to access the Retrofit instance
     *
     *  Using @ContributesAndroidInjector we effecively create a subcomponent and can specify modules which are only
     *  accessible through that subcomponent like AuthViewModelModule and AuthModule here.
     *  */
    @AuthScope
    @ContributesAndroidInjector(modules = {AuthViewModelsModule.class, AuthModule.class})
    abstract AuthActivity contributeAuthActivity();

    /**
     * The Fragment Module is ulltimately a submodule of ActivityBuildersModule
     * @return
     */
    @MainScope
    @ContributesAndroidInjector(modules = {MainFragmentBuildersModule.class,
            MainViewModelsModule.class, MainModule.class})
    abstract MainActivity contributeMainActivituy();
}
