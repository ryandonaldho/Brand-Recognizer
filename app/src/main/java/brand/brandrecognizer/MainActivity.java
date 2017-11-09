package brand.brandrecognizer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import static android.R.attr.bitmap;
import static android.R.attr.progress;
import static android.R.id.message;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int SELECT_SINGLE_PICTURE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int SELECT_MULTIPLE_PICTURE = 201;
    public static final String IMAGE_TYPE = "image/*";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCKKR9KzqqTGUzzJysbOr_E2nKdPz-8q7M";

    Button takePicture;
    Button getGallery;

    ImageView imageView;

    TextView resultView;

    ProgressBar progressBar;

    private Bitmap bitmap;
    private Feature feature;
    private Feature feature2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feature = new Feature();
        feature.setType("LOGO_DETECTION");
        //feature.setType("WEB_DETECTION");
        feature.setMaxResults(5);
        feature2 = new Feature();
        feature2.setType("WEB_DETECTION");
        feature2.setMaxResults(5);

        takePicture = (Button) findViewById(R.id.take_picture);
        imageView = (ImageView) findViewById(R.id.imageView);

        resultView = (TextView) findViewById(R.id.textView);

        getGallery = (Button) findViewById(R.id.gallery);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture.setVisibility(View.VISIBLE);
        } else {
            takePicture.setVisibility(View.INVISIBLE);
            makeRequest(Manifest.permission.CAMERA);
        }
    }

    private int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }

    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
    }

    // test

    public void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void getImageFromPhone(){
        //Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(gallery,103);
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            callCloudVisionAPI(bitmap, feature);
        }

        if(resultCode == RESULT_OK && requestCode == SELECT_SINGLE_PICTURE){
            Uri selectedImageUri = data.getData();
            try {
                imageView.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
            } catch (IOException e) {
                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.failed_to_get_data, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVisionAPI(Bitmap bitmap, final Feature feature) {
        progressBar.setVisibility(View.VISIBLE);
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

            protected void onPostExecute(String result) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                    toast.show();
                    resultView.setText(result);
                    Intent intent = new Intent(MainActivity.this,WebActivity.class);
                    intent.putExtra("test",result);
                    startActivity(intent);
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
        if (test != null)
            if (message == "Nothing Found for Logo \n")
                message += formatWebAnnotation(test.getWebEntities());

        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                message = message + "    " + entity.getDescription(); // + " " + entity.getScore();
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
