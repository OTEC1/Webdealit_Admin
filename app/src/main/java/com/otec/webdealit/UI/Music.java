package com.otec.webdealit.UI;


import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
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
import com.otec.webdealit.model.Music_Response;
import com.otec.webdealit.model.Music_Upload;

import java.io.File;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.otec.webdealit.Utils.Constants.SELECT_VIDEO;
import static com.otec.webdealit.Utils.utils.message;


public class Music extends Fragment {
    private ProgressBar mainProgressbar, country_loader, Genre_loader;
    private Spinner Genrespinner, countrySpinner;
    private Button upload_btn, music_chooser, music_thumbnail, play, pause;
    private EditText musicTitle, musicYear, musicArtist;
    private TextView progressCount, musicIndicator;
    private ArrayAdapter genre, country;
    private VideoView videoPlayer;
    private Uri uri;


    private String TAG = "Music", genreStringSelector;
    private Map<String, Object> containers = new HashMap<>();
    private boolean image_or_video, started_payload = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        Mapping(view);
        GENRE_CALL();



        music_chooser.setOnClickListener(e -> {

            if (music_chooser.getText().toString().equals("Music")) {
                filePicker("Music");
                music_chooser.setText("Video");
            } else {
                filePicker("Video");
            }
        });


        videoPlayer.setOnClickListener(e -> {
            pause.setVisibility(View.VISIBLE);
        });


        play.setOnClickListener(e -> {
            videoPlayer.setVideoURI((Uri) containers.get("Vid"));
            videoPlayer.start();
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onCreateView: " + containers.get("Vid") + "   " + containers.get("Mus") + " " + containers.get("Img"));
        });


        pause.setOnClickListener(e -> {
            videoPlayer.pause();
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
        });


        music_thumbnail.setOnClickListener(e -> {
            filePicker("image");
        });


        Genrespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genreStringSelector = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        upload_btn.setOnClickListener(e -> {

            if (image_or_video){
                if (!started_payload) {
                    if (!musicTitle.getText().toString().trim().isEmpty() && !musicArtist.getText().toString().trim().isEmpty() && !musicYear.getText().toString().trim().isEmpty()) {
                        if (!genreStringSelector.equals("Select Music Genre")) {
                            mainProgressbar.setVisibility(View.VISIBLE);
                            started_payload = true;
                            credentials();
                        } else
                            message("Pls select music Genre", getActivity());
                    } else
                        message("Pls fill out all fields !", getActivity());
                }else
                    message("Pls wait upload in progress ", getActivity());
        }else
                message("Pls add all media files ", getActivity());

        });
        return view;
    }


    public String generate_name() {
        long x = System.currentTimeMillis();
        long q = System.nanoTime();
        return String.valueOf(x).concat(String.valueOf(q));
    }


    private void filePicker(String url) {
        Intent intent;
        if (url.equals("Music")) {
            intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                startActivityForResult(Intent.createChooser(intent, "Choose audio file"), SELECT_VIDEO);
            else
                message("Unsupported Device ", getContext());
        } else if (url.equals("Video")) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setDataAndType(uri, "video/*");
            startActivityForResult(intent, SELECT_VIDEO);

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setDataAndType(uri, "image/*");
            startActivityForResult(intent, SELECT_VIDEO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
            assert data != null;
            uri = data.getData();
            if (uri.toString().contains("audio")) {
                String path = getFile_extension(uri);
                containers.put("Mus", uri);
                musicIndicator.setText("Music file added ");
                progressCount.setText("");
                Log.d(TAG, "onActivityResult: " + path);

            } else if (uri.toString().contains("video")) {
                music_chooser.setText("OK");
                containers.put("Vid", uri);
                progressCount.setText("");


            } else {
                if (containers.get("Mus") != null && containers.get("Vid") != null) {
                    image_or_video = true;
                    music_thumbnail.setText("OK");
                    containers.put("Img", uri);
                }

            }
        }
    }


    private String getFile_extension(Uri uri) {
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }


    private void credentials() {
        Calls call = Base_config.getConnection().create(Calls.class);
        Call<Auth> obj = call.getAuth();
        obj.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Map<String, Object> task = response.body().getList2();
                try {
                    send_data_to_s3(musicArtist.getText().toString(), Integer.parseInt(musicYear.getText().toString()), musicTitle.getText().toString(), task.get("p1").toString(), task.get("p2").toString(), task.get("p3").toString());
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


    private void GENRE_CALL() {
        Calls call = Base_config.getConnection().create(Calls.class);
        Call<Music_Response> obj = call.getGenre();
        obj.enqueue(new Callback<Music_Response>() {
            @Override
            public void onResponse(Call<Music_Response> call, Response<Music_Response> response) {

                assert response.body() != null;
                CarryOn(response.body().getList());
            }

            @Override
            public void onFailure(Call<Music_Response> call, Throwable t) {
                message(t.getLocalizedMessage(), getContext());
                Log.d(TAG, t.getMessage());
            }
        });
    }


    private void CarryOn(List<Object> body) {
        genre = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, body);
        genre.setDropDownViewResource(R.layout.text_pad);
        genre.notifyDataSetChanged();
        Genrespinner.setAdapter(genre);
        Genre_loader.setVisibility(View.INVISIBLE);
    }


    private void send_data_to_s3(String artist, int year, String title, String p1, String p2, String p3) throws URISyntaxException {


        String fileName = title.replace(" ","_") + "_" + generate_name();
        List<Uri> path = new ArrayList<>();
        path.add((Uri) containers.get("Img"));
        path.add((Uri) containers.get("Vid"));
        path.add((Uri) containers.get("Mus"));


        for (int x = 0; x < path.size(); x++) {
            AWSCredentials credentials = new BasicAWSCredentials(p1, p2);
            AmazonS3 s3 = new AmazonS3Client(credentials);
            Security.setProperty("networkaddress.cache.ttl", "60");
            s3.setRegion(Region.getRegion(Regions.EU_WEST_3));
            //s3.setObjectAcl("", ".png", CannedAccessControlList.PublicRead);
            TransferUtility transferUtility = new TransferUtility(s3, getActivity());
            String format = "";
            final int xb = x;
            if (xb == 0)
                format = "Music_Thumbnails/" + fileName + ".png";
            else if (xb == 1)
                format = "Music_Videouploads/" + fileName + ".mp4";
            else
                format = "Music_fileuploads/" + fileName + ".mp3";

            TransferObserver trans = transferUtility.upload(p3, format.trim(), new File(Find.get_file_selected_path(path.get(xb), getActivity())));
            String finalFormat = format;
            trans.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDone = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDo = (int) percentDone;
                    progressCount.setText("Uploading... " + percentDo + "%");
                        if (started_payload) {
                            send_payload(artist, year, title,finalFormat);
                            if (xb == 0)
                                progressCount.setText("Image uploaded Pls wait video file uploading...");
                            else if (xb == 1)
                                progressCount.setText("Music video uploaded Pls wait music file uploading...");
                            else {
                                progressCount.setText("Upload complete");
                                musicTitle.setText("");
                                musicYear.setText("");
                                musicArtist.setText("");
                            }
                        }
                        started_payload = false;
                        mainProgressbar.setVisibility(View.GONE);

                }


                @Override
                public void onError(int id, Exception ex) {
                    message("S3= " + ex.getLocalizedMessage(), getActivity());
                    mainProgressbar.setVisibility(View.GONE);
                    started_payload = false;
                    if (s3.doesObjectExist(p3, finalFormat))
                        s3.deleteObject(p3, finalFormat);

                }

            });

        }
    }


    private void send_payload(String artist, int year, String title, String format) {

        Map<String,Object> music = new HashMap<>();
        music.put("name","Admin");
        music.put("email","Admin@gmail.com");
        music.put("music_thumbnail",format);
        music.put("music_video",format.replace("Music_Thumbnails","Music_Videouploads").replace(".png",".mp4").trim());
        music.put("music_title",title.toLowerCase());
        music.put("music_url",format.replace("Music_Thumbnails","Music_fileuploads").replace(".png",".mp3").trim());
        music.put("music_artist", artist);
        music.put("music_year",year);
        music.put("downloadCount",0);
        music.put("viewCount",0);
        music.put("userType","admin");
        music.put("flag",true);
        Map<String,Object> musicObj = new HashMap<>();
        musicObj.put("Music",music);
        SEND_MUSIC(musicObj);

    }


    private void SEND_MUSIC(Map<String, Object> musicObj) {
        Calls call = Base_config.getConnection().create(Calls.class);
        Call<Music_Upload> obj = call.sendMusic(musicObj);
        obj.enqueue(new Callback<Music_Upload>() {
            @Override
            public void onResponse(Call<Music_Upload> call, Response<Music_Upload> response) {

                assert response.body() != null;
                message(response.body().getResponse().toString(),getContext());
            }

            @Override
            public void onFailure(Call<Music_Upload> call, Throwable t) {
                message(t.getLocalizedMessage(), getContext());
                Log.d(TAG, t.getMessage());
            }
        });
    }





    public int getCameraRotation(Context context, Uri uri, String path) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(uri, null);
            File file = new File(path);

            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
            }

        } catch (Exception e) {
            Log.d(TAG, "getCameraRotation: " + e.getLocalizedMessage());

        }
        return rotate;
    }

    private void Mapping(View view) {
        upload_btn = view.findViewById(R.id.upload);
        music_chooser = view.findViewById(R.id.chooseMusic);
        music_thumbnail = view.findViewById(R.id.thumbnail);
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        videoPlayer = view.findViewById(R.id.musicThumbnailView);
        progressCount = view.findViewById(R.id.musicThumb);
        musicIndicator = view.findViewById(R.id.musicPathIndicator);
        musicArtist = view.findViewById(R.id.music_artist);
        musicTitle = view.findViewById(R.id.music_title);
        musicYear = view.findViewById(R.id.music_year);
        Genrespinner = view.findViewById(R.id.Genre);
        countrySpinner = view.findViewById(R.id.Country);
        Genre_loader = view.findViewById(R.id.Genre_loader);
        country_loader = view.findViewById(R.id.country_loader);
        mainProgressbar = view.findViewById(R.id.main_loader);
    }


}