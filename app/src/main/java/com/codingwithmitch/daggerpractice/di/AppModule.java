package com.codingwithmitch.daggerpractice.di;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.codingwithmitch.daggerpractice.R;
import com.codingwithmitch.daggerpractice.models.User;
import com.codingwithmitch.daggerpractice.util.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.core.content.ContextCompat;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Not abstract as we're not using @ContributeAndroidInjector.
 * Here is where we put application level dependencies like Retrofit instance, Glide instance, etc.
 *
 * @Provides annotation is used to declare a dependency.
 * using static help to ensure less memory leaks
 */
@Module
public class AppModule {

    //Example dependency to inject. Not real just to show that this is working.
    @Singleton
    @Provides
    static String someString() {
        return "this is a test string.";
    }

    /**
     * Returning FALSE if we have Application otherwise return TRUE. We see here that we have the application object
     * available to us. Since we used @BindsInstance application() in AppComponent the object is bound at creation and
     * we can access that object inside the modules being used. Otherwise (if we did not @BindsIstance )the application
     * object would return as NULL.
     * @param application
     * @return
     */
    @Singleton
    @Provides
    static boolean getApp(Application application) {
        return application == null;
    }

    /** ***** FOR GLIDE *****
     * Provides Glide object to load the image properly. If for some reason it cannot it loads the white background
     * on .error().
     * @return
     */
    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions.placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }

    /** ***** FOR GLIDE *****
     * Again for Glide it takes in application, which is already provided though the @BindsInstance and
     * the also requestOptions which is pulled from the above @Provides provideRequestOptions(). Here we can call
     * to other @Provides methods within the module
     * @param application
     * @param requestOptions
     * @return
     */
    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application).setDefaultRequestOptions(requestOptions);
    }

    /**
     * Goes ahead and grabs the drawable logo from resources
     * @param application
     * @return
     */
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }

    /**
     * We want the Retrofit instance created to last the span of the app itself which is why it's being placed here in
     * the AppModule.
     * @return
     */
    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Singleton
    @Provides
    @Named("app_user")
    static User someUser(){
        return new User();
    }
}
