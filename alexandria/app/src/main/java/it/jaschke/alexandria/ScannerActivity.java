package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * An activity used to scan the 1-D barcode using the Zxing lib.
 * Created by YANG on 1/5/2016.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler{
    private static final String LOG_TAG = ScannerActivity.class.getSimpleName();
    private static final String SCAN_CONTENTS = "scanContents";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(Arrays.asList(new BarcodeFormat[]{BarcodeFormat.EAN_13}));
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(true);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register this activity as the handler for scan results.
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Sends the result back to the calling activity
        Intent returnedData = new Intent();
        returnedData.putExtra(SCAN_CONTENTS, rawResult.getText());
        setResult(RESULT_OK, returnedData);
        finish();
    }
}
