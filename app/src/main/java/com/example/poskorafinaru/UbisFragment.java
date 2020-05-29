package com.example.poskorafinaru;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UbisFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private int mColumnCount = 1;
    private String datePick1;
    private String datePick2;
    private RecyclerView recyclerView;
    private Spinner spinnerUbis;
    private TextView datePickUbis1;
    private TextView datePickUbis2;
    private TextView ubisTregOk;
    private TextView ubisTregNok;
    private TextView ubisTregPer;
    private LinearLayout rowUbisTreg;
    private Button buttonUbis;

    private SharedPrefManager sharedPrefManager;

    private ProgressDialog progressDialog;

    private OnListFragmentInteractionListener mListener;

    public UbisFragment() {
    }

    public static UbisFragment newInstance() {
        UbisFragment fragment = new UbisFragment();
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
        final View view = inflater.inflate(R.layout.fragment_ubis, container, false);

        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        ubisTregOk = (TextView) view.findViewById(R.id.ubisTregOk);
        ubisTregNok = (TextView) view.findViewById(R.id.ubisTregNok);
        ubisTregPer = (TextView) view.findViewById(R.id.ubisTregPer);
        datePickUbis1 = (TextView) view.findViewById(R.id.datePickUbis1);
        datePickUbis2 = (TextView) view.findViewById(R.id.datePickUbis2);
        spinnerUbis = (Spinner) view.findViewById(R.id.spinnerUbis);
        buttonUbis = (Button) view.findViewById(R.id.buttonUbis);
        rowUbisTreg = (LinearLayout) view.findViewById(R.id.rowUbisTreg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewUbis);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Refresh Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        Bundle bundle = this.getArguments();
        datePick1 = bundle.getString("datePick1");
        datePick2 = bundle.getString("datePick2");

        if (datePick1 != null || datePick2 != null){
            datePickUbis1.setText(bundle.getString("textViewDate1"));
            datePickUbis2.setText(bundle.getString("textViewDate2"));

            if (bundle.getString("spinner").toLowerCase().contains("denpasar".toLowerCase())){
                spinnerUbis.setSelection(1);
                loadData("ubis", "DENPASAR");
            } else if (bundle.getString("spinner").toLowerCase().contains("jember".toLowerCase())) {
                spinnerUbis.setSelection(2);
                loadData("ubis", "JEMBER");
            } else if (bundle.getString("spinner").toLowerCase().contains("kediri".toLowerCase())) {
                spinnerUbis.setSelection(3);
                loadData("ubis", "KEDIRI");
            } else if (bundle.getString("spinner").toLowerCase().contains("kupang".toLowerCase())) {
                spinnerUbis.setSelection(4);
                loadData("ubis", "KUPANG");
            } else if (bundle.getString("spinner").toLowerCase().contains("madiun".toLowerCase())) {
                spinnerUbis.setSelection(5);
                loadData("ubis", "MADIUN");
            } else if (bundle.getString("spinner").toLowerCase().contains("madura".toLowerCase())) {
                spinnerUbis.setSelection(6);
                loadData("ubis", "MADURA");
            } else if (bundle.getString("spinner").toLowerCase().contains("malang".toLowerCase())) {
                spinnerUbis.setSelection(7);
                loadData("ubis", "MALANG");
            } else if (bundle.getString("spinner").toLowerCase().contains("mataram".toLowerCase())) {
                spinnerUbis.setSelection(8);
                loadData("ubis", "MATARAM");
            } else if (bundle.getString("spinner").toLowerCase().contains("pasuruan".toLowerCase())) {
                spinnerUbis.setSelection(9);
                loadData("ubis", "PASURUAN");
            } else if (bundle.getString("spinner").toLowerCase().contains("sidoarjo".toLowerCase())) {
                spinnerUbis.setSelection(10);
                loadData("ubis", "SIDOARJO");
            } else if (bundle.getString("spinner").toLowerCase().contains("singar".toLowerCase())) {
                spinnerUbis.setSelection(11);
                loadData("ubis", "SINGARJA");
            } else if (bundle.getString("spinner").toLowerCase().contains("selatan".toLowerCase())) {
                spinnerUbis.setSelection(12);
                loadData("ubis", "SBY%20SELATAN");
            } else if (bundle.getString("spinner").toLowerCase().contains("utara".toLowerCase())) {
                spinnerUbis.setSelection(13);
                loadData("ubis", "SBY%20UTARA");
            } else loadData("allubis", "");
        } else {
            datePick1 = sharedPrefManager.getDatePick1();
            datePick2 = sharedPrefManager.getDatePick2();
            datePickUbis1.setText(sharedPrefManager.getTextDatePick1());
            datePickUbis2.setText(sharedPrefManager.getTextDatePick2());

            loadData("allubis", "");
        }

        datePickUbis1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });
        datePickUbis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
        }

        buttonUbis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerUbis.getSelectedItemPosition()==0)
                    loadData("allubis", "");
                else if (spinnerUbis.getSelectedItemPosition()==1)
                    loadData("ubis", "DENPASAR");
                else if (spinnerUbis.getSelectedItemPosition()==2)
                    loadData("ubis", "JEMBER");
                else if (spinnerUbis.getSelectedItemPosition()==3)
                    loadData("ubis", "KEDIRI");
                else if (spinnerUbis.getSelectedItemPosition()==4)
                    loadData("ubis", "KUPANG");
                else if (spinnerUbis.getSelectedItemPosition()==5)
                    loadData("ubis", "MADIUN");
                else if (spinnerUbis.getSelectedItemPosition()==6)
                    loadData("ubis", "MADURA");
                else if (spinnerUbis.getSelectedItemPosition()==7)
                    loadData("ubis", "MALANG");
                else if (spinnerUbis.getSelectedItemPosition()==8)
                    loadData("ubis", "MATARAM");
                else if (spinnerUbis.getSelectedItemPosition()==9)
                    loadData("ubis", "PASURUAN");
                else if (spinnerUbis.getSelectedItemPosition()==10)
                    loadData("ubis", "SIDOARJO");
                else if (spinnerUbis.getSelectedItemPosition()==11)
                    loadData("ubis", "SINGARJA");
                else if (spinnerUbis.getSelectedItemPosition()==12)
                    loadData("ubis", "SBY%20SELATAN");
                else if (spinnerUbis.getSelectedItemPosition()==13)
                    loadData("ubis", "SBY%20UTARA");
                else loadData("allubis", "");
            }
        });

        rowUbisTreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFragment, new MainFragment());
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void loadData(String param, String ubis) {
        progressDialog.show();

        final String firstDate = String.valueOf(datePick1);
        final String secondtDate = String.valueOf(datePick2);
        final List<Treg> tregs = new ArrayList<>();

        if (Integer.valueOf(datePick1)>=Integer.valueOf(datePick2)){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Date is incorrect", Toast.LENGTH_LONG).show();
        } else {
            sharedPrefManager.setDatePick1(datePick1);
            sharedPrefManager.setTextDatePick1(String.valueOf(datePickUbis1.getText()));
            sharedPrefManager.setDatePick2(datePick2);
            sharedPrefManager.setTextDatePick2(String.valueOf(datePickUbis2.getText()));

            StringRequest request = new StringRequest(Request.Method.GET,
                    Config.DATA_URL+"param="+param+"&witel="+ubis+"&firstDate="+firstDate+"&secondtDate="+secondtDate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                double ok = 0, nok = 0, percent = 0;
                                JSONArray jsonArrayTreg = new JSONArray(response);
                                for(int i=0;i<jsonArrayTreg.length();i++){
                                    tregs.add(new Treg(jsonArrayTreg.getJSONObject(i)));
                                    ok = ok+Integer.parseInt(jsonArrayTreg.getJSONObject(i).getString("jml_ok"));
                                    nok = nok+Integer.parseInt(jsonArrayTreg.getJSONObject(i).getString("jml_nok"));
                                }
                                percent = ok/(ok+nok)*100;
                                ubisTregOk.setText(""+new DecimalFormat("#").format(ok));
                                ubisTregNok.setText(""+new DecimalFormat("#").format(nok));
                                ubisTregPer.setText(new DecimalFormat("#.##").format(percent)+"%");
                                recyclerView.setAdapter(new UbisFragment.MyMainRecyclerViewAdapter(tregs, mListener));
                                progressDialog.dismiss();
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
                            progressDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(getContext(), "Network error, check your network", Toast.LENGTH_LONG).show();
                        }
                    });
            Volley.newRequestQueue(getActivity()).add(request);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Treg item);
    }

    private void setDate(final int dp) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(UbisFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dp==0) {
                    datePickUbis1.setText("26-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick1 =  year + "0" + (month + 1);
                    else datePick1 =  "" + year + (month + 1);
                } else {
                    datePickUbis2.setText("25-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick2 =  year + "0" +(month + 1);
                    else datePick2 =  "" + year +(month + 1);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private class MyMainRecyclerViewAdapter extends RecyclerView.Adapter<MyMainRecyclerViewAdapter.ViewHolder> {

        private final List<Treg> mValues;
        private final OnListFragmentInteractionListener mListener;

        private MyMainRecyclerViewAdapter(List<Treg> mValues, OnListFragmentInteractionListener mListener) {
            this.mValues = mValues;
            this.mListener = mListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_ubis_list, parent, false);
            return new MyMainRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyMainRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.textViewUbis.setText(mValues.get(position).getUbis());
            holder.textViewUbisOk.setText(mValues.get(position).getJml_ok());
            holder.textViewUbisNok.setText(mValues.get(position).getJml_nok());
            holder.textViewUbisPer.setText(mValues.get(position).getPrs()+"%");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new CrewFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();

                    bundle.putString("ubisname", mValues.get(position).getUbis());
                    bundle.putString("datePick1", datePick1);
                    bundle.putString("datePick2", datePick2);
                    bundle.putString("textViewDate1", datePickUbis1.getText().toString());
                    bundle.putString("textViewDate2", datePickUbis2.getText().toString());
                    fragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.containerFragment, fragment);
                    fragmentTransaction.disallowAddToBackStack();
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            public final TextView textViewUbis;
            public final TextView textViewUbisOk;
            public final TextView textViewUbisNok;
            public final TextView textViewUbisPer;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                textViewUbis = (TextView) itemView.findViewById(R.id.textViewUbis);
                textViewUbisOk = (TextView) itemView.findViewById(R.id.textViewUbisOk);
                textViewUbisNok = (TextView) itemView.findViewById(R.id.textViewUbisNok);
                textViewUbisPer = (TextView) itemView.findViewById(R.id.textViewUbisPer);
            }
        }
    }
}