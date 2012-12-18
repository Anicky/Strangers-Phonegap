package fr.utt.if26.strangersPhonegap;

import android.os.Bundle;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import org.apache.cordova.DroidGap;

/**
 * Application Strangers
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Strangers extends DroidGap {

    private static byte[] key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            createKey();
        } catch (NoSuchAlgorithmException ex) {
            Log.e("Strangers", ex.getLocalizedMessage());
        }
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }

    private void createKey() throws NoSuchAlgorithmException {
        byte[] keyStart = "rtru054687eryeryZETZE".getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        key = kgen.generateKey().getEncoded();
    }

    public static byte[] getKey() {
        return key;
    }
}
