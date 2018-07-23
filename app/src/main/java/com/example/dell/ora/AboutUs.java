package com.example.dell.ora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutUs extends Fragment{
    CalendarView cal;
        public AboutUs() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View x = inflater.inflate(R.layout.fragment_about_us, container, false);

            ImageView image = x.findViewById(R.id.imageView);
            Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.RESTART); // Repeat animation
            // infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
            // end so the button will
            // fade back in
            image.startAnimation(animation);

            return x;
        }



}

