package kr.kdev.dg1s.biowiki.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.wordpress.rest.RestRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.BuildConfig;
import kr.kdev.dg1s.biowiki.models.Note;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.DeviceUtils;
import kr.kdev.dg1s.biowiki.util.MapUtils;

public class NotificationUtils {

    public static final String WPCOM_PUSH_DEVICE_NOTIFICATION_SETTINGS = "wp_pref_notification_settings";
    public static final String WPCOM_PUSH_DEVICE_UUID = "wp_pref_notifications_uuid";
    private static final String WPCOM_PUSH_DEVICE_SERVER_ID = "wp_pref_notifications_server_id";

    public static void refreshNotifications(final RestRequest.Listener listener,
                                            final RestRequest.ErrorListener errorListener) {
        BioWiki.getRestClientUtils().getNotifications(new RestRequest.Listener() {
                                                          @Override
                                                          public void onResponse(JSONObject response) {
                                                              if (listener != null) {
                                                                  listener.onResponse(response);
                                                              }
                                                          }
                                                      }, new RestRequest.ErrorListener() {
                                                          @Override
                                                          public void onErrorResponse(VolleyError error) {
                                                              if (errorListener != null) {
                                                                  errorListener.onErrorResponse(error);
                                                              }
                                                          }
                                                      }
        );
    }

    /*
     * updates a single notification, storing the result in the local db upon success
     */
    public static void updateNotification(final int noteId, final NoteUpdatedListener updateListener) {
        if (noteId == 0)
            return;

        RestRequest.Listener listener = new RestRequest.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject == null)
                    return;

                final List<Note> notes;
                try {
                    // response is an array of notes with a single note item
                    notes = parseNotes(jsonObject);
                    if (notes == null || notes.size() == 0) {
                        return;
                    }
                } catch (JSONException e) {
                    AppLog.e(AppLog.T.NOTIFS, e);
                    return;
                }

                BioWiki.wpDB.addNote(notes.get(0), false);

                if (updateListener != null) {
                    updateListener.onNoteUpdated(noteId);
                }
            }
        };
        BioWiki.getRestClientUtils().getNotification(Integer.toString(noteId), listener, null);
    }

    public static List<Note> parseNotes(JSONObject response) throws JSONException {
        List<Note> notes;
        JSONArray notesJSON = response.getJSONArray("notes");
        notes = new ArrayList<Note>(notesJSON.length());
        for (int i = 0; i < notesJSON.length(); i++) {
            Note n = new Note(notesJSON.getJSONObject(i));
            notes.add(n);
        }
        return notes;
    }

    public static String unzipString(byte[] zbytes) {
        try {
            // Add extra byte to array when Inflater is set to true
            byte[] input = new byte[zbytes.length + 1];
            System.arraycopy(zbytes, 0, input, 0, zbytes.length);
            input[zbytes.length] = 0;
            ByteArrayInputStream bin = new ByteArrayInputStream(input);
            InflaterInputStream in = new InflaterInputStream(bin);
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            int b;
            while ((b = in.read()) != -1) {
                bout.write(b);
            }
            bout.close();
            return bout.toString();
        } catch (IOException io) {
            AppLog.e(AppLog.T.NOTIFS, "Unzipping failed", io);
            return null;
        }
    }

    public static void getPushNotificationSettings(Context context, RestRequest.Listener listener,
                                                   RestRequest.ErrorListener errorListener) {
        if (/*!BioWiki.hasValidWPComCredentials(context)*/ true) {
            return;
        }

        String gcmToken = GCMRegistrar.getRegistrationId(context);
        if (TextUtils.isEmpty(gcmToken)) {
            return;
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceID = settings.getString(WPCOM_PUSH_DEVICE_SERVER_ID, null);
        if (TextUtils.isEmpty(deviceID)) {
            AppLog.e(AppLog.T.NOTIFS, "device_ID is null in preferences. Get device settings skipped.");
            return;
        }

        BioWiki.getRestClientUtils().get("/device/" + deviceID, listener, errorListener);
    }

    public static void setPushNotificationSettings(Context context) {
        if (/*!BioWiki.hasValidWPComCredentials(context)*/ true) {
            return;
        }

        String gcmToken = GCMRegistrar.getRegistrationId(context);
        if (TextUtils.isEmpty(gcmToken)) {
            return;
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceID = settings.getString(WPCOM_PUSH_DEVICE_SERVER_ID, null);
        if (TextUtils.isEmpty(deviceID)) {
            AppLog.e(AppLog.T.NOTIFS, "device_ID is null in preferences. Set device settings skipped.");
            return;
        }

        String settingsJson = settings.getString(WPCOM_PUSH_DEVICE_NOTIFICATION_SETTINGS, null);
        if (settingsJson == null)
            return;

        Gson gson = new Gson();
        Map<String, StringMap<String>> notificationSettings = gson.fromJson(settingsJson, HashMap.class);
        Map<String, Object> updatedSettings = new HashMap<String, Object>();
        if (notificationSettings == null)
            return;


        // Build the settings object to send back to WP.com
        StringMap<?> mutedBlogsMap = notificationSettings.get("muted_blogs");
        StringMap<?> muteUntilMap = notificationSettings.get("mute_until");
        ArrayList<StringMap<Double>> blogsList = (ArrayList<StringMap<Double>>) mutedBlogsMap.get("value");
        notificationSettings.remove("muted_blogs");
        notificationSettings.remove("mute_until");

        for (Map.Entry<String, StringMap<String>> entry : notificationSettings.entrySet()) {
            StringMap<String> setting = entry.getValue();
            updatedSettings.put(entry.getKey(), setting.get("value"));
        }

        if (muteUntilMap != null && muteUntilMap.get("value") != null) {
            updatedSettings.put("mute_until", muteUntilMap.get("value"));
        }

        ArrayList<StringMap<Double>> mutedBlogsList = new ArrayList<StringMap<Double>>();
        for (StringMap<Double> userBlog : blogsList) {
            if (MapUtils.getMapBool(userBlog, "value")) {
                mutedBlogsList.add(userBlog);
            }
        }

        if (updatedSettings.size() == 0 && mutedBlogsList.size() == 0)
            return;

        updatedSettings.put("muted_blogs", mutedBlogsList); //If muted blogs list is unchanged we can even skip this assignment.

        Map<String, String> contentStruct = new HashMap<String, String>();
        contentStruct.put("device_token", gcmToken);
        contentStruct.put("device_family", "android");
        contentStruct.put("app_secret_key", NotificationUtils.getAppPushNotificationsName());
        contentStruct.put("settings", gson.toJson(updatedSettings));
        BioWiki.getRestClientUtils().post("/device/" + deviceID, contentStruct, null, null, null);
    }

    public static void registerDeviceForPushNotifications(final Context ctx, String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
        String uuid = settings.getString(WPCOM_PUSH_DEVICE_UUID, null);
        if (uuid == null)
            return;

        String deviceName = DeviceUtils.getInstance().getDeviceName(ctx);
        Map<String, String> contentStruct = new HashMap<String, String>();
        contentStruct.put("device_token", token);
        contentStruct.put("device_family", "android");
        contentStruct.put("app_secret_key", NotificationUtils.getAppPushNotificationsName());
        contentStruct.put("device_name", deviceName);
        contentStruct.put("device_model", Build.MANUFACTURER + " " + Build.MODEL);
        contentStruct.put("app_version", BioWiki.versionName);
        contentStruct.put("os_version", android.os.Build.VERSION.RELEASE);
        contentStruct.put("device_uuid", uuid);
        com.wordpress.rest.RestRequest.Listener listener = new RestRequest.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                AppLog.d(AppLog.T.NOTIFS, "Register token action succeeded");
                try {
                    String deviceID = jsonObject.getString("ID");
                    if (deviceID == null) {
                        AppLog.e(AppLog.T.NOTIFS, "Server response is missing of the device_id. Registration skipped!!");
                        return;
                    }
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(WPCOM_PUSH_DEVICE_SERVER_ID, deviceID);
                    JSONObject settingsJSON = jsonObject.getJSONObject("settings");
                    editor.putString(WPCOM_PUSH_DEVICE_NOTIFICATION_SETTINGS, settingsJSON.toString());
                    editor.commit();
                    AppLog.d(AppLog.T.NOTIFS, "Server response OK. The device_id : " + deviceID);
                } catch (JSONException e1) {
                    AppLog.e(AppLog.T.NOTIFS, "Server response is NOT ok. Registration skipped!!", e1);
                }
            }
        };
        RestRequest.ErrorListener errorListener = new RestRequest.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppLog.e(AppLog.T.NOTIFS, "Register token action failed", volleyError);
            }
        };

        BioWiki.getRestClientUtils().post("/devices/new", contentStruct, null, listener, errorListener);
    }

    public static void unregisterDevicePushNotifications(final Context ctx) {
        com.wordpress.rest.RestRequest.Listener listener = new RestRequest.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                AppLog.d(AppLog.T.NOTIFS, "Unregister token action succeeded");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
                editor.remove(WPCOM_PUSH_DEVICE_SERVER_ID);
                editor.remove(WPCOM_PUSH_DEVICE_NOTIFICATION_SETTINGS);
                editor.remove(WPCOM_PUSH_DEVICE_UUID);
                editor.commit();
            }
        };
        RestRequest.ErrorListener errorListener = new RestRequest.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppLog.e(AppLog.T.NOTIFS, "Unregister token action failed", volleyError);
            }
        };

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
        String deviceID = settings.getString(WPCOM_PUSH_DEVICE_SERVER_ID, null);
        if (TextUtils.isEmpty(deviceID)) {
            return;
        }
        BioWiki.getRestClientUtils().post("/devices/" + deviceID + "/delete", listener, errorListener);
    }

    public static String getAppPushNotificationsName() {
        //white listing only few keys.
        if (BuildConfig.APP_PN_KEY.equals("kr.kdev.dg1s.biowiki.beta.build"))
            return "kr.kdev.dg1s.biowiki.beta.build";
        if (BuildConfig.APP_PN_KEY.equals("kr.kdev.dg1s.biowiki.debug.build"))
            return "kr.kdev.dg1s.biowiki.debug.build";

        return "kr.kdev.dg1s.biowiki.playstore";
    }

    static interface NoteUpdatedListener {
        void onNoteUpdated(int noteId);
    }
}