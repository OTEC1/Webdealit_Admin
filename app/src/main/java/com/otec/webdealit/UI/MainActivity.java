package com.otec.webdealit.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.otec.webdealit.R;
import com.otec.webdealit.Retrofit_.Base_config;
import com.otec.webdealit.Retrofit_.Calls;
import com.otec.webdealit.Utils.Find;
import com.otec.webdealit.Utils.utils;
import com.otec.webdealit.model.Music_Response;

import java.io.File;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otec.webdealit.Utils.Constants.BASE_URL_S3;
import static com.otec.webdealit.Utils.Constants.READ_STORAGE_PERMISSION_REQUEST_CODE;
import static com.otec.webdealit.Utils.Constants.SELECT_VIDEO;
import static com.otec.webdealit.Utils.Constants.p1;
import static com.otec.webdealit.Utils.Constants.p2;
import static com.otec.webdealit.Utils.Constants.p3;
import static com.otec.webdealit.Utils.utils.message;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private  ProgressBar progressBar,post_cat_loader;
    private Spinner post_category;
    private EditText title,writeup,youtube;
    private Button chooseImg,upload;
    private TextView progress;
    private  Uri uri;
    private ArrayAdapter adapter;
    private List list;


    private  long back_pressed;
    private  int Time_lapsed = 2000;
    private  String TAG = "MainActivity",tab;
    private  boolean  observe = false;


    @Override
    protected void onResume() {
        super.onResume();
        STRICT_POLICY();
        CHECK_POLICY();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        STRICT_POLICY();
        CHECK_POLICY();
        mapping();
        API_CALL();
        new utils().credentials(getApplicationContext());
        navigationView = findViewById(R.id.bottomNav);
        progressBar = findViewById(R.id.progressBar);
        new utils().bottom_nav(navigationView,this,progressBar);


        chooseImg.setOnClickListener(v -> filePicker());


        post_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tab = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        upload.setOnClickListener(v -> {
            String format =   generate_name() + new UUID(System.currentTimeMillis(),System.nanoTime())+".jpg";
            if(!title.getText().toString().isEmpty() && !writeup.getText().toString().isEmpty() && uri!= null && youtube.getText().toString().trim().isEmpty() && !tab.equals("Select Option"))
                AWS(uri,format,p1,p2,p3,tab,1);
            else
              if(!title.getText().toString().isEmpty() && !writeup.getText().toString().isEmpty() && uri == null && !youtube.getText().toString().trim().isEmpty()  && !tab.equals("Select Option"))
                  AWS(uri,format,p1,p2,p3,tab,2);
            else
                message("Pls fill out  both fields and indicate", getApplicationContext());
        });


    }




    private void AWS(Uri path, String format, String p1, String p2, String p3,String tab,int n){
        upload.setEnabled(false);
           if(n == 1) {
               try {
                    AWSCredentials credentials = new BasicAWSCredentials(p1, p2);
                    AmazonS3 s3 = new AmazonS3Client(credentials);
                    Security.setProperty("networkaddress.cache.ttl", "60");
                    s3.setRegion(Region.getRegion(Regions.EU_WEST_3));
                    TransferUtility transferUtility = new TransferUtility(s3, this);
                    TransferObserver trans = transferUtility.upload(p3, "Pictureuploads/"+format.trim(), new File(Find.get_file_selected_path(path, this)));
                    trans.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {

                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            float percentDone = ((float) bytesCurrent / (float) bytesTotal) * 100;
                            int percentDo = (int) percentDone;
                            progress.setText("Sent= " + percentDo + "%");
                            if(percentDo == 100 && !observe) {
                                sendRequest(format, "", "", "Webdealz/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/" + format, tab);
                                observe = true;
                            }
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            message("S3= " + ex.getLocalizedMessage(), getApplicationContext());
                            if (s3.doesObjectExist(p3, format))
                                s3.deleteObject(p3, format);
                        }
                        });
                   } catch (URISyntaxException e) {
                       Log.d(TAG, "AWS: " + e.getLocalizedMessage());
                   }
                }
                else
                    if(n == 2)
                       sendRequest(format, "", youtube.getText().toString(), "", tab);
    }





    private void sendRequest(String img,String video,String link,String cloud,String tabs) {
        Map<String,Object> m1 = new HashMap<>();
        Map<String,Object> m2 = new HashMap<>();
        Map<String,Object> m3 = new HashMap<>();

        m1.put("username", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        m1.put("user_img", "https://webdealit.s3.eu-west-3.amazonaws.com/logo/Circlebanner.png");
        m1.put("useremail",FirebaseAuth.getInstance().getCurrentUser().getEmail());

        m2.put("image",img);
        m2.put("video",video);
        m2.put("title",title.getText().toString());
        m2.put("writeup",writeup.getText().toString());
        m2.put("youtubeLink",link);
        m2.put("date_time",new Date());
        m2.put("cloudinaryPub",cloud);
        m2.put("orientations",tabs);
        m2.put("timestamp",new Date().getTime());
        m2.put("exifData",0);
        m2.put("views",0);
        m2.put("likes",0);
        m2.put("approved",true);


        m3.put("User",m1);
        m3.put("UserPost",m2);


        API_POST(1,m3,BASE_URL_S3 + img,cloud.replace(".jpg",""));

    }


    private void  API_POST(int n,Map<String,Object> post,String url,String cloud){
        if(n == 1) {
            Calls call = Base_config.getConnection().create(Calls.class);
            Call<Object> obj = call.addPost(post);
            obj.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    upload.setEnabled(true);
                    clear();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    message(t.getLocalizedMessage(), getApplicationContext());
                }
            });


            if (cloud.trim().length() > 0) {
                        Calls caller = Base_config.getConnection().create(Calls.class);
                        Map<String, Object> urls = new HashMap<>();
                        urls.put("url", url);
                        urls.put("publicface", cloud);
                        Call<Object> objs = caller.addimg(urls);
                        objs.enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            message(String.valueOf(response.body()), getApplicationContext());
                            upload.setEnabled(true);
                            clear();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            message(t.getLocalizedMessage(), getApplicationContext());
                        }
                    });
                }
         }
    }


    private  void clear(){
        title.setText(""); writeup.setText(""); youtube.setText("");
    }


    private void API_CALL() {
            Calls call = Base_config.getConnection().create(Calls.class);
            Call<Music_Response> obj = call.get_web_fly_TopList();
            obj.enqueue(new Callback<Music_Response>() {
                @Override
                public void onResponse(Call<Music_Response> call, Response<Music_Response> response) {

                    assert response.body() != null;
                    CarryOn(response.body().getList());
                }

                @Override
                public void onFailure(Call<Music_Response> call, Throwable t) {
                    message(t.getLocalizedMessage(), getApplicationContext());
                    Log.d(TAG, t.getMessage());
                }
            });
        }


    private void CarryOn(List<Object> body) {
        body.add(0,"Select Option");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, body);
        adapter.setDropDownViewResource(R.layout.text_pad);
        adapter.notifyDataSetChanged();
        post_category.setAdapter(adapter);
        post_cat_loader.setVisibility(View.INVISIBLE);
    }


    private void mapping() {
        post_category = findViewById(R.id.post_Category);
        writeup = findViewById(R.id.Writeup);
        title = findViewById(R.id.title);
        upload = findViewById(R.id.upload);
        chooseImg = findViewById(R.id.chooseImg);
        progress = findViewById(R.id.progress);
        post_cat_loader = findViewById(R.id.post_cat_loader);
        youtube = findViewById(R.id.youtubelink);
    }


    public String generate_name() {
        long x = System.currentTimeMillis();
        long q = System.nanoTime();
        return String.valueOf(x).concat(String.valueOf(q));
    }


    private void filePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setDataAndType(uri, "image/*");
            startActivityForResult(intent, SELECT_VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
            assert data != null;
            uri = data.getData();
            if (uri.toString().contains("image")) {
                String path = getFile_extension(uri);
                chooseImg.setText("OK");
                progress.setText("");
            }
        }
    }


    private String getFile_extension(Uri uri) {
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(getApplicationContext().getContentResolver().getType(uri));
    }




    //----------------------------------------------Permission for file sharing ---------------------------------------------//
    //Step 1
    public void STRICT_POLICY() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    //Step 2
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CHECK_POLICY() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else
            request_permission();
    }


    //Step 3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void request_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This Permission is needed for file sharing")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);

        }
    }


    //Step 4
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                message("Permission Granted", this);
            else
                message("Permission Denied", this);

        }

    }
    //----------------------------------------------End of file sharing ---------------------------------------------//


    @Override
    public void onBackPressed() {
        message("Press again to exist ", getApplicationContext());
        if (back_pressed + Time_lapsed > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        back_pressed = System.currentTimeMillis();
    }
}