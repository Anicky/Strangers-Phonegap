package fr.utt.if26.strangersPhonegap;

import android.util.Log;
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

    private final String TAG = "Plugin : StockageLocal";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Plugin start");
        boolean resultat = true;

        // @todo
        
        Log.d(TAG, "Plugin stop");
        return resultat;
    }
}