package com.letit0or1.akimaleo.ocr;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by akimaleo on 11.01.17.
 */

public class OpticalCharacterRecognizer {

    public static String LOG_TAG = "OCR";
//    public static String DATA_PATH = "src/main/assets/tessdata";

    public static String extractText(Bitmap imageBitmap, Context context) {
        String extractedText = "";
        // imageBitmap is the Bitmap image you're trying to process for text
        if (imageBitmap != null) {

            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

            if (!textRecognizer.isOperational()) {
                // Note: The first time that an app using a Vision API is installed on a
                // device, GMS will download a native libraries to the device in order to do detection.
                // Usually this completes before the app is run for the first time.  But if that
                // download has not yet completed, then the above call will not detect any text,
                // barcodes, or faces.
                // isOperational() can be used to check if the required native libraries are currently
                // available.  The detectors will automatically become operational once the library
                // downloads complete on device.
                Log.w(LOG_TAG, "Detector dependencies are not yet available.");

                // Check for low storage.  If there is low storage, the native library will not be
                // downloaded, so detection will not become operational.
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = context.registerReceiver(null, lowstorageFilter) != null;

                if (hasLowStorage) {
                    Toast.makeText(context, "Low Storage", Toast.LENGTH_LONG).show();
                    Log.w(LOG_TAG, "Low Storage");
                }
            }


            Frame imageFrame = new Frame.Builder()
                    .setBitmap(imageBitmap)
                    .build();

            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                extractedText += textBlock.getValue();
                Log.e(LOG_TAG, textBlock.getValue());
                // Do something with value
            }
        }
        return extractedText;
    }

}
