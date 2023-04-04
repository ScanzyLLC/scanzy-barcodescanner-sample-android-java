# scanzy-barcodescanner-sample-android-java
Native Android sample to use ScanzyBarcodeScannerSDK. 

If you have any questions or need help, check out official website [scanzy.com](https://scanzy.com). Get a free trial license, you'll be able to integrate Scanzy SDK to your app in less than an hour, and it's insanely simple!

## Prerequisites

Android Studio is the official IDE to develop Android mobile apps. Follow the official site to setup the IDE and understand the concepts. [Android Studio](https://developer.android.com/studio/intro)


## Installation

3. In your app module's build.gradle file, add the below dependencies:

```gradle
dependencies {
    ...
    
    implementation 'com.scanzy:ScanzyBarcodeScannerSDK:0.0.6'

   ...
}

```

## Quick Start

1. In your app's entry point, such as an Oncreate method in MainActivity, set the license key you obtained from [scanzy.com](https://scanzy.com) for free trial.

```java
 ScanzyBarcodeManager.setLicense(getApplicationContext(),"your-valid-licensekey");
```

For example, the code in the sample app is:

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        ScanzyBarcodeManager.setLicense(getApplicationContext(),"your-valid-licensekey");
    }
    
```

Note: Although it's not harmful to call ScanzyBarcodeManager.setLicense multiple times, even on every single scan, it's better to call it just once in your app's entry point.

2. Add the below code in a specific place in your app, such as a button click where you need to trigger a barcode scan:

There are two ways to launch an activity. Android suggests the AndroidX Result API way:

```java

ActivityResultLauncher<Intent> launchBarcodeScanActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == BarcodeScanStatus.SUCCESS) {
                         String barcode = result.getData().getStringExtra("barcode");
                            if(!barcode.equalsIgnoreCase("")) {
                                //your actual business logic to deal with the returned barcode
                            }
                    }
                });

 binding.buttonFirst.setOnClickListener(view1 -> {
            ScanzyBarcodeOptions barcodeOptions = new ScanzyBSBarcodeOptions(
                    false,false,false,false,
                    EnumSet.of(ScanzyBarcodeFormat.Code128, ScanzyBarcodeFormat.Code39,ScanzyBarcodeFormat.QRCode,
                            ScanzyBarcodeFormat.EAN13,ScanzyBarcodeFormat.UPCA));
            ScanzyBarcodeManager manager = new ScanzyBarcodeManager(getActivity(), barcodeOptions);
            manager.scan(launchBarcodeScanActivity);
        });

```

If you still use the old ways, do it like below:

```java
 binding.buttonFirst.setOnClickListener(view1 -> {
            ScanzyBarcodeOptions barcodeOptions = new ScanzyBarcodeOptions(
                    false,false,false,false,
                    EnumSet.of(ScanzyBarcodeFormat.Code128, ScanzyBarcodeFormat.Code39,ScanzyBarcodeFormat.QRCode,
                            ScanzyBarcodeFormat.EAN13,ScanzyBarcodeFormat.UPCA));
            ScanzyBarcodeManager manager = new ScanzyBarcodeManager(getActivity(), barcodeOptions);

            Intent intent = manager.getBarcodeScannerIntent(getActivity());
            startActivityForResult(intent,ScanzyBarcodeManager.RC_BARCODE_CAPTURE);
        });

```

```java
 @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == BarcodeScanStatus.SUCCESS) {
           String barcode = data.getStringExtra("barcode");
           //your actual business logic to deal with the returned barcode
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
```

## API Specification

Below are more details about the parameters:

The definition of ScanzyBarcodeFormat:

```java

public enum ScanzyBarcodeFormat {
    Code128,
    Code39,
    Code93,
    CodaBar,
    DataMatrix,
    EAN13,
    EAN8,
    ITF,
    QRCode,
    UPCA,
    UPCE,
    PDF417,
    Aztec,
    MaxiCode
}

```
To support barcode formats, set them like this:

```java
EnumSet<ScanzyBarcodeFormat> barcodeFormats =  EnumSet.of(ScanzyBarcodeFormat.Code128, ScanzyBarcodeFormat.Code39,ScanzyBarcodeFormat.QRCode,
                            ScanzyBarcodeFormat.EAN13,ScanzyBarcodeFormat.UPCA);
```
This way it will support Code128, Code39, QRCode, EAN13, Code128 and UPCA. 

Note: Set only the formats you are interested in. You can add ALL formats, but it would impact performance.


ScanzyBarcodeOptions is defined as:

```java
public class ScanzyBarcodeOptions {
   
    public ScanzyBarcodeOptions(boolean enableVibration, 
                                boolean enableBeep, 
                                boolean enableAutoZoom, 
                                boolean enableScanRectOnly, 
                                EnumSet<ScanzyBSBarcodeFormat> formats) {}
```

|     Parameter    |   Description         | 
| ------------- |:-------------:| 
| enableVibration      | vibrate your phone when a barcode is detected |
| enableBeep      |   play a beep sound when a barcode is detected    |  
| enableAutoZoom |   the library will zoom in/out automatcially to scan a barcode    |   
| enableScanCropRectOnly |   only scan the view finder area    |   
| formats |   the barcode formats    |   
