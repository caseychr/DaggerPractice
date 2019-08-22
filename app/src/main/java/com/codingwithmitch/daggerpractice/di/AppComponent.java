package com.codingwithmitch.daggerpractice.di;

import android.app.Application;

import com.codingwithmitch.daggerpractice.BaseApplication;
import com.codingwithmitch.daggerpractice.SessionManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is our Component interface the spans the life of the app. This tells the Dagger Generator that this is
 * going to be a Component class.
 *
 * Extending by AndroidInjector<BaseApplication> takes code generator a step further. We would typically use
 * void inject(BaseApplication) but instead we're telling Dagger that we're injecting AppComponent into
 * BaseApplication and that BaseApplication will be a client of AppComponent and AppComponent is the service.
 *
 * IMPORTANT: if using AndroidInjector<BaseApplication> you MUST include AndroidSupportInjectionModule as a module.
 * You only need to include it once in the AppComponent.
 *
 * |||||*****||||| EVERY DAGGER 2 INTERACTION IS SOME FORM OF CLIENT/SERVER |||||*****|||||
 *A
 * specifying modules here tells the component, "here's all the modules you need to do what I need you to do"
 *
 * Purpose of @Singleton is to let Dagger know that the annotated entity should persist and be kept in memory. This
 * component is to be scoped to an application-wide singleton. We are telling Dagger that the AppComponent OWNS the
 * @Singleton scope and then any dependencies that are associated with AppComponent need to be annotated with the
 * @Singleton annotation as well.
 *
 * SCOPE -> Scopes are a way to give a name to the lifetime of a component. If a component is has a certain scope, the
 * component and it's dependencies live for the span of that scope. Once they're done you do cleanup on the dependencies.
 */
@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBuildersModule.class,
 AppModule.class, ViewModelFactoryModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    /** This is the usual way of injecting the AppComponent(Server) into AuthActivity(Client).
     *  Now that we're using Dagger Support libraries this has been simplified. */
    //void inject(AuthActivity authActivity);

    /**
     * this is all we need to do. Now sessionManager is kept alive through the scope of the app component which is the
     * life of the app itself. Since SessionManager is a Singleton class that keeps everything we need for user auth
     * it can all be managed through this object.
     * @return
     */
    SessionManager sessionManager();


    /**
     * Here we are overriding the regular Builder and return a type of AppComponent.
     */
    @Component.Builder
    interface Builder {

        /** Not necessarily mandatory. Bind a particular instance of an object to the component at the time
         *  it's construction. It makes sense to use the @BindsInstance here and when implementing it onto the
         *  application since we want the AppComponent to live throughout the instance of the Application.
         */
        @BindsInstance
        Builder application(Application application);

        /** Should happen every time (mandatory) */
        AppComponent build();
    }
}
