package fr.utt.if26.strangersPhonegap;

import android.content.Context;
import android.util.Log;
import fr.utt.if26.strangersPhonegap.outils.Cryptage;
import java.io.IOException;
import java.util.Properties;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Plugin Android pour stocker des données en local
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class StockageLocal extends CordovaPlugin {

    private final String ACTION_GET = "get";
    private final String ACTION_SET = "set";
    private final String TAG = "Plugin : StockageLocal";
    private final String SETTINGS_FILE = "settings.properties";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Plugin start");
        boolean resultat = true;
        if (action.equals(ACTION_GET)) {
            Log.d(TAG, "Action : Get");
            try {
                JSONArray comptes = get();
                Log.d(TAG, "Returns :  " + comptes);
                callbackContext.success(comptes);
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        } else if (action.equals(ACTION_SET)) {
            Log.d(TAG, "Action : Set");
            try {
                set(args);
                
                String test = "Blabla\nCeci est un test ! : \naccount.test=nop@true.fr";
                Log.d(TAG, test);
                String testcrypte = Cryptage.crypter(test);
                Log.d(TAG, testcrypte);
                String testdecrypte = Cryptage.decrypter(testcrypte);
                Log.d(TAG, testdecrypte);
                
                Log.d(TAG, "Donnees sauvegardees");
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
        Log.d(TAG, "Plugin stop");
        return resultat;
    }

    private void set(JSONArray args) throws JSONException, IOException {
        int nombre_comptes = args.length();
        Properties proprietes = new Properties();
        proprietes.setProperty("accouns", String.valueOf(nombre_comptes));
        for (int i = 0; i < nombre_comptes; i++) {
            JSONObject compte = args.getJSONObject(i);
            proprietes.setProperty("account_" + i + ".email", compte.getString("email"));
            proprietes.setProperty("account_" + i + ".user", compte.getString("user"));
            proprietes.setProperty("account_" + i + ".pass", compte.getString("pass"));
            proprietes.setProperty("account_" + i + ".server", compte.getString("server"));
            proprietes.setProperty("account_" + i + ".port", compte.getString("port"));
            proprietes.setProperty("account_" + i + ".ssl", compte.getString("ssl"));
        }
        proprietes.store(cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE), "Configuration Strangers");
    }

    private JSONArray get() throws IOException, JSONException {
        JSONArray comptes = new JSONArray();
        Properties proprietes = new Properties();
        proprietes.load(cordova.getContext().openFileInput(SETTINGS_FILE));
        int nombre_comptes = Integer.valueOf(proprietes.getProperty("accouns", "0"));
        for (int i = 0; i < nombre_comptes; i++) {
            JSONObject compte = new JSONObject();
            compte.put("email", proprietes.getProperty("account_" + i + ".email", ""));
            compte.put("user", proprietes.getProperty("account_" + i + ".user", ""));
            compte.put("pass", proprietes.getProperty("account_" + i + ".pass", ""));
            compte.put("server", proprietes.getProperty("account_" + i + ".server", ""));
            compte.put("port", proprietes.getProperty("account_" + i + ".port", ""));
            compte.put("ssl", proprietes.getProperty("account_" + i + ".ssl", ""));
            comptes.put(i, compte);
        }
        return comptes;
    }
}