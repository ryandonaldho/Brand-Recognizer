package brand.brandrecognizer;


<<<<<<< HEAD
import android.Manifest;
=======
import android.*;
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
<<<<<<< HEAD
=======
import android.net.Uri;
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
<<<<<<< HEAD
=======
import android.widget.ProgressBar;
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
<<<<<<< HEAD
=======
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
<<<<<<< HEAD
public class MainFragment extends Fragment {
=======
public class MainFragment extends Fragment implements View.OnClickListener {
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da

    private static final String TAG = "MainActivity";

    private static final int RECORD_REQUEST_CODE = 101;
<<<<<<< HEAD
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCSU5gfjgvg6XaA6EgSjMj6Ud1QeOD8Mnc";

    Button takePicture;
    Button getGallery;

    ImageView imageView;

    TextView resultView;
=======
    private static final int SELECT_SINGLE_PICTURE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int SELECT_MULTIPLE_PICTURE = 201;
    public static final String IMAGE_TYPE = "image/*";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCKKR9KzqqTGUzzJysbOr_E2nKdPz-8q7M";

    Button takePicture;
    Button getGallery;
    Button searchImageButton;

    ImageView imageView;

   // TextView resultView;

    ProgressBar progressBar;
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da

    private Bitmap bitmap;
    private Feature feature;
    private Feature feature2;

<<<<<<< HEAD
=======



>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
<<<<<<< HEAD
        View v  = inflater.inflate(R.layout.fragment_main, container, false);
        feature = new Feature();
        feature.setType("LOGO_DETECTION");
=======

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        feature = new Feature();
        feature.setType("LOGO_DETECTION");
        //feature.setType("WEB_DETECTION");
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
        feature.setMaxResults(5);
        feature2 = new Feature();
        feature2.setType("WEB_DETECTION");
        feature2.setMaxResults(5);

<<<<<<< HEAD
        takePicture = (Button) v.findViewById(R.id.take_picture);
        imageView = (ImageView) v.findViewById(R.id.imageView);

        resultView = (TextView) v.findViewById(R.id.textView);

        getGallery = (Button) v.findViewById(R.id.gallery);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
            }
        });

        getGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromPhone();
            }
        });

        return v;
=======

        takePicture = (Button) v.findViewById(R.id.take_picture);
        imageView = (ImageView) v.findViewById(R.id.imageView);

        searchImageButton = (Button) v.findViewById(R.id.searchImage);

        //resultView = (TextView) v.findViewById(R.id.textView);

        getGallery = (Button) v.findViewById(R.id.gallery);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        takePicture.setOnClickListener(this);
        getGallery.setOnClickListener(this);
        searchImageButton.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.take_picture:
                takePictureFromCamera();
                break;
            case R.id.gallery:
                getImageFromPhone();
                break;
            case R.id.searchImage:
                startSearch();
        }
    }

    private void startSearch() {
        callCloudVisionAPI(bitmap, feature);
        System.out.println("clicked");
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
    }

    @Override
    public void onResume() {
        super.onResume();
<<<<<<< HEAD
        if (checkPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture.setVisibility(View.VISIBLE);
        } else {
            takePicture.setVisibility(View.INVISIBLE);
            makeRequest(Manifest.permission.CAMERA);
=======
        if (checkPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture.setVisibility(View.VISIBLE);
        } else {
            takePicture.setVisibility(View.INVISIBLE);
            makeRequest(android.Manifest.permission.CAMERA);
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
        }
    }

    private int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.getActivity(), permission);
    }

    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, RECORD_REQUEST_CODE);
    }

<<<<<<< HEAD
=======
    // test

>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
    public void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void getImageFromPhone(){
<<<<<<< HEAD
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,103);
    }


=======
        //Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(gallery,103);
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
    }

>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
<<<<<<< HEAD
            callCloudVisionAPI(bitmap, feature);
=======
            searchImageButton.setVisibility(View.VISIBLE);
        }

        if(resultCode == RESULT_OK && requestCode == SELECT_SINGLE_PICTURE){
            Uri selectedImageUri = data.getData();
            try {
                imageView.setImageBitmap(new UserPicture(selectedImageUri, getActivity().getContentResolver()).getBitmap());
                bitmap = new UserPicture(selectedImageUri,getActivity().getContentResolver()).getBitmap();
                searchImageButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
            }
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
        }
    }

    private void callCloudVisionAPI(Bitmap bitmap, final Feature feature) {
<<<<<<< HEAD
=======
        progressBar.setVisibility(View.VISIBLE);
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
        List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);
        featureList.add(feature2);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();
        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);

<<<<<<< HEAD
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

=======
        new AsyncTask<Object, Integer, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    publishProgress(20);
                    Thread.sleep(500);
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);
<<<<<<< HEAD

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
=======
                    publishProgress(60);
                    Thread.sleep(500);
                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    publishProgress(100);
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " + e.getMessage());
                }
<<<<<<< HEAD
                return "Cloud Vision API request failed. Check logs for details.";
            }


            @Override
            protected void onPostExecute(String result) {
                Context context = getActivity().getApplicationContext();
                Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                toast.show();
                resultView.setText(result);
=======
                catch (InterruptedException e){
                    e.printStackTrace();

                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setMax(100);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                progressBar.setVisibility(View.GONE );
                Context context = getActivity().getApplicationContext();
                Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                toast.show();
                // resultView.setText(result);
                if (result != "Nothing Found for Logo \n"){
                    Intent intent = new Intent(getActivity(),OptionTabActivity.class);
                    intent.putExtra("brand",result);

                    // put search result into database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    String uuid = currentUser.getUid();
                    myRef.child(uuid).child("searches").push().setValue(result);

                    startActivity(intent);
                }
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
                // imageUploadProgress.setVisibility(View.INVISIBLE);
            }
        }.execute();


    }

    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        AnnotateImageResponse imageResponses = response.getResponses().get(0);
        List<EntityAnnotation> entityAnnotations;
        List<WebEntity> webAnnotations;

        String message = "";
        entityAnnotations = imageResponses.getLogoAnnotations();
        WebDetection test = imageResponses.getWebDetection();
        //webSearchView.setText(formatWebAnnotation(test.getWebEntities()));
        message = formatAnnotation(entityAnnotations);
<<<<<<< HEAD
        if (test != null) // prevent null pointer exception when not webdetection
            if (message == "Nothing Found for Logo \n")
                message += formatWebAnnotation(test.getWebEntities());

=======
        /*
        if (test != null)
            if (message == "Nothing Found for Logo \n")
                message += formatWebAnnotation(test.getWebEntities());
        */
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
<<<<<<< HEAD
                message = message + "    " + entity.getDescription() + " " + entity.getScore();
                message += "\n";
=======
                message += entity.getDescription(); // + " " + entity.getScore();
>>>>>>> 13dcfc8d44480016ebd64c521910dd6d5c0b07da
            }
        } else {
            message = "Nothing Found for Logo \n";
        }
        return message;
    }

    private String formatWebAnnotation(List<WebEntity> entityAnnotation){
        String message = "Web: \n";
        if (entityAnnotation != null ){
            for (WebEntity entity: entityAnnotation){
                message = message + "      " + entity.getDescription() + "  " + entity.getScore();
                message += "\n";
            }
        }
        else{
            message = "Nothing Found";
        }
        return message;
    }

}
