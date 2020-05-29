package com.example.poskorafinaru;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class WitelFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private int mColumnCount = 1;
    private String datePick1;
    private String datePick2;
    private RecyclerView recyclerView;
    private Spinner spinnerWitel;
    private TextView datePickWitel1;
    private TextView datePickWitel2;
    private TextView witelTregOk;
    private TextView witelTregNok;
    private TextView witelTregPer;
    private LinearLayout rowAreaTreg;
    private Button buttonWitel;

    private SharedPrefManager sharedPrefManager;

    private ProgressDialog progressDialog;

    private OnListFragmentInteractionListener mListener;

    public WitelFragment() {
    }

    public static WitelFragment newInstance() {
        WitelFragment fragment = new WitelFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_witel, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Refresh Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        sharedPrefManager = SharedPrefManager.getInstance(getActivity());

        witelTregOk = (TextView) view.findViewById(R.id.witelTregOk);
        witelTregNok = (TextView) view.findViewById(R.id.witelTregNok);
        witelTregPer = (TextView) view.findViewById(R.id.witelTregPer);

        datePickWitel1 = (TextView) view.findViewById(R.id.datePickWitel1);
        datePickWitel2 = (TextView) view.findViewById(R.id.datePickWitel2);
        spinnerWitel = (Spinner) view.findViewById(R.id.spinnerWitel);
        buttonWitel = (Button) view.findViewById(R.id.buttonWitel);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewWitel);
        rowAreaTreg = (LinearLayout) view.findViewById(R.id.rowWitelTreg);

        Bundle bundle = this.getArguments();
        datePick1 = bundle.getString("datePick1");
        datePick2 = bundle.getString("datePick2");

        if (datePick1 != null || datePick2 != null){
            datePickWitel1.setText(bundle.getString("textViewDate1"));
            datePickWitel2.setText(bundle.getString("textViewDate2"));

            if (bundle.getString("spinner").toLowerCase().contains("jatim 1".toLowerCase())){
                spinnerWitel.setSelection(1);
                loadData("jatim1");
            } else if (bundle.getString("spinner").toLowerCase().contains("jatim 2".toLowerCase())){
                spinnerWitel.setSelection(2);
                loadData("jatim2");
            } else if (bundle.getString("spinner").toLowerCase().contains("jatim 3".toLowerCase())){
                spinnerWitel.setSelection(3);
                loadData("jatim3");
            } else if (bundle.getString("spinner").toLowerCase().contains("bali".toLowerCase())){
                spinnerWitel.setSelection(4);
                loadData("bali");
            } else if (bundle.getString("spinner").toLowerCase().contains("nusra".toLowerCase())){
                spinnerWitel.setSelection(5);
                loadData("nusra");
            } else {
                spinnerWitel.setSelection(0);
                loadData("alljatim");
            }
        } else {
            datePick1 = sharedPrefManager.getDatePick1();
            datePick2 = sharedPrefManager.getDatePick2();
            datePickWitel1.setText(sharedPrefManager.getTextDatePick1());
            datePickWitel2.setText(sharedPrefManager.getTextDatePick2());

            loadData("alljatim");
        }

        datePickWitel1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });
        datePickWitel2.setOnClickListener(new View.OnClickListener(){
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

        buttonWitel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerWitel.getSelectedItemPosition()==0)
                    loadData("alljatim");
                else if (spinnerWitel.getSelectedItemPosition()==1)
                    loadData("jatim1");
                else if (spinnerWitel.getSelectedItemPosition()==2)
                    loadData("jatim2");
                else if (spinnerWitel.getSelectedItemPosition()==3)
                    loadData("jatim3");
                else if (spinnerWitel.getSelectedItemPosition()==4)
                    loadData("bali");
                else if (spinnerWitel.getSelectedItemPosition()==5)
                    loadData("nusra");
                else loadData("alljatim");
            }
        });

        rowAreaTreg.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void loadData(String witel) {
        progressDialog.show();

        final String firstDate = String.valueOf(datePick1);
        final String secondtDate = String.valueOf(datePick2);
        final List<Treg> tregs = new ArrayList<>();

        if (Integer.valueOf(datePick1)>=Integer.valueOf(datePick2)){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Date is incorrect", Toast.LENGTH_LONG).show();
        } else {
            sharedPrefManager.setDatePick1(datePick1);
            sharedPrefManager.setTextDatePick1(String.valueOf(datePickWitel1.getText()));
            sharedPrefManager.setDatePick2(datePick2);
            sharedPrefManager.setTextDatePick2(String.valueOf(datePickWitel2.getText()));

            StringRequest request = new StringRequest(Request.Method.GET,
                    Config.DATA_URL+"param="+witel+"&witel=&firstDate="+firstDate+"&secondtDate="+secondtDate,
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
                                witelTregOk.setText(""+new DecimalFormat("#").format(ok));
                                witelTregNok.setText(""+new DecimalFormat("#").format(nok));
                                witelTregPer.setText(new DecimalFormat("#.##").format(percent)+"%");
                                recyclerView.setAdapter(new WitelFragment.MyMainRecyclerViewAdapter(tregs, mListener));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode == getActivity().RESULT_OK ){
            loadData("alljatim");
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(WitelFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dp==0) {
                    datePickWitel1.setText("26-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick1 =  year + "0" + (month + 1);
                    else datePick1 =  "" + year + (month + 1);
                } else {
                    datePickWitel2.setText("25-" + (month + 1) + "-" + year);
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
                    .inflate(R.layout.fragment_witel_list, parent, false);
            return new MyMainRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyMainRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.textViewWitel.setText(mValues.get(position).getWitel());
            holder.textViewWitelOk.setText(mValues.get(position).getJml_ok());
            holder.textViewWitelNok.setText(mValues.get(position).getJml_nok());
            holder.textViewWitelPer.setText(mValues.get(position).getPrs()+"%");
            holder.textViewArea.setText(mValues.get(position).getArea());

            ViewGroup.LayoutParams layoutParams = holder.textViewArea.getLayoutParams();
            if (position>0){
                if (mValues.get(position).getArea().toLowerCase().contains(mValues.get(position-1).getArea().toLowerCase())){

                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    holder.textViewArea.setLayoutParams(layoutParams);
                }
            } else {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.textViewArea.setLayoutParams(layoutParams);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new UbisFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();

                    bundle.putString("datePick1", datePick1);
                    bundle.putString("datePick2", datePick2);
                    bundle.putString("textViewDate1", datePickWitel1.getText().toString());
                    bundle.putString("textViewDate2", datePickWitel2.getText().toString());
                    bundle.putString("spinner", mValues.get(position).getWitel());
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

            public final TextView textViewWitel;
            public final TextView textViewWitelOk;
            public final TextView textViewWitelNok;
            public final TextView textViewWitelPer;
            public final TextView textViewArea;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                textViewWitel = (TextView) itemView.findViewById(R.id.textViewWitel);
                textViewWitelOk = (TextView) itemView.findViewById(R.id.textViewWitelOk);
                textViewWitelNok = (TextView) itemView.findViewById(R.id.textViewWitelNok);
                textViewWitelPer = (TextView) itemView.findViewById(R.id.textViewWitelPer);
                textViewArea = (TextView) itemView.findViewById(R.id.textViewArea);
            }
        }
    }
}
