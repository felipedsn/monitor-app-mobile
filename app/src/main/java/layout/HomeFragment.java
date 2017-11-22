package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fasno.monitor.R;

import org.json.JSONException;
import org.json.JSONObject;

import constants.CONSTANTS;
import network.RestConnector;
import network.VolleyCallback;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("Monitor");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView statusValue = (TextView) view.findViewById(R.id.status_value);
        TextView lastMovementValue = (TextView) view.findViewById(R.id.last_movement_value);
        //TextView lastMedicine = (TextView) view.findViewById(R.id.last_medicine_value);
        TextView nextMedicineValue = (TextView) view.findViewById(R.id.next_medicine_value);
        TextView lastFallValue = (TextView) view.findViewById(R.id.last_fall_value);

        callGetMethod(statusValue, CONSTANTS.PREFERENCES_ENDPOINT, "medicine");
        callGetMethod(lastMovementValue, CONSTANTS.PREFERENCES_ENDPOINT, "medicine");
        //callGetMethod(lastMedicine, CONSTANTS.PREFERENCES_ENDPOINT, "medicine");
        callGetMethod(nextMedicineValue, CONSTANTS.PREFERENCES_ENDPOINT, "medicine");
        callGetMethod(lastFallValue, CONSTANTS.PREFERENCES_ENDPOINT, "medicine");

        return view;
    }

    private void callGetMethod(final TextView textView, String endpoint, final String jsonProperty){
        RestConnector restConnector = new RestConnector(getActivity());
        restConnector.doGet(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                textView.setText(jsonObject.getString(jsonProperty));
            }
            @Override
            public void onConnectionError(VolleyError error) {
                textView.setText("Error");
                textView.setTextColor(Color.RED);
            }
            @Override
            public void onJsonExceptionError() {
                textView.setText("Error");
                textView.setTextColor(Color.RED);
            }
        }, endpoint);
    }
}
