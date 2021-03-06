package fr.utt.if26.strangersPhonegap.plugins;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Plugin Android pour récupérer les appels du journal d'appel
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class JournalAppels extends CordovaPlugin {

    private final String ACTION_LIST = "list";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean resultat = true;
        if (action.equals(ACTION_LIST)) {
            try {
                JSONArray listeAppels = list();
                callbackContext.success(listeAppels);
            } catch (Exception e) {
                resultat = false;
            }
        }
        return resultat;
    }

    private JSONArray list() throws JSONException {
        JSONArray listeAppels = new JSONArray();

        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
        String[] arguments = {android.provider.CallLog.Calls.NUMBER};
        String requete = CallLog.Calls.DATE + "<=?";
        String[] parametres = {new Date().toString()};
        String ordre = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER;

        List<String> numerosTelephone = new ArrayList<String>();
        Cursor curseur = cordova.getActivity().getContentResolver().query(uri, arguments, requete, parametres, ordre);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            do {
                String numero = curseur.getString(0);
                if (!numerosTelephone.contains(numero)) {
                    numerosTelephone.add(numero);
                    listeAppels.put(numero);
                }
            } while (curseur.moveToNext());
        }
        curseur.close();
        return listeAppels;
    }
}