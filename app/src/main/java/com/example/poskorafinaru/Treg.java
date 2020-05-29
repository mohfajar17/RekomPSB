package com.example.poskorafinaru;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Treg implements Parcelable {

    private String witel;
    private String area;
    private String ubis;
    private String sto;
    private String jml_ok;
    private String jml_nok;
    private String prs;
    private String tanggal;

    public Treg (String witel, String area, String ubis, String sto, String jml_ok, String jml_nok, String prs, String tanggal){
        this.witel = witel;
        this.area = area;
        this.ubis = ubis;
        this.sto = sto;
        this.jml_ok = jml_ok;
        this.jml_nok = jml_nok;
        this.prs = prs;
        this.tanggal = tanggal;
    }

    protected Treg(Parcel in) {
        witel = in.readString();
        area = in.readString();
        ubis = in.readString();
        sto = in.readString();
        jml_ok = in.readString();
        jml_nok = in.readString();
        prs = in.readString();
        tanggal = in.readString();
    }

    public Treg(JSONObject jsonObject){
        try {
            this.witel = jsonObject.getString("witel");
            this.area = jsonObject.getString("area");
            this.ubis = jsonObject.getString("ubis");
            this.sto = jsonObject.getString("sto");
            this.jml_ok = jsonObject.getString("jml_ok");
            this.jml_nok = jsonObject.getString("jml_nok");
            this.prs = jsonObject.getString("prs");
            this.tanggal = jsonObject.getString("tanggal");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Treg> CREATOR = new Creator<Treg>() {
        @Override
        public Treg createFromParcel(Parcel in) {
            return new Treg(in);
        }

        @Override
        public Treg[] newArray(int size) {
            return new Treg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(witel);
        dest.writeString(area);
        dest.writeString(ubis);
        dest.writeString(sto);
        dest.writeString(jml_ok);
        dest.writeString(jml_nok);
        dest.writeString(prs);
        dest.writeString(tanggal);
    }

    public String getWitel() {
        return witel;
    }

    public String getArea() {
        return area;
    }

    public String getUbis() {
        return ubis;
    }

    public String getSto() {
        return sto;
    }

    public String getJml_ok() {
        return jml_ok;
    }

    public String getJml_nok() {
        return jml_nok;
    }

    public String getPrs(){
        return prs;
    }

    public String getTanggal() {
        return tanggal;
    }
}
