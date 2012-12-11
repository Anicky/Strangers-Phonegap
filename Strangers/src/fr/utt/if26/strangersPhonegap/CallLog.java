package fr.utt.if26.strangersPhonegap;

import android.database.*;
import android.util.Log;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.*;

public class CallLog extends Plugin {

    @Override
    public PluginResult execute(String actionName, JSONArray arguments, String callback) {


        JSONObject callLogs;
        PluginResult result;


        try {
            switch (getActionItem(actionName)) {
                case 1:
                    callLogs = getAllCallLog();
                    result = new PluginResult(Status.OK, callLogs);
                    break;
                default:
                    result = new PluginResult(Status.INVALID_ACTION);
            }
        } catch (JSONException jsonEx) {
            result = new PluginResult(Status.JSON_EXCEPTION);
        }



        return result;
    }

    private JSONObject getAllCallLog() throws JSONException {
        JSONObject callLog = new JSONObject();

        String[] strFields = {
            android.provider.CallLog.Calls.DATE,
            android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.TYPE,
            android.provider.CallLog.Calls.DURATION,
            android.provider.CallLog.Calls.NEW,
            android.provider.CallLog.Calls.CACHED_NAME,
            android.provider.CallLog.Calls.CACHED_NUMBER_TYPE,
            android.provider.CallLog.Calls.CACHED_NUMBER_LABEL//,
        };

        try {
            Cursor callLogCursor = cordova.getActivity().getContentResolver().query(
                    android.provider.CallLog.Calls.CONTENT_URI,
                    strFields,
                    null,
                    null,
                    android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);



            int callCount = callLogCursor.getCount();

            if (callCount > 0) {
                JSONArray callLogItem = new JSONArray();
                JSONArray callLogItems = new JSONArray();

                callLogCursor.moveToFirst();
                do {
                    callLogItem.put(callLogCursor.getLong(0));
                    callLogItem.put(callLogCursor.getString(1));
                    callLogItem.put(callLogCursor.getInt(2));
                    callLogItem.put(callLogCursor.getLong(3));
                    callLogItem.put(callLogCursor.getInt(4));
                    callLogItem.put(callLogCursor.getString(5));
                    callLogItem.put(callLogCursor.getInt(6));
                    callLogItems.put(callLogItem);
                    callLogItem = new JSONArray();

                } while (callLogCursor.moveToNext());

                callLog.put("Rows", callLogItems);
            }


            callLogCursor.close();
        } catch (Exception e) {

            Log.d("CallLog_Plugin", " ERROR : SQL to get cursor: ERROR " + e.getMessage());
        }



        return callLog;
    }

    private int getActionItem(String actionName) throws JSONException {
        JSONObject actions = new JSONObject("{'all':1,'last':2,'time':3}");
        if (actions.has(actionName)) {
            return actions.getInt(actionName);
        }

        return 0;
    }
}