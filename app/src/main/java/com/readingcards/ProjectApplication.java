package com.readingcards;

import android.app.Application;

import com.readingcards.data.domain.CardCollection;
import com.readingcards.data.domain.Note;
import com.readingcards.util.FontsOverride;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

/**
 * Created by gabrielaradu on 26/12/2016.
 */

public class ProjectApplication extends Application {

    @Inject
    @Override
    public void onCreate() {
        super.onCreate();

        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(CardCollection.class);
        classes.add(Note.class);
        config.setClasses(classes);
        RushCore.initialize(config);

        // Add font
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/Roboto-Light.ttf");
    }

}
