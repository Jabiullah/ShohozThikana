package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class imageAddPage extends AppCompatActivity {
    TextView display,btn_skip;
    String loc_name,loc_house,loc_street,cat,Path;
    String location_serial;
    Button loadImage;

    public static final String UPLOAD_KEY   = "image";
    public static final String LOCATION_KEY = "locationID";

    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private ImageView btnCross;
    private Bitmap bitmap;
    private Uri filePath;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add_page);

        display          = findViewById(R.id.test);
        imageView        = (ImageView) findViewById(R.id.imageView);
        loadImage        = (Button) findViewById(R.id.btn_upload_image);
        btn_skip         = (TextView) findViewById(R.id.skipNow);
        btnCross         = (ImageView) findViewById(R.id.crossImage);

        Bundle bundle_next = getIntent().getExtras();
        if (bundle_next != null) {
            loc_name    = bundle_next.getString("location_name");
            loc_house   = bundle_next.getString("location_house");
            loc_street  = bundle_next.getString("location_street");
            cat         = bundle_next.getString("category");

            Path        = Arrays.toString(bundle_next.getStringArray("voiceFilePath"));
        }
        dataStore();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    imageView.setImageResource(0);
                    loadImage.setVisibility(View.VISIBLE);
                    btn_skip.setVisibility(View.GONE);
                    btnCross.setVisibility(View.VISIBLE);
            }
        });

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(0);
                imageView.setImageResource(R.drawable.addimage);
                loadImage.setVisibility(View.GONE);
                btn_skip.setVisibility(View.VISIBLE);
                btnCross.setVisibility(View.GONE);
            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nextPage done.
                Bundle bundle_geo   =  new Bundle();
                bundle_geo.putString("lastPartOfHouse",loc_house);
                bundle_geo.putString("locationPK",location_serial);
                bundle_geo.putString("voiceFilePath",Path);

                Intent intent_geo = new Intent(imageAddPage.this, GeoLocationCodeGenarate.class);
                intent_geo.putExtras(bundle_geo);
                startActivity(intent_geo);

            }
        });
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(imageAddPage.this, "আপনার জিও লোকেশন কোড সম্পন্ন হচ্ছে।", "Image Uploading...",true,true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //nextPage done.
                Bundle bundle_geo   =  new Bundle();
                bundle_geo.putString("lastPartOfHouse",loc_house);
                bundle_geo.putString("locationPK",location_serial);
                bundle_geo.putString("voiceFilePath",Path);

                Intent intent_geo = new Intent(imageAddPage.this, GeoLocationCodeGenarate.class);
                intent_geo.putExtras(bundle_geo);
                startActivity(intent_geo);
            }
            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                String location_id = location_serial;
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put(LOCATION_KEY,location_id);
                String result = rh.sendPostRequest(URLs.URL_IMAGE,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageResource(0);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);

        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void dataStore() {
        User user                    = SharedPrefManager.getInstance(this).getUser();

        class dataStore extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        final String user_message = obj.getString("message");

                        location_serial = obj.getString("location_serial");

                        //creating a new location object
                        if (user_message.contains("Successful")) {
                            //
                            //display.setText("আপনার সহজ ঠিকানা হচ্ছে - ");

                        }
                        //error check

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid information", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("location_name", loc_name);
                params.put("location_house", loc_house);
                params.put("location_street", loc_street);
                params.put("location_category", cat);
                params.put("user_serial", String.valueOf(user.getId()));

                return requestHandler.sendPostRequest(URLs.URL_LOCATION_INFO_INSERT, params);
            }
        }

        dataStore ds = new dataStore();
        ds.execute();
    }

}