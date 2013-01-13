package fr.utt.if26.strangersPhonegap;

import android.content.Context;
import android.util.Log;
import fr.utt.if26.strangersPhonegap.outils.Cryptage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
    private final String ACTION_DELETE = "delete";
    private final String TAG = "Plugin : StockageLocal";
    private final String SETTINGS_FILE = "settings.properties";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Plugin start");
        boolean resultat = true;
        if (action.equals(ACTION_GET)) {
            Log.d(TAG, "Action : Get");
            try {
                if (args.getString(0).isEmpty()) {
                    JSONArray comptes = get();
                    Log.d(TAG, "Returns :  " + comptes);
                    callbackContext.success(comptes);
                } else {
                    JSONObject compte = get(args.getInt(0));
                    Log.d(TAG, "Returns :  " + compte);
                    callbackContext.success(compte);
                }
            } catch (FileNotFoundException ex) {
                Log.d(TAG, ex.getLocalizedMessage());
                callbackContext.success("");
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                callbackContext.error(0);
            }
        } else if (action.equals(ACTION_SET)) {
            Log.d(TAG, "Action : Set");
            try {
                set(args);
                Log.d(TAG, "Donnees sauvegardees");
                callbackContext.success();
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                callbackContext.error(0);
            }
        } else if (action.equals(ACTION_DELETE)) {
            Log.d(TAG, "Action : Delete");
            try {
                delete(args.getInt(0));
                callbackContext.success();
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                callbackContext.error(0);
            }
            Log.d(TAG, "Donnees supprimees");
        }
        Log.d(TAG, "Plugin stop");
        return resultat;
    }

    private void set(JSONArray args) throws JSONException, IOException, GeneralSecurityException {
        int nombre_comptes = args.length();
        Properties proprietes = new Properties();
        proprietes.setProperty("accounts", Cryptage.crypter(String.valueOf(nombre_comptes)));
        for (int i = 0; i < nombre_comptes; i++) {
            set(proprietes, args.getJSONObject(i), i);
        }
        proprietes.store(cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE), "Configuration Strangers");
    }

    private void set(Properties proprietes, JSONObject compte, int i) throws GeneralSecurityException, JSONException {
        proprietes.setProperty("account_" + i + ".mail", Cryptage.crypter(compte.getString("mail")));
        proprietes.setProperty("account_" + i + ".user", Cryptage.crypter(compte.getString("user")));
        proprietes.setProperty("account_" + i + ".pass", Cryptage.crypter(compte.getString("pass")));
        proprietes.setProperty("account_" + i + ".serv", Cryptage.crypter(compte.getString("serv")));
        proprietes.setProperty("account_" + i + ".port", Cryptage.crypter(compte.getString("port")));
        proprietes.setProperty("account_" + i + ".ssl", Cryptage.crypter(compte.getString("ssl")));
    }

    private JSONArray get() throws IOException, JSONException, GeneralSecurityException {
        JSONArray comptes = new JSONArray();
        Properties proprietes = getPropertiesInput();
        int nombre_comptes = Integer.valueOf(Cryptage.decrypter(proprietes.getProperty("accounts", "0")));
        for (int i = 0; i < nombre_comptes; i++) {
            comptes.put(i, get(proprietes, i));
        }
        return comptes;
    }

    private JSONObject get(int i) throws IOException, JSONException, GeneralSecurityException {
        return get(getPropertiesInput(), i);
    }

    private JSONObject get(Properties proprietes, int i) throws IOException, JSONException, GeneralSecurityException {
        JSONObject compte = new JSONObject();
        compte.put("mail", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".mail", "")));
        compte.put("user", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".user", "")));
        compte.put("pass", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".pass", "")));
        compte.put("serv", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".serv", "")));
        compte.put("port", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".port", "")));
        compte.put("ssl", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".ssl", "")));
        return compte;
    }

    private void delete(int i) throws GeneralSecurityException, JSONException, IOException {
        Properties proprietes = getPropertiesInput();
        delete(proprietes, i);
        int nombre_comptes = Integer.valueOf(Cryptage.decrypter(proprietes.getProperty("accounts", "0")));
        nombre_comptes--;
        proprietes.setProperty("accounts", Cryptage.crypter(String.valueOf(nombre_comptes)));
        proprietes.store(cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_APPEND), "Configuration Strangers");
    }

    private void delete(Properties proprietes, int i) throws GeneralSecurityException, JSONException {
        proprietes.remove("account_" + i + ".mail");
        proprietes.remove("account_" + i + ".user");
        proprietes.remove("account_" + i + ".pass");
        proprietes.remove("account_" + i + ".serv");
        proprietes.remove("account_" + i + ".port");
        proprietes.remove("account_" + i + ".ssl");
    }

    private Properties getPropertiesInput() throws IOException {
        Properties proprietes = new Properties();
        proprietes.load(cordova.getContext().openFileInput(SETTINGS_FILE));
        return proprietes;
    }
}