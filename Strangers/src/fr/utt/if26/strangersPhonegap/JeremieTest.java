package fr.utt.if26.strangersPhonegap;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class JeremieTest extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("list")) {
            String message = args.getString(0);
            this.liste(message, callbackContext);
            return true;
        }
        return false;
    }

    private void liste(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message + "BREF.");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
