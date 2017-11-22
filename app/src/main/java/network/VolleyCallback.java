package network;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by felipe on 31/10/17.
 */

public interface VolleyCallback {

    void onSuccess(JSONObject jsonObject) throws JSONException;
    void onConnectionError(VolleyError error);
    void onJsonExceptionError();
}
