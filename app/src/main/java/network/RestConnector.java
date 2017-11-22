package network;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import constants.CONSTANTS;

public class RestConnector {
    private Context context;

    public RestConnector(Context context) {
        this.context = context;
    }

    public void doGet(final VolleyCallback callback, String endpoint) {
        String url = CONSTANTS.NODE_SERVER_URL + endpoint;
        RequestQueue queue = VolleySingleton.getInstance(context).
                getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            callback.onJsonExceptionError();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onConnectionError(error);
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void doPost(final VolleyCallback callback, String endpoint, JSONObject payload) {
        String url = CONSTANTS.NODE_SERVER_URL + endpoint;
        RequestQueue queue = VolleySingleton.getInstance(context).
                getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            callback.onJsonExceptionError();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onConnectionError(error);
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
