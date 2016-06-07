package com.cyan.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.cyan.community.R;
import com.cyan.scanpay.CameraManager;
import com.cyan.scanpay.CaptureActivityHandler;
import com.cyan.scanpay.QRCodeReader;
import com.cyan.scanpay.RGBLuminanceSource;
import com.cyan.scanpay.ResultHandler;
import com.cyan.scanpay.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:28
 * 类说明：扫描二维码，查看订单，支付部分
 */
@SuppressLint("HandlerLeak")
public class ScanpayFragment extends PreferenceFragment implements SurfaceHolder.Callback
{
    String message = null;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private Result lastResult;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private com.cyan.scanpay.InactivityTimer inactivityTimer;
    private final int from_photo = 010;
    static final int PARSE_BARCODE_SUC = 3035;
    static final int PARSE_BARCODE_FAIL = 3036;
    String photoPath;
    ProgressDialog mProgress;
    private SurfaceView surfaceView;

    enum IntentSource
    {

        ZXING_LINK, NONE

    }

    Handler barHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    //viewfinderView.setRun(false);
                    showDialog((String) msg.obj);
                    break;
                case PARSE_BARCODE_FAIL:
                    //showDialog((String) msg.obj);
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("扫描失败！").setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public ViewfinderView getViewfinderView()
    {
        return viewfinderView;
    }

    public Handler getHandler()
    {
        return handler;
    }

    public CameraManager getCameraManager()
    {
        return cameraManager;
    }


    public static ScanpayFragment newInstance()
    {
        ScanpayFragment scanpayFragment = new ScanpayFragment();
        return scanpayFragment;
    }

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        hasSurface = false;
        inactivityTimer = new com.cyan.scanpay.InactivityTimer(getActivity());

        cameraManager = new CameraManager(getActivity().getApplication());
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_scanpay, null);
        initView(fragmentView);

        return fragmentView;
    }

    private void initView(View fragmentRootView)
    {
        viewfinderView = (ViewfinderView) fragmentRootView.findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        surfaceView = (SurfaceView) fragmentRootView.findViewById(R.id.preview_view);

    }


    public void showDialog(final String msg)
    {
        msg.startsWith("http");

    }

    public String parsLocalPic(String path)
    {
        String parseOk = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        // 缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader2 = new QRCodeReader();
        Result result;
        try {
            result = reader2.decode(bitmap1, hints);
            android.util.Log.i("steven", "result:" + result);
            parseOk = result.getText();

        } catch (NotFoundException e) {
            parseOk = null;
        } catch (ChecksumException e) {
            parseOk = null;
        } catch (FormatException e) {
            parseOk = null;
        }
        return parseOk;
    }

    @SuppressWarnings("unused")
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        android.util.Log.i("steven", "data.getData()" + data);
        if (data != null) {
            mProgress = new ProgressDialog(getActivity());
            mProgress.setMessage("正在扫描...");
            mProgress.setCancelable(false);
            mProgress.show();
            final ContentResolver resolver = getActivity().getContentResolver();
            if (requestCode == from_photo) {
                if (resultCode == getActivity().RESULT_OK) {
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Looper.prepare();
                            String result = parsLocalPic(photoPath);
                            if (result != null) {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result;
                                barHandler.sendMessage(m);
                            } else {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "扫描失败！";
                                barHandler.sendMessage(m);
                            }
                            Looper.loop();
                        }
                    }).start();
                }

            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume()
    {
        super.onResume();
        handler = null;
        lastResult = null;
        resetStatusView();

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
    }

    @Override
    public void onPause()
    {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        if (!hasSurface) {

            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        inactivityTimer.shutdown();
        if (mProgress != null) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return onKeyDown(keyCode, event);
    }

    // 这里初始化界面，调用初始化相机
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (holder == null) {
            Log.e("", "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    private static ParsedResult parseResult(Result rawResult)
    {
        return ResultParser.parseResult(rawResult);
    }

    // 解析二维码
    @SuppressWarnings("unused")
    public void handleDecode(Result rawResult, Bitmap barcode)
    {
        inactivityTimer.onActivity();
        lastResult = rawResult;

        ResultHandler resultHandler = new ResultHandler(parseResult(rawResult));

        boolean fromLiveScan = barcode != null;
        if (barcode == null) {
            android.util.Log.i("steven", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
            android.util.Log.i("steven", "resultHandler.getType().toString():" + resultHandler.getType().toString());
            android.util.Log.i("steven", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());
        } else {//扫描成功
            String code = resultHandler.getDisplayContents().toString();
            if (code != null && !code.equals("")) {
                if (code.startsWith("http")) {//网址
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(code);
                    intent.setData(content_url);
                    startActivity(intent);
                    getActivity().finish();
                } else {//内容
                    showCustomMessageOK(this.getString(R.string.scanDialogTitle), code);
                }
            }


        }
    }

    /**
     * it will show the OK dialog like iphone, make sure no keyboard is visible
     *
     * @param pTitle title for dialog
     * @param pMsg   msg for body
     */
    private void showCustomMessageOK(String pTitle, final String pMsg)
    {
        message=pMsg;
        String[] Array = message.split("\n");
       String name=Array[0];
        String data=Array[1];
        String station=Array[2];
        String type=Array[3];
        String number=Array[4];
        String money=Array[5];

        Intent intent=new Intent(getActivity(), com.cyan.gas.Result.class);
        intent.putExtra("name",name);
        intent.putExtra("data",data);
        intent.putExtra("station",station);
        intent.putExtra("type",type);
        intent.putExtra("number",number);
        intent.putExtra("money",money);

        startActivity(intent);
    }

    // 初始化照相机，CaptureActivityHandler解码
    private void initCamera(SurfaceHolder surfaceHolder)
    {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w("", "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new com.cyan.scanpay.CaptureActivityHandler(ScanpayFragment.this, decodeFormats, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w("", ioe);
        } catch (RuntimeException e) {
            Log.w("", "Unexpected error initializing camera", e);
        }
    }

    public void restartPreviewAfterDelay(long delayMS)
    {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView()
    {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();
    }

    /**
     * 监听返回按钮
     */

    public boolean dispatchKeyEvent(KeyEvent event)
    {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                getActivity().finish();
            }
            return true;
        }
        return dispatchKeyEvent(event);
    }


}
