package com.example.nasrul.presensi_VUB;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextAbsenId, editTextTgl;
    Spinner spinnerKls, spinnerMk, spinnerNama, spinnerKet;
    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate, buttonReset, buttonLogout;

    List<Absen> absenList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAbsenId = (EditText) findViewById(R.id.editTextAbsenId);
        editTextTgl = (EditText) findViewById(R.id.spinnerTanggal);

        spinnerKls = (Spinner) findViewById(R.id.spinnerKelas);
        spinnerMk = (Spinner) findViewById(R.id.spinnerMapel);
        spinnerNama = (Spinner) findViewById(R.id.spinnerNama);
        spinnerKet = (Spinner) findViewById(R.id.spinnerKeterangan);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);
        buttonReset = (Button) findViewById(R.id.reset);
        buttonLogout = (Button) findViewById(R.id.logout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewAbsen);

        absenList = new ArrayList<>();


        editTextTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    updateAbsen();
                } else {
                    createAbsen();
                }
            }
        });
        readAbsen();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTgl.setText("");
                spinnerKls.setSelection(0);
                spinnerMk.setSelection(0);
                spinnerNama.setSelection(0);
                spinnerKet.setSelection(0);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String date = dayOfMonth + "/" + month + "/" + year;
        editTextTgl.setText(date);
    }


    private void createAbsen() {
        String tanggal = editTextTgl.getText().toString().trim();
        String kelas = spinnerKls.getSelectedItem().toString();
        String mapel = spinnerMk.getSelectedItem().toString();
        String nama = spinnerNama.getSelectedItem().toString();
        String keterangan = spinnerKet.getSelectedItem().toString();

        /* if (TextUtils.isEmpty(judul)) {
            editTextJudul.setError("Please enter judul");
            editTextJudul.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(penulis)) {
            editTextPenulis.setError("Please enter penulis");
            editTextPenulis.requestFocus();
            return;
        } */

        HashMap<String, String> params = new HashMap<>();
        params.put("tanggal", tanggal);
        params.put("kelas", kelas);
        params.put("mapel", mapel);
        params.put("nama", nama);
        params.put("keterangan", keterangan);

       PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ABSEN, params, CODE_POST_REQUEST);
       request.execute();
    }

    private void readAbsen() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ABSEN, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateAbsen() {
        String id = editTextAbsenId.getText().toString();
        String tanggal = editTextTgl.getText().toString().trim();
        String kelas = spinnerKls.getSelectedItem().toString();
        String mapel = spinnerMk.getSelectedItem().toString();
        String nama = spinnerNama.getSelectedItem().toString();
        String keterangan = spinnerKet.getSelectedItem().toString();


        /*if (TextUtils.isEmpty(judul)) {
            editTextJudul.setError("Please enter judul");
            editTextJudul.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(penulis)) {
            editTextPenulis.setError("Please enter penulis");
            editTextPenulis.requestFocus();
            return;
        } */

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("tanggal", tanggal);
        params.put("kelas", kelas);
        params.put("mapel", mapel);
        params.put("nama", nama);
        params.put("keterangan", keterangan);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ABSEN, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Add");

        editTextTgl.setText("");
        spinnerKls.setSelection(0);
        spinnerMk.setSelection(0);
        spinnerNama.setSelection(0);
        spinnerKet.setSelection(0);

        isUpdating = false;
    }

    private void deleteAbsen(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_ABSEN + id, null, CODE_GET_REQUEST);
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

        AbsenAdapter adapter = new AbsenAdapter(absenList);
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
            super(MainActivity.this, R.layout.layout_list_absen, absenList);
            this.absenList = absenList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_list_absen, null, true);

            TextView textViewNama = listViewItem.findViewById(R.id.textViewNama);
            TextView textViewTgl = listViewItem.findViewById(R.id.textViewTgl);
            TextView textViewKet = listViewItem.findViewById(R.id.textViewKet);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Absen absen = absenList.get(position);

            textViewNama.setText(absen.getNama());
            textViewTgl.setText(absen.getTanggal());
            textViewKet.setText(absen.getKeterangan());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdating = true;
                    editTextAbsenId.setText(String.valueOf(absen.getId()));
                    editTextTgl.setText(absen.getTanggal());
                    spinnerKls.setSelection(((ArrayAdapter<String>) spinnerKls.getAdapter()).getPosition(absen.getKelas()));
                    spinnerMk.setSelection(((ArrayAdapter<String>) spinnerMk.getAdapter()).getPosition(absen.getMapel()));
                    spinnerNama.setSelection(((ArrayAdapter<String>) spinnerNama.getAdapter()).getPosition(absen.getNama()));
                    spinnerKet.setSelection(((ArrayAdapter<String>) spinnerKet.getAdapter()).getPosition(absen.getKeterangan()));
                    buttonAddUpdate.setText("Update");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Delete " + absen.getNama())
                            .setMessage("Are you sure you want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAbsen(absen.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }
    }
}
