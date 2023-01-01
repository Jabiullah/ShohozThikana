package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GeoList extends AppCompatActivity {
    private static final int CODE_POST_REQUEST = 1025;
    private static final int CODE_GET_REQUEST = 1024;
    //audio Record
    private static int MICRROPHONE_PERMISSION_CODE = 200;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    List<GeoLocationDetails> GeoLocationList;
    RecyclerView recyclerView;
    User user = SharedPrefManager.getInstance(this).getUser();

    FloatingActionButton btn_home;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGEOLIST);
        btn_home     = findViewById(R.id.homeReturn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GeoLocationList = new ArrayList<>();
        load_GEOLOCATION_list();

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(GeoList.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(GeoList.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private void deleteGEOCODE(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_GEOCODE_DELETE+id, null, CODE_GET_REQUEST);
        request.execute();
        refreshList();
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
            //progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

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
    private void refreshList(){
        GeoLocationList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GEOCODE_DISPLAY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                GeoLocationList.add(new GeoLocationDetails(
                                        n.getString("voiceLINK_ser"),
                                        n.getInt("GEO_ID"),
                                        n.getString("geocode"),
                                        n.getString("locationName"),
                                        n.getString("locationHouse"),
                                        n.getString("locationStreet"),
                                        n.getString("locationCategory")
                                ));
                            }
                            GeoLocationAdapter adapter = new GeoLocationAdapter(GeoList.this, GeoLocationList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void load_GEOLOCATION_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GEOCODE_DISPLAY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                GeoLocationList.add(new GeoLocationDetails(
                                        n.getString("voiceLINK_ser"),
                                        n.getInt("GEO_ID"),
                                        n.getString("geocode"),
                                        n.getString("locationName"),
                                        n.getString("locationHouse"),
                                        n.getString("locationStreet"),
                                        n.getString("locationCategory")
                                ));
                            }
                            GeoLocationAdapter adapter = new GeoLocationAdapter(GeoList.this, GeoLocationList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public class GeoLocationAdapter extends RecyclerView.Adapter<GeoLocationAdapter.GeoLocationDetailsViewHolder>{
        private Context mCtx;
        private List<GeoLocationDetails> geoLocationList;

        public GeoLocationAdapter(Context mCtx, List<GeoLocationDetails> geoLocationList) {
            this.mCtx = mCtx;
            this.geoLocationList = geoLocationList;
        }
        @NonNull
        @Override
        public GeoLocationAdapter.GeoLocationDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.geolist, null);
            return new GeoLocationAdapter.GeoLocationDetailsViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull GeoLocationAdapter.GeoLocationDetailsViewHolder holder, int position) {
            GeoLocationDetails n1 = geoLocationList.get(position);
            holder.GeoCode.setText(n1.getGeocode());
            holder.address.setText(n1.getLocationHouse() + " , " + n1.getLocationName() + " , " + n1.getLocationStreet());
            holder.houseCat.setText(n1.getLocationCategory());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GeoList.this);
                    builder.setTitle("Delete " + n1.getGeocode()).setMessage("Are you sure you want to delete it?\nIt will also delete all information from shared audience and location info.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteGEOCODE(n1.getGEO_ID());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();

                }
            });
            holder.viewAudience.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("GEO_ID", String.valueOf(n1.getGEO_ID()));
                    Intent aud_intent = new Intent(GeoList.this, showAudience.class);
                    aud_intent.putExtras(bundle);
                    startActivity(aud_intent);
                    finish();
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundleAnother = new Bundle();
                    bundleAnother.putString("GEO_ID", String.valueOf(n1.getGEO_ID()));
                    bundleAnother.putString("GEOCODE",n1.getGeocode());
                    Intent share_intent = new Intent(GeoList.this, ShareCode.class);
                    share_intent.putExtras(bundleAnother);
                    startActivity(share_intent);
                    finish();
                }
            });
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.play.getText().toString().equals("অডিও নির্দেশাবলী চালান")){
                        holder.play.setText("অডিও এখন চলছে");
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource((n1.getVoiceLINK_ser()));
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(holder.play.getText().toString().equals("অডিও এখন চলছে")){
                        holder.play.setText("অডিও নির্দেশাবলী চালান");
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }

                }
            });


        }

        @Override
        public int getItemCount() {return geoLocationList.size();}
        public class GeoLocationDetailsViewHolder extends RecyclerView.ViewHolder {
            TextView GeoCode,address, voiceFile, houseCat;
            ImageView edit , delete , share , viewAudience;
            Button play;
            public GeoLocationDetailsViewHolder(View itemView) {
                super(itemView);
                play        = itemView.findViewById(R.id.voiceFile);
                GeoCode     = itemView.findViewById(R.id.geoCodeValue);
                address     = itemView.findViewById(R.id.address);
                houseCat    = itemView.findViewById(R.id.houseCategory);
                delete      = itemView.findViewById(R.id.Delete);
                viewAudience= itemView.findViewById(R.id.Audience);
                share       = itemView.findViewById(R.id.Share);
            }
        }

    }

}