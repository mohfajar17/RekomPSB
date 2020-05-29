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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AreaFragment extends Fragment {

    private int mColumnCount = 1;
    private String datePick1;
    private String datePick2;
    private TextView datePickArea1;
    private TextView datePickArea2;
    private TextView areaTregOk;
    private TextView areaTregNok;
    private TextView areaTregPer;
    private TableRow rowAreaTreg;
    private Button buttonArea;
    private RecyclerView recyclerView;
    private SharedPrefManager sharedPrefManager;

    private ProgressDialog progressDialog;

    private OnListFragmentInteractionListener mListener;

    public AreaFragment() {
    }

    public static AreaFragment newInstance() {
        AreaFragment fragment = new AreaFragment();
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
        final View view = inflater.inflate(R.layout.fragment_area, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Refresh Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        sharedPrefManager = SharedPrefManager.getInstance(getActivity());

        areaTregOk = (TextView) view.findViewById(R.id.areaTregOk);
        areaTregNok = (TextView) view.findViewById(R.id.areaTregNok);
        areaTregPer = (TextView) view.findViewById(R.id.areaTregPer);
        datePickArea1 = (TextView) view.findViewById(R.id.datePickArea1);
        datePickArea2 = (TextView) view.findViewById(R.id.datePickArea2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewArea);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
        }

        datePick1 = sharedPrefManager.getDatePick1();
        datePick2 = sharedPrefManager.getDatePick2();
        datePickArea1.setText(sharedPrefManager.getTextDatePick1());
        datePickArea2.setText(sharedPrefManager.getTextDatePick2());

        datePickArea1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });
        datePickArea2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        buttonArea = (Button) view.findViewById(R.id.buttonArea);
        buttonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                setDataTreg();
            }
        });

        rowAreaTreg = (TableRow) view.findViewById(R.id.rowAreaTreg);
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

        setData();
        setDataTreg();

        return view;
    }

    private void setData() {
        progressDialog.show();

        final String firstDate = String.valueOf(datePick1);
        final String secondtDate = String.valueOf(datePick2);
        final List<Treg> tregs = new ArrayList<>();

        if (Integer.valueOf(datePick1)>=Integer.valueOf(datePick2)){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Date is incorrect", Toast.LENGTH_LONG).show();
        } else {
            sharedPrefManager.setDatePick1(datePick1);
            sharedPrefManager.setTextDatePick1(String.valueOf(datePickArea1.getText()));
            sharedPrefManager.setDatePick2(datePick2);
            sharedPrefManager.setTextDatePick2(String.valueOf(datePickArea2.getText()));

            StringRequest request = new StringRequest(Request.Method.GET,
                    Config.DATA_URL+"param=area&witel=&firstDate="+firstDate+"&secondtDate="+secondtDate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();

                                JSONArray jsonArrayTreg = new JSONArray(response);
                                for(int i=0;i<jsonArrayTreg.length();i++){
                                    tregs.add(new Treg(jsonArrayTreg.getJSONObject(i)));
                                }
                                recyclerView.setAdapter(new AreaFragment.MyMainRecyclerViewAdapter(tregs, mListener));
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

    private void setDataTreg() {

        final String firstDate = String.valueOf(datePick1);
        final String secondtDate = String.valueOf(datePick2);

        if (Integer.valueOf(datePick1)<Integer.valueOf(datePick2)){
            StringRequest request = new StringRequest(Request.Method.GET,
                    Config.DATA_URL+"param=treg&witel=&firstDate="+firstDate+"&secondtDate="+secondtDate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArrayTreg = new JSONArray(response);
                                DecimalFormat percent = new DecimalFormat("#.#");
                                Double per = Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_ok"))/(Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_ok"))+Double.valueOf(jsonArrayTreg.getJSONObject(0).getString("jml_nok")))*100;
                                areaTregOk.setText(jsonArrayTreg.getJSONObject(0).getString("jml_ok"));
                                areaTregNok.setText(jsonArrayTreg.getJSONObject(0).getString("jml_nok"));
                                areaTregPer.setText(percent.format(per)+"%");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Volley.newRequestQueue(getActivity()).add(request);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AreaFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dp==0) {
                    datePickArea1.setText("26-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick1 =  year + "0" + (month + 1);
                    else datePick1 =  "" + year + (month + 1);
                } else {
                    datePickArea2.setText("25-" + (month + 1) + "-" + year);
                    if (month+1<10)
                        datePick2 =  year + "0" +(month + 1);
                    else datePick2 =  "" + year +(month + 1);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private class MyMainRecyclerViewAdapter extends RecyclerView.Adapter<MyMainRecyclerViewAdapter.ViewHolder>{

        private final List<Treg> mValues;
        private final OnListFragmentInteractionListener mListener;

        private MyMainRecyclerViewAdapter(List<Treg> mValues, OnListFragmentInteractionListener mListener) {
            this.mValues = mValues;
            this.mListener = mListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_area_list, parent, false);
            return new MyMainRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyMainRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.textViewArea.setText(mValues.get(position).getArea());
            holder.textViewAreaOk.setText(mValues.get(position).getJml_ok());
            holder.textViewAreaNok.setText(mValues.get(position).getJml_nok());
            holder.textViewAreaPer.setText(mValues.get(position).getPrs()+"%");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new WitelFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();

                    bundle.putString("datePick1", datePick1);
                    bundle.putString("datePick2", datePick2);
                    bundle.putString("textViewDate1", datePickArea1.getText().toString());
                    bundle.putString("textViewDate2", datePickArea2.getText().toString());
                    bundle.putString("spinner", mValues.get(position).getArea());
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

            public final TextView textViewArea;
            public final TextView textViewAreaOk;
            public final TextView textViewAreaNok;
            public final TextView textViewAreaPer;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                textViewArea = (TextView) itemView.findViewById(R.id.textViewArea);
                textViewAreaOk = (TextView) itemView.findViewById(R.id.textViewAreaOk);
                textViewAreaNok = (TextView) itemView.findViewById(R.id.textViewAreaNok);
                textViewAreaPer = (TextView) itemView.findViewById(R.id.textViewAreaPer);
            }
        }
    }
}
