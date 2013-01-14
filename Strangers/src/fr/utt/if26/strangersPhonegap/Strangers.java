package fr.utt.if26.strangersPhonegap;

import android.os.Bundle;
import java.io.InputStream;
import org.apache.cordova.DroidGap;

/**
 * Application Strangers
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Strangers extends DroidGap {

    private static InputStream keystore = null;
    
    public static InputStream getKeyStore() {
        return keystore;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        keystore = this.getResources().openRawResource(R.raw.keystore);
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
