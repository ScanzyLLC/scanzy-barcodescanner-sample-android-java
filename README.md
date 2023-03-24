# scanzy-barcodescanner-sample-android-java
native android sample to use ScanzyBarcodeScannerSDK

## Environment Setup

Android Studio is the official IDE to develop Android mobile apps. Follow the official site to setup the IDE and understand the concepts. [Android Studio](https://developer.android.com/studio/intro)


## Get Started

## Install ScanzyBarcodeScannerSDK

3. In your app module's build.gradle file, add below dependencies:

```gradle
dependencies {
    ...
    
    implementation 'com.scanzy:ScanzyBarcodeScannerSDK:0.0.6'

   ...
}

```

## Start to scan.

1. In your app's entry point, such as Oncreate method in MainActivity, set the license key you obtained from Scanzy.

```java
 ScanzyBarcodeManager.setLicense(getApplicationContext(),"your-valid-licensekey");
```

For example, the code in the sample app:

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

2. In your app's specific place, such as button click where you need to trigger the barcode scan. add below code:

There are two ways to launch an activity. Android suggests AndroidX Result API way:

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

If you still use the old ways, do like below:

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

That is all you need to use ScanzyBarcodeScannerSDK, happy coding :joy:

Below gives you more details about the parameters:

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
To support the barcode formats, set it such as:

```java
EnumSet<ScanzyBarcodeFormat> barcodeFormats =  EnumSet.of(ScanzyBarcodeFormat.Code128, ScanzyBarcodeFormat.Code39,ScanzyBarcodeFormat.QRCode,
                            ScanzyBarcodeFormat.EAN13,ScanzyBarcodeFormat.UPCA);
```
so it supports Code128, Code39, QRCode, EAN13, Code128 and UPCA. 

Note: to set the formats you only interested, although you can add ALL formats, it definitely would impact the performance.


The ScanzyBarcodeOptions is defined as:

```java
public class ScanzyBarcodeOptions {
   
    public ScanzyBarcodeOptions(boolean enableVibration, boolean enableBeep, boolean enableAutoZoom, boolean enableScanRectOnly, EnumSet<ScanzyBSBarcodeFormat> formats) {}
```

enableBeep: play the beep sound when barcode detected.<br>

enableVibration: vibrate your phone when barcode detected.<br>

enableAutoZoom: the library will zoom in/out automatcially to scan the barcode.<br>

enableScanRectOnly: only scan the view finder area.<br>

formats: the barcode formats.<br>
