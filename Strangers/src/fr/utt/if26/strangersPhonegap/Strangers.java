package fr.utt.if26.strangersPhonegap;

import android.os.Bundle;
import org.apache.cordova.DroidGap;

/**
 * Application Strangers
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Strangers extends DroidGap { 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
