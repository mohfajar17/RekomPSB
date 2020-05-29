package com.example.poskorafinaru;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CrewFragment extends Fragment {

    private TextView tiketUbisName;
    private String datePick1;
    private String datePick2;
    private TextView datePickTiket1;
    private TextView datePickTiket2;
    private Button buttonTiket;

    private SharedPrefManager sharedPrefManager;

    private OnFragmentInteractionListener mListener;

    public CrewFragment() {

    }

    public static CrewFragment newInstance() {
        CrewFragment fragment = new CrewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crew, container, false);

        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        tiketUbisName = (TextView) view.findViewById(R.id.tiketUbisName);
        datePickTiket1 = (TextView) view.findViewById(R.id.datePickTiket1);
        datePickTiket2 = (TextView) view.findViewById(R.id.datePickTiket2);
        buttonTiket = (Button) view.findViewById(R.id.buttonTiket);

        Bundle bundle = this.getArguments();
        datePick1 = bundle.getString("datePick1");
        datePick2 = bundle.getString("datePick2");

        datePickTiket1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });

        datePickTiket2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        buttonTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(datePick1)>=Integer.valueOf(datePick2)){
                    Toast.makeText(getContext(), "Date is incorrect", Toast.LENGTH_LONG).show();
                } else {
                    sharedPrefManager.setDatePick1(datePick1);
                    sharedPrefManager.setTextDatePick1(String.valueOf(datePickTiket1.getText()));
                    sharedPrefManager.setDatePick2(datePick2);
                    sharedPrefManager.setTextDatePick2(String.valueOf(datePickTiket2.getText()));

                    String url = "http://10.11.4.52/roc-api/rekonPsb_download.php?param=ubis&area="+tiketUbisName.getText().toString()+"&firstDate="+datePick1+"&secondtDate="+datePick2;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });

        if (datePick1 != null || datePick2 != null){
            tiketUbisName.setText(bundle.getString("ubisname"));
            datePickTiket1.setText(bundle.getString("textViewDate1"));
            datePickTiket2.setText(bundle.getString("textViewDate2"));
        } else {
            tiketUbisName.setText("ALL CREW");

            datePick1 = sharedPrefManager.getDatePick1();
            datePick2 = sharedPrefManager.getDatePick2();
            datePickTiket1.setText(sharedPrefManager.getTextDatePick1());
            datePickTiket2.setText(sharedPrefManager.getTextDatePick2());
        }

        return view;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dp==0) {
                    datePickTiket1.setText("26-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick1 =  year + "0" + (month + 1);
                    else datePick1 =  "" + year + (month + 1);
                } else {
                    datePickTiket2.setText("25-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick2 =  year + "0" +(month + 1);
                    else datePick2 =  "" + year +(month + 1);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}