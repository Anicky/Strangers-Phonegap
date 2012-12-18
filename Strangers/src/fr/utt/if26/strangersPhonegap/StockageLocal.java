package fr.utt.if26.strangersPhonegap;

import android.content.Context;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
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

    private byte[] key = null;
    private final String ACTION_GET = "get";
    private final String ACTION_SET = "set";
    private final String TAG = "Plugin : StockageLocal";
    private final String SETTINGS_FILE = "settings.dat";

    private byte[] getKey() {
        if (key == null) {
            try {
                createKey();
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
        return key;
    }

    private void createKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("06437129437961346794313579".getBytes("UTF8"));
        kgen.init(sr);
        key = kgen.generateKey().getEncoded();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Plugin start");
        boolean resultat = true;

        if (action.equals(ACTION_GET)) {
            Log.d(TAG, "Action : Get");
            try {
                String donnees = read();
                Log.d(TAG, donnees);
                callbackContext.success(donnees);
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        } else if (action.equals(ACTION_SET)) {
            Log.d(TAG, "Action : Set");
            String email = args.getString(0);
            String user = args.getString(1);
            String pass = args.getString(2);
            String server = args.getString(3);
            int port = args.getInt(4);
            boolean ssl = args.getBoolean(5);
            String donnees = "account1.email=" + email + "\n"
                    + "account1.user=" + user + "\n";
            write(donnees);
        }
        Log.d(TAG, "Plugin stop");
        return resultat;
    }

    private void write(String donnees) {
        FileOutputStream fichier = null;
        OutputStreamWriter sortie = null;

        Log.d(TAG, donnees);
        try {
            Log.d(TAG, decrypter(donnees));
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        try {
            fichier = cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_APPEND);
            sortie = new OutputStreamWriter(fichier);
            sortie.write(crypter(donnees));
            sortie.flush();
            Log.d(TAG, "Donnees sauvegardees");
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        } finally {
            try {
                sortie.close();
                fichier.close();
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }

    private String read() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        char[] buffer = new char[255];
        String donnees = null;
        try {
            InputStreamReader entree = new InputStreamReader(cordova.getContext().openFileInput(SETTINGS_FILE));
            entree.read(buffer);
            donnees = new String(buffer);
            Log.d(TAG, decrypter(donnees));
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return decrypter(donnees);
    }

    private String crypter(String chaine) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(), "AES"));
        return new String(cipher.doFinal(chaine.getBytes("UTF8")));
    }

    private String decrypter(String chaine) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(), "AES"));
        return new String(cipher.doFinal(chaine.getBytes("UTF8")));
    }
}