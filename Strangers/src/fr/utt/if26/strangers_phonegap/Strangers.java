package fr.utt.if26.strangers_phonegap;

import android.os.Bundle;
import org.apache.cordova.DroidGap;

public class Strangers extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
