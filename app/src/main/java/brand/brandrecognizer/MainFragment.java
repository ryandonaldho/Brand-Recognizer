package brand.brandrecognizer;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainActivity";

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCSU5gfjgvg6XaA6EgSjMj6Ud1QeOD8Mnc";

    Button takePicture;
    Button getGallery;

    ImageView imageView;

    TextView resultView;

    private Bitmap bitmap;
    private Feature feature;
    private Feature feature2;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.fragment_main, container, false);
        feature = new Feature();
        feature.setType("LOGO_DETECTION");
        feature.setMaxResults(5);
        feature2 = new Feature();
        feature2.setType("WEB_DETECTION");
        feature2.setMaxResults(5);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture.setVisibility(View.VISIBLE);
        } else {
            takePicture.setVisibility(View.INVISIBLE);
            makeRequest(Manifest.permission.CAMERA);
        }
    }

    private int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.getActivity(), permission);
    }

    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, RECORD_REQUEST_CODE);
    }

    public void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void getImageFromPhone(){
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,103);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            callCloudVisionAPI(bitmap, feature);
        }
    }

    private void callCloudVisionAPI(Bitmap bitmap, final Feature feature) {
        List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);
        featureList.add(feature2);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();
        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);

        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }


            @Override
            protected void onPostExecute(String result) {
                Context context = getActivity().getApplicationContext();
                Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                toast.show();
                resultView.setText(result);
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
        if (test != null) // prevent null pointer exception when not webdetection
            if (message == "Nothing Found for Logo \n")
                message += formatWebAnnotation(test.getWebEntities());

        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                message = message + "    " + entity.getDescription() + " " + entity.getScore();
                message += "\n";
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
