package com.example.myscanapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.BarcodeFormat;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG =  TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private ZBarView zBarView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        zBarView = findViewById(R.id.zbarview);

        zBarView.setDelegate(this);
        context= this;
        Button open = findViewById(R.id.open_flashlight);
        Button close = findViewById(R.id.close_flashlight);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zBarView.openFlashlight();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zBarView.closeFlashlight();
            }
        });
//        Button stop_spot = findViewById(R.id.choose_qrcde_from_gallery);
//        stop_spot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BGAPhotoPickerActivity.IntentBuilder intentBuilder = new BGAPhotoPickerActivity.IntentBuilder(context);
//                intentBuilder.cameraFileDir(null);
//                intentBuilder.maxChooseCount(1);
//                intentBuilder.selectedPhotos(null);
//                intentBuilder.pauseOnScroll(false);
//                Intent photoPickerIntent = intentBuilder
//                        .build();
//                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        zBarView.startCamera();
        zBarView.startSpotAndShowRect();
    }



    @Override
    protected void onStop() {
        super.onStop();
        zBarView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zBarView.onDestroy();
    }
    private void vibrate () {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
//        TextView textView = (TextView) findViewById(R.id.txt);
//        textView.setText(result);
//        setTitle("扫描结果为：" + result);
        vibrate();
        Intent intent = new Intent(TestScanActivity.this,MainActivity.class);
        intent.putExtra("maple",result);
        startActivity(intent);
//        zBarView.startSpot(); // 开

    }


    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = zBarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                zBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }
    public void onClick(View v) {
        System.out.println(22665);
        switch (v.getId()) {
            case R.id.start_preview:
                zBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                break;
            case R.id.stop_preview:
                zBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
                break;
            case R.id.start_spot:
                zBarView.startSpot(); // 开始识别
                break;
            case R.id.stop_spot:
                System.out.println(2565655);
                zBarView.stopSpot(); // 停止识别
                break;
            case R.id.start_spot_showrect:
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.stop_spot_hiddenrect:
                zBarView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
                break;
            case R.id.show_scan_rect:
                zBarView.showScanRect(); // 显示扫描框
                break;
            case R.id.hidden_scan_rect:
                zBarView.hiddenScanRect(); // 隐藏扫描框
                break;
            case R.id.decode_scan_box_area:
                zBarView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
                break;
            case R.id.decode_full_screen_area:
                zBarView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
                break;
            case R.id.open_flashlight:
                zBarView.openFlashlight(); // 打开闪光灯
                break;
            case R.id.close_flashlight:
                zBarView.closeFlashlight(); // 关闭闪光灯
                break;
            case R.id.scan_one_dimension:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_two_dimension:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_qr_code:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_code128:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_ean13:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_high_frequency:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、ISBN13、UPC_A、EAN_13、CODE_128
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_all:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.ALL, null); // 识别所有类型的码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_custom:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式

                List<BarcodeFormat> formatList = new ArrayList<>();
                formatList.add(BarcodeFormat.QRCODE);
                formatList.add(BarcodeFormat.ISBN13);
                formatList.add(BarcodeFormat.UPCA);
                formatList.add(BarcodeFormat.EAN13);
                formatList.add(BarcodeFormat.CODE128);
                zBarView.setType(BarcodeType.CUSTOM, formatList); // 自定义识别的类型

                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;

            case R.id.choose_qrcde_from_gallery:
                /*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                        .cameraFileDir(null)
                        .maxChooseCount(1)
                        .selectedPhotos(null)
                        .pauseOnScroll(false)
                        .build();
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        zBarView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
//            zBarView.decodeQRCode(picturePath);
        }
    }
//    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
//    private void requestCodeQRCodePermissions() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, perms)) {
//            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
//        }
//    }
}
