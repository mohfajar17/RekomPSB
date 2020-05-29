package com.example.poskorafinaru;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {


    private String datePick1;
    private String datePick2;
    private TextView datePickTreg1;
    private TextView datePickTreg2;
    private TextView textViewTregOk;
    private TextView textViewTregNok;
    private TextView textViewTregPer;
    private TableRow tableRowTreg;
    private Button buttonMain;
    private SharedPrefManager sharedPrefManager;

    private ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        textViewTregOk = (TextView) view.findViewById(R.id.textViewTregOk);
        textViewTregNok = (TextView) view.findViewById(R.id.textViewTregNok);
        textViewTregPer = (TextView) view.findViewById(R.id.textViewTregPer);
        datePickTreg1 = (TextView) view.findViewById(R.id.datePickTreg1);
        datePickTreg2 = (TextView) view.findViewById(R.id.datePickTreg2);
        buttonMain = (Button) view.findViewById(R.id.buttonMain);
        tableRowTreg = (TableRow) view.findViewById(R.id.tableRowTreg);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Refresh Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        datePick1 = sharedPrefManager.getDatePick1();
        datePickTreg1.setText(sharedPrefManager.getTextDatePick1());
        datePickTreg1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });

        datePick2 = sharedPrefManager.getDatePick2();
        datePickTreg2.setText(sharedPrefManager.getTextDatePick2());
        datePickTreg2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        tableRowTreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AreaFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, fragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }
        });

        setData();

        return view;
    }

    private void setData() {
        progressDialog.show();

        final String firstDate = String.valueOf(datePick1);
        final String secondtDate = String.valueOf(datePick2);

        if (Integer.valueOf(datePick1)>=Integer.valueOf(datePick2)){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Date is incorrect", Toast.LENGTH_LONG).show();
        } else {
            sharedPrefManager.setDatePick1(datePick1);
            sharedPrefManager.setTextDatePick1(String.valueOf(datePickTreg1.getText()));
            sharedPrefManager.setDatePick2(datePick2);
            sharedPrefManager.setTextDatePick2(String.valueOf(datePickTreg2.getText()));
            StringRequest request = new StringRequest(Request.Method.GET,
                    Config.DATA_URL+"param=treg&witel=&firstDate="+firstDate+"&secondtDate="+secondtDate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONArray jsonArrayTreg = new JSONArray(response);
                                DecimalFormat percent = new DecimalFormat("#.#");
                                Double per = Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_ok"))/(Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_ok"))+Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_nok")))*100;
                                textViewTregOk.setText(jsonArrayTreg.getJSONObject(0).getString("jml_ok"));
                                textViewTregNok.setText(jsonArrayTreg.getJSONObject(0).getString("jml_nok"));
                                textViewTregPer.setText(percent.format(per)+"%");
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Data not found", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Network error, check your network", Toast.LENGTH_LONG).show();
                        }
                    });
            Volley.newRequestQueue(getActivity()).add(request);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void setDate(final int dp) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dp==0) {
                    if (month+1<10)
                        datePick1 =  year + "0" + (month + 1);
                    else datePick1 =  "" + year + (month + 1);
                    datePickTreg1.setText("26-" + (month + 1) + "-" + year);
                } else {
                    if (month+1<10)
                        datePick2 =  year + "0" +(month + 1);
                    else datePick2 =  "" + year +(month + 1);
                    datePickTreg2.setText("25-" + (month + 1) + "-" + year);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
