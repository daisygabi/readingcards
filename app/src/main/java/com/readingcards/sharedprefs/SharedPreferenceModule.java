package com.readingcards.sharedprefs;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Signal to Dagger to search within the available methods for possible instance providers
 */
@Module
public class SharedPreferenceModule {
    Application application;

    public SharedPreferenceModule(Application mApplication) {
        this.application = mApplication;
    }

    //Application is required for shared preference
    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    //Application context is injected using the above menthd
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
