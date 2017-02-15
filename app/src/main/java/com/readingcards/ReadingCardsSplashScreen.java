package com.readingcards;

import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

/**
 * Created by gabrielaradu on 08/02/2017.
 */
public class ReadingCardsSplashScreen extends WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorAccent)
                .page(new ParallaxPage(R.layout.splash_screen_one_parallax,
                        "Make your own notes",
                        "Are you preparing to pitch your startup to somebody? Add your notes and keep them safe on your phone.")
                        .lastParallaxFactor(2f)
                        .background(R.color.step_one)
                )
                .page(new ParallaxPage(R.layout.splash_screen_two_parallax,
                        "And...?",
                        "Make collections from your notes and practice reading them with a timer you set.")
                        .lastParallaxFactor(2f)
                        .background(R.color.step_two)
                )
                .page(new ParallaxPage(R.layout.splash_screen_three_parallax,
                        "Want more features?",
                        "Email the developer: gabrielaradu.developer@gmail.com")
                        .lastParallaxFactor(2f)
                        .background(R.color.step_three)
                )
                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
}