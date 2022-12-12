package com.scanzy.barcodescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.scanzy.barcodescanner.databinding.FragmentFirstBinding;
import com.scanzy.datacapture.barcodescanner.BarcodeScanStatus;
import com.scanzy.datacapture.barcodescanner.ScanzyBSBarcodeFormat;
import com.scanzy.datacapture.barcodescanner.ScanzyBSBarcodeManager;
import com.scanzy.datacapture.barcodescanner.ScanzyBSBarcodeOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

public class BarcodeScanFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActivityResultLauncher<Intent> launchBarcodeScanActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == BarcodeScanStatus.SUCCESS) {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            String barcode = result.getData().getStringExtra("barcode");
                            String barcodeType = result.getData().getStringExtra("barcode_type");
                            if(!barcode.equalsIgnoreCase("")) {
                                builder.setMessage(barcode + " - " + barcodeType).setTitle("SCAN RESULT");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                        }
                    }
                });

        binding.buttonFirst.setOnClickListener(view1 -> {
            ScanzyBSBarcodeOptions barcodeOptions = new ScanzyBSBarcodeOptions(
                    false,
                    true,
                    true,
                    false,
                    EnumSet.of(
                            ScanzyBSBarcodeFormat.Code128,
                            ScanzyBSBarcodeFormat.Code39,
                            ScanzyBSBarcodeFormat.QRCode,
                            ScanzyBSBarcodeFormat.EAN13,
                            ScanzyBSBarcodeFormat.UPCA));
            ScanzyBSBarcodeManager manager = new ScanzyBSBarcodeManager(getActivity(), barcodeOptions);
            manager.scan(launchBarcodeScanActivity);
        });
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == ScanzyBSBarcodeManager.RC_BARCODE_CAPTURE) {
            if (resultCode == BarcodeScanStatus.SUCCESS) {
                if (data != null) {
                    String barcode = data.getStringExtra("barcode");
                    if (barcode.equalsIgnoreCase("permission_missing")) {

                    } else {
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                if(!barcode.equalsIgnoreCase("")) {
                                    builder.setMessage(barcode).setTitle("SCAN RESULT");
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                            catch (Exception e){
                            }
                    }
                } else {
                }
            } else {
            }
        }

        super.onActivityResult(requestCode, resultCode,data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}