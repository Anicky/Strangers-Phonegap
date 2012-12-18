package fr.utt.if26.strangersPhonegap;

import android.util.Log;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Plugin Android pour stocker des données en local
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class StockageLocal extends CordovaPlugin {

    private final String ACTION_GET = "get";
    private final String ACTION_SET = "set";
    private final String TAG = "Plugin : StockageLocal";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Plugin start");
        boolean resultat = true;

        if (action.equals(ACTION_GET)) {
            Log.d(TAG, "Action : Get");
            // @todo

            try {
                byte[] donnees = "account1.port=23\naccount2.port=64".getBytes();

                // crypter
                byte[] donneesCryptees = crypter(Strangers.getKey(), donnees);

                // decrypter
                byte[] donneesDecryptees = decrypter(Strangers.getKey(), donneesCryptees);
                Log.d(TAG, new String(donneesDecryptees));
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }

        } else if (action.equals(ACTION_SET)) {
            Log.d(TAG, "Action : Set");
            // @todo
        }
        Log.d(TAG, "Plugin stop");
        return resultat;
    }

    private byte[] crypter(byte[] raw, byte[] clear) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(raw, "AES"));
        return cipher.doFinal(clear);
    }

    private byte[] decrypter(byte[] raw, byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, "AES"));
        return cipher.doFinal(encrypted);
    }
}