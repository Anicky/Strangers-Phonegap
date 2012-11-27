package fr.utt.if26.td05;

import android.os.Bundle;
import org.apache.cordova.DroidGap;

public class PhonegapTest extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
