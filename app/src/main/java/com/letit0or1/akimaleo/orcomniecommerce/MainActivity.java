package com.letit0or1.akimaleo.orcomniecommerce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final int REQ_READ_EXTERNAL_STORAGE = 0;
    private Uri mImageUri;
    private Bitmap mImage;
    private boolean mDoLoadingPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDoLoadingPlaceholder = getIntent().getBooleanExtra("loading", true);

        try {
            mImageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            reqPermission();
        } catch (Exception e) {
            finishOcr("No image uri. .putExtra(\"imageUri\",URI_HERE)");
            e.printStackTrace();
        }
        startRecognizing();
    }

    void startRecognizing() {

        try {
            mImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
        } catch (IOException e) {
            finishOcr("No image found by URI.");
            e.printStackTrace();
        }
        try {
            String result = OpticalCharacterRecognizer.extractText(mImage, this);
            finishOcr(result);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void reqPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQ_READ_EXTERNAL_STORAGE);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQ_READ_EXTERNAL_STORAGE);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_READ_EXTERNAL_STORAGE:
                    startRecognizing();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void finishOcr(String dataText) {

        Intent data = new Intent();
        data.putExtra("text", dataText);
        setResult(RESULT_OK, data);

        finish();
    }
}
