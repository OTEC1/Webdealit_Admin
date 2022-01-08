package com.otec.webdealit.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.otec.webdealit.R;
import com.otec.webdealit.Retrofit_.Base_config;
import com.otec.webdealit.Retrofit_.Calls;
import com.otec.webdealit.Utils.Find;
import com.otec.webdealit.model.Auth;
import com.otec.webdealit.model.SendMovies;
import com.otec.webdealit.model.listOfmoviecategories;

import java.io.File;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.otec.webdealit.Utils.Constants.PICK_IMAGE;
import static com.otec.webdealit.Utils.Constants.READ_STORAGE_PERMISSION_REQUEST_CODE;
import static com.otec.webdealit.Utils.utils.message;


public class Video extends Fragment {


    ProgressBar spinnerProgressBar,mainProgressbar;
    VideoView videoPlayer;
    EditText videoTitle,yearReleased;
    ImageView thumbnail;
    Button chooseVideo,chooseThumbnail,upload,play,pause;
    Spinner spinner;
    ArrayAdapter adapter;
    Uri imgUri;
    TextView progressCount;
    AlertDialog alertDialog;
    View views;


    String TAG = "Video",categories="";
    Map<String, Object> containers;
    int m=0;
    boolean started_payload = false, image_or_video = false;


    @Override
    public void onResume() {
        super.onResume();
        CHECK();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_video, container, false);

        STRICT_POLICY();
        CHECK_POLICY();
        API_MOVIES_LIST_CALL();
        Mapping(view);
        DIALOG();
        containers = new HashMap<>();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categories = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categories="";
            }
        });

        chooseVideo.setOnClickListener(r->{
            file_picker(2);
        });


        chooseThumbnail.setOnClickListener(w->{
            file_picker(1);
        });




        // thumbnail.animate().rotation(m).start();
        upload.setOnClickListener(r->{

               alertDialog.show();
               EditText edit = alertDialog.findViewById(R.id.writeUp);
               Button button = alertDialog.findViewById(R.id.upload);
               button.setOnClickListener(u->{
                   if(!started_payload) {
                       if (videoTitle.getText().toString().trim().isEmpty() || yearReleased.getText().toString().trim().isEmpty()
                               || categories.equals("Select Categories") || containers.size() <=1 || edit.getText().toString().trim().isEmpty())
                           message("Pls fill out all fields and add media files", getContext());
                       else {
                           if (imgUri != null) {
                               started_payload = true;
                               credentials(edit.getText().toString());
                               alertDialog.hide();
                           }else
                               message("Pls select a media file", getActivity());
                       }
                   }else
                       message("Pls wait upload in progress", getContext());
               });

        });


        videoPlayer.setOnClickListener(e->{

            if(videoPlayer.isPlaying()) {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
            }else
                play.setVisibility(View.VISIBLE);


        });


        pause.setOnClickListener(r->{
            videoPlayer.pause();
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
        });


        play.setOnClickListener(u->{
            videoPlayer.start();
                play.setVisibility(View.INVISIBLE);
        });



        return view;
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else
            request_permission();
    }


    //Step 3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void request_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This Permission is needed for file sharing")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);

        }
    }


    //Step 4
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                message("Permission Granted", getActivity());
            else
                message("Permission Denied", getActivity());

        }

    }
    //----------------------------------------------End of file sharing ---------------------------------------------//



    //Step 5
    private void credentials(String writeUp) {
        Calls call = Base_config.getConnection().create(Calls.class);
        Call<Auth> obj = call.getAuth();
        obj.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Map<String,Object> task = response.body().getList2();
                try {
                    send_data_to_s3(writeUp,task.get("p1").toString(), task.get("p2").toString(), task.get("p3").toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                message(t.getLocalizedMessage(), getContext());
            }
        });

    }


    private void CHECK() {

        if(containers!=null){
            if(containers.get("VID")!=null)
                videoPlayer.setVideoURI((Uri) containers.get("VID"));
            if(containers.get("IMG")!=null)
                thumbnail.setImageURI((Uri) containers.get("IMG"));
        }
    }


    private void DIALOG() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        views = layoutInflater.inflate(R.layout.dialog,null);
        AlertDialog.Builder albuild = new AlertDialog.Builder(getContext());
        albuild.setView(views);
        alertDialog = albuild.create();
    }


    private void API_MOVIES_LIST_CALL() {
        Calls call = Base_config.getConnection().create(Calls.class);
        Call<listOfmoviecategories> obj = call.getMovie_categories();
        obj.enqueue(new Callback<listOfmoviecategories>() {
            @Override
            public void onResponse(Call<listOfmoviecategories> call, Response<listOfmoviecategories> response) {

                assert response.body() != null;
                CarryOn(response.body().getList());
            }

            @Override
            public void onFailure(Call<listOfmoviecategories> call, Throwable t) {
                message(t.getLocalizedMessage(), getContext());
                Log.d(TAG,t.getMessage());
            }
        });
    }


    private void CarryOn(List<Object> body) {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,body);
        adapter.setDropDownViewResource(R.layout.text_pad);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinnerProgressBar.setVisibility(View.INVISIBLE);
    }


    private void Mapping(View view) {

        videoPlayer = view.findViewById(R.id.videoPlayer);
        thumbnail = view.findViewById(R.id.thumbnail);
        videoTitle = view.findViewById(R.id.videoTitle);
        yearReleased = view.findViewById(R.id.yearreleased);
        spinner = view.findViewById(R.id.category);
        chooseVideo = view.findViewById(R.id.videoChooser);
        chooseThumbnail = view.findViewById(R.id.thumbnailChooser);
        upload = view.findViewById(R.id.uploader);
        spinnerProgressBar = view.findViewById(R.id.spinnerProgressBar);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        progressCount = view.findViewById(R.id.progressCount);
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);

    }



    private void send_data_to_s3(String writeUp, String p1, String p2, String p3) throws URISyntaxException {

        String fileName = videoTitle.getText().toString().replace(" ","".trim())+"_"+generate_name();
        List<Uri> path = new ArrayList<>();
        path.add((Uri) containers.get("IMG"));
        path.add((Uri) containers.get("VID"));



               for (int x = 0; x < path.size(); x++) {
                    AWSCredentials credentials = new BasicAWSCredentials(p1, p2);
                    AmazonS3 s3 = new AmazonS3Client(credentials);
                    Security.setProperty("networkaddress.cache.ttl", "60");
                    s3.setRegion(Region.getRegion(Regions.EU_WEST_3));
                    //s3.setObjectAcl("", ".png", CannedAccessControlList.PublicRead);
                    TransferUtility transferUtility = new TransferUtility(s3, getActivity());
                    String d = Find.get_file_selected_path(path.get(x), getActivity());
                    String format = (x == 0) ?  "Stream_Thumbnails/"+fileName+".png" : "Stream_Videouploads/"+fileName+".mp4";
                    TransferObserver trans = transferUtility.upload(p3, format.trim(), new File(d));
                    trans.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {

                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            float percentDone = ((float) bytesCurrent / (float) bytesTotal) * 100;
                            int percentDo = (int) percentDone;

                            progressCount.setText("Uploading... " + percentDo+"%");
                            if (percentDo == 100) {
                                progressCount.setText("Uploaded");
                                if(started_payload)
                                    send_payload(writeUp,fileName);
                                videoTitle.setText("");
                                yearReleased.setText("");
                                started_payload = false;
                                mainProgressbar.setVisibility(View.GONE);

                            }
                        }



                        @Override
                        public void onError(int id, Exception ex) {
                            message(ex.getLocalizedMessage(), getActivity());
                            mainProgressbar.setVisibility(View.GONE);
                            started_payload = false;

                        }

                    });

             }
    }



    public int getCameraRotation(Context context, Uri uri, String path){
        int rotate = 0;
        try{
            context.getContentResolver().notifyChange(uri,null);
            File file = new File(path);

            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation  = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate =90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
            }
            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);

        }catch (Exception e){
            Log.d(TAG, "getCameraRotation: "+e.getLocalizedMessage());

        }
        return  rotate;
    }



    private void send_payload(String writeUp, String fileName) {
        EchoToAPI(videoTitle.getText().toString(), yearReleased.getText().toString(), categories, writeUp, fileName);

    }


    private  void EchoToAPI(String Mtitle, String year, String categories, String writeUp, String fileName){

        Calls sendMovies =  Base_config.getConnection().create(Calls.class);
        SendMovies send = new SendMovies(Mtitle,year,categories,writeUp,fileName,0);
        Call<SendMovies> sendMoviesCall =  sendMovies.sendMovie(send);
        sendMoviesCall.enqueue(new Callback<SendMovies>() {
            @Override
            public void onResponse(Call<SendMovies> call, Response<SendMovies> response) {
                 final String x;
                if(response.isSuccessful())
                    x ="Movie Added";
                else
                    x= "Error Occurred !";

                message(x,getContext());
            }

            @Override
            public void onFailure(Call<SendMovies> call, Throwable t) {
                message(t.getLocalizedMessage(),getContext());

            }
        });
    }


    //Media selector  Custom ui
    public void file_picker(int v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if (v == 1)
           intent.setDataAndType(imgUri,"image/*");
        else
            intent.setDataAndType(imgUri,"video/*");

        startActivityForResult(intent, PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            assert data != null;
                imgUri = data.getData();
                assert imgUri != null;
                if (imgUri.toString().contains("image")) {
                    thumbnail.setImageURI(imgUri);
                    image_or_video = true;
                    progressCount.setText("");
                    containers.put("IMG",imgUri);
                    try {
                      message(String.valueOf(getCameraRotation(getContext(),imgUri,Find.get_file_selected_path(imgUri, getActivity()))),getContext());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
                else
                    if (imgUri.toString().contains("video")) {
                        videoPlayer.setVideoURI(imgUri);
                        image_or_video = true;
                        progressCount.setText("");
                        containers.put("VID",imgUri);

                    }
               else
                  message("Pls Select an Image.", getActivity());
        }
    }


    public String generate_name() {
        long x = System.currentTimeMillis();
        long q = System.nanoTime();
        return String.valueOf(x).concat(String.valueOf(q));
    }

}