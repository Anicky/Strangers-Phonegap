package fr.utt.if26.strangersPhonegap.plugins;

import android.content.Context;
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
    private final String SETTINGS_FILE = "settings.properties";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean resultat = true;
        if (action.equals(ACTION_GET)) {
            try {
                if (args.getString(0).isEmpty()) {
                    JSONArray comptes = get();
                    callbackContext.success(comptes);
                } else {
                    JSONObject compte = get(args.getInt(0));
                    callbackContext.success(compte);
                }
            } catch (FileNotFoundException ex) {
                callbackContext.success("");
            } catch (Exception ex) {
                callbackContext.error(0);
            }
        } else if (action.equals(ACTION_SET)) {
            try {
                set(args);
                callbackContext.success();
            } catch (Exception ex) {
                callbackContext.error(0);
            }
        } else if (action.equals(ACTION_DELETE)) {
            try {
                delete(args.getInt(0));
                callbackContext.success();
            } catch (Exception ex) {
                callbackContext.error(0);
            }
        }
        return resultat;
    }

    private void set(JSONArray args) throws JSONException, GeneralSecurityException, IOException {
        Properties proprietes = null;
        int lastId = 0;
        try {
            proprietes = getPropertiesInput();
        } catch (FileNotFoundException ex) {
        }
        if (proprietes != null) {
            String property_lastId = proprietes.getProperty("last_id");

            if (property_lastId != null) {
                lastId = Integer.valueOf(Cryptage.decrypter(property_lastId));
            }
        } else {
            proprietes = new Properties();
        }
        for (int i = 0; i < args.length(); i++) {
            set(proprietes, args.getJSONObject(i), lastId + i + 1);
        }
        lastId += args.length();
        proprietes.setProperty("last_id", Cryptage.crypter(String.valueOf(lastId)));
        proprietes.store(cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE), "Configuration Strangers");
    }

    private void set(Properties proprietes, JSONObject compte, int i) throws GeneralSecurityException, JSONException, IOException {
        String s = compte.getString("id");
        int id_account = i;
        if (!s.isEmpty()) {
            id_account = Integer.valueOf(s);
        }
        proprietes.setProperty("account_" + id_account + ".id", Cryptage.crypter(String.valueOf(id_account)));
        proprietes.setProperty("account_" + id_account + ".mail", Cryptage.crypter(compte.getString("mail")));
        proprietes.setProperty("account_" + id_account + ".user", Cryptage.crypter(compte.getString("user")));
        proprietes.setProperty("account_" + id_account + ".pass", Cryptage.crypter(compte.getString("pass")));
        proprietes.setProperty("account_" + id_account + ".serv", Cryptage.crypter(compte.getString("serv")));
        proprietes.setProperty("account_" + id_account + ".port", Cryptage.crypter(compte.getString("port")));
        proprietes.setProperty("account_" + id_account + ".ssl", Cryptage.crypter(compte.getString("ssl")));
        proprietes.setProperty("account_" + id_account + ".boxes", Cryptage.crypter(compte.getString("boxes")));
    }

    private JSONArray get() throws JSONException, GeneralSecurityException, IOException {
        JSONArray comptes = new JSONArray();
        Properties proprietes = null;
        try {
            proprietes = getPropertiesInput();
        } catch (FileNotFoundException ex) {
        }
        if (proprietes != null) {
            String property_lastId = proprietes.getProperty("last_id");
            if (property_lastId != null) {
                int lastId = Integer.valueOf(Cryptage.decrypter(property_lastId));
                int j = 0;
                for (int i = 1; i <= lastId; i++) {
                    if (proprietes.getProperty("account_" + i + ".mail") != null) {
                        comptes.put(j++, get(proprietes, i));
                    }
                }
            }
        }
        return comptes;
    }

    private JSONObject get(int i) throws IOException, JSONException, GeneralSecurityException {
        return get(getPropertiesInput(), i);
    }

    private JSONObject get(Properties proprietes, int i) throws IOException, JSONException, GeneralSecurityException {
        JSONObject compte = new JSONObject();
        compte.put("id", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".id", "")));
        compte.put("mail", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".mail", "")));
        compte.put("user", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".user", "")));
        compte.put("pass", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".pass", "")));
        compte.put("serv", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".serv", "")));
        compte.put("port", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".port", "")));
        compte.put("ssl", Cryptage.decrypter(proprietes.getProperty("account_" + i + ".ssl", "0")));
        compte.put("boxes", new JSONArray(Cryptage.decrypter(proprietes.getProperty("account_" + i + ".boxes", "[]"))));
        return compte;
    }

    private void delete(int i) throws GeneralSecurityException, JSONException, IOException {
        Properties proprietes = null;
        try {
            proprietes = getPropertiesInput();
        } catch (FileNotFoundException ex) {
        }
        if (proprietes != null) {
            delete(proprietes, i);
            proprietes.store(cordova.getContext().openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE), "Configuration Strangers");
        }
    }

    private void delete(Properties proprietes, int i) throws GeneralSecurityException, JSONException {
        proprietes.remove("account_" + i + ".id");
        proprietes.remove("account_" + i + ".mail");
        proprietes.remove("account_" + i + ".user");
        proprietes.remove("account_" + i + ".pass");
        proprietes.remove("account_" + i + ".serv");
        proprietes.remove("account_" + i + ".port");
        proprietes.remove("account_" + i + ".ssl");
        proprietes.remove("account_" + i + ".boxes");
    }

    private Properties getPropertiesInput() throws FileNotFoundException, IOException {
        Properties proprietes = new Properties();
        proprietes.load(cordova.getContext().openFileInput(SETTINGS_FILE));
        return proprietes;
    }
}
