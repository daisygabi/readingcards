package com.readingcards.sharedprefs;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The connection between the provider of dependencies, @Module, and the classes requesting them
 * through @Inject is made using @Component
 */
@Singleton
@Component(modules = {SharedPreferenceModule.class})
public interface SharedPreferenceComponent {
    public SharedPreferences getSharedPreference();
}
