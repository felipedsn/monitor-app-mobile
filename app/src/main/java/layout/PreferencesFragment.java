package layout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fasno.monitor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Pattern;

import constants.CONSTANTS;
import network.RestConnector;
import network.VolleyCallback;

public class PreferencesFragment extends Fragment {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);

        getActivity().setTitle(R.string.preferences_title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText medicineTimeValue = (EditText) view.findViewById(R.id.medicine_time_value);
        final EditText sleepTimeFromValue = (EditText) view.findViewById(R.id.sleep_time_from_value);
        final EditText sleepTimeToValue = (EditText) view.findViewById(R.id.sleep_time_to_value);

        /**
         * GET actual values to EditText.
         */
        RestConnector restConnector = new RestConnector(getActivity());
        restConnector.doGet(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                medicineTimeValue.setText(jsonObject.getString("medicine"));
                sleepTimeFromValue.setText(jsonObject.getString("sleepFrom"));
                sleepTimeToValue.setText(jsonObject.getString("sleepTo"));
            }
            @Override
            public void onConnectionError(VolleyError error){
                medicineTimeValue.setText(R.string.error);
                medicineTimeValue.setTextColor(Color.RED);
                sleepTimeFromValue.setText(R.string.error);
                sleepTimeFromValue.setTextColor(Color.RED);
                sleepTimeToValue.setText(R.string.error);
                sleepTimeToValue.setTextColor(Color.RED);
            }
            @Override
            public void onJsonExceptionError(){
                medicineTimeValue.setText(R.string.error);
                sleepTimeFromValue.setText(R.string.error);
                sleepTimeToValue.setText(R.string.error);
            }
        }, CONSTANTS.PREFERENCES_ENDPOINT);

        /**
         * Set onClick method to call POST and update preferences.
         */
        Button updateButton = (Button) view.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("medicine", medicineTimeValue.getText());
                    payload.put("sleepTo", sleepTimeToValue.getText());
                    payload.put("sleepFrom", sleepTimeFromValue.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if( Pattern.matches("\\d+:\\d+", medicineTimeValue.getText())
                        && Pattern.matches("\\d+:\\d+", sleepTimeToValue.getText())
                        && Pattern.matches("\\d+:\\d+", sleepTimeFromValue.getText())) {
                    callPostMethod(CONSTANTS.PREFERENCES_ENDPOINT, payload);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(R.string.error);
                    alertDialog.setMessage(getActivity().getResources().getString(R.string.preferences_wrong_time_format));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
    }

    private void callGetMethod(final EditText editText, String endpoint, final String jsonProperty){
        RestConnector restConnector = new RestConnector(getActivity());
        restConnector.doGet(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                editText.setText(jsonObject.getString(jsonProperty));
            }
            @Override
            public void onConnectionError(VolleyError error){
                editText.setText(R.string.error);
            }
            @Override
            public void onJsonExceptionError(){
                editText.setText(R.string.error);
            }
        }, endpoint);
    }

    private void callPostMethod(String endpoint, JSONObject payload){
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        final DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        RestConnector restConnector = new RestConnector(getActivity());
        restConnector.doPost(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                alertDialog.setTitle(R.string.success_title);
                alertDialog.setMessage(getActivity().getResources().getString(R.string.preferences_success_update));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",dialogInterface);
                alertDialog.show();
            }
            @Override
            public void onConnectionError(VolleyError error){
                alertDialog.setTitle(R.string.error_title);
                alertDialog.setMessage(getActivity().getResources().getString(R.string.preferences_connection_error));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",dialogInterface);
                alertDialog.show();
            }
            @Override
            public void onJsonExceptionError(){
                alertDialog.setTitle(R.string.error_title);
                alertDialog.setMessage(getActivity().getResources().getString(R.string.preferences_json_error));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",dialogInterface);
                alertDialog.show();
            }
        }, endpoint, payload);
    }

}