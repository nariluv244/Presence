package com.example.nasrul.presensi_VUB;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class DaftarHadirActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ProgressBar progressBar;
    ListView listView;
    List<Absen> absenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_hadir);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewDaftar);
        absenList = new ArrayList<>();

        readAbsen();
    }

    private void readAbsen() {
        DaftarHadirActivity.PerformNetworkRequest request = new DaftarHadirActivity.PerformNetworkRequest(Api.URL_READ_ABSEN, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshAbsenList(JSONArray absenku) throws JSONException {
        absenList.clear();

        for (int i = 0; i < absenku.length(); i++) {
            JSONObject obj = absenku.getJSONObject(i);

            absenList.add(new Absen(
                    obj.getInt("id"),
                    obj.getString("tanggal"),
                    obj.getString("kelas"),
                    obj.getString("mapel"),
                    obj.getString("nama"),
                    obj.getString("keterangan")
            ));
        }

        DaftarHadirActivity.AbsenAdapter adapter = new DaftarHadirActivity.AbsenAdapter(absenList);
        listView.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshAbsenList(object.getJSONArray("absen"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class AbsenAdapter extends ArrayAdapter<Absen> {
        List<Absen> absenList;

        public AbsenAdapter(List<Absen> absenList) {
            super(DaftarHadirActivity.this, R.layout.layout_list_daftar, absenList);
            this.absenList = absenList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_list_daftar, null, true);

            TextView textViewNama = listViewItem.findViewById(R.id.textViewNama);
            TextView textViewTgl = listViewItem.findViewById(R.id.textViewTgl);
            TextView textViewMapel = listViewItem.findViewById(R.id.textViewMapel);
            TextView textViewKet = listViewItem.findViewById(R.id.textViewKet);

            final Absen absen = absenList.get(position);

            textViewNama.setText(absen.getNama());
            textViewTgl.setText(absen.getTanggal());
            textViewMapel.setText(absen.getMapel());
            textViewKet.setText(absen.getKeterangan());

            return listViewItem;
        }
    }
}
