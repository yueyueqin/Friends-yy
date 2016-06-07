
package com.cyan.scanpay;

import android.os.Handler;
import android.os.Looper;

import com.cyan.fragment.ScanpayFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 */
final class DecodeThread extends Thread
{

    public static final String BARCODE_BITMAP = "barcode_bitmap";

    private ScanpayFragment fragment;
    private final Map<DecodeHintType, Object> hints;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    DecodeThread(ScanpayFragment fragment,
                 Collection<BarcodeFormat> decodeFormats, String characterSet,
                 ResultPointCallback resultPointCallback)
    {

        this.fragment = fragment;
        handlerInitLatch = new CountDownLatch(1);

        hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

        // The prefs can't change while the thread is running, so pick them up
        // once here.
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
            if (true) {
                decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            }
            if (true) {
                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            }
            if (true) {
                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
            }
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }
        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
                resultPointCallback);
    }

    Handler getHandler()
    {
        try {

            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run()
    {
        Looper.prepare();
        handler = new DecodeHandler(fragment, hints);

        handlerInitLatch.countDown();
        Looper.loop();
    }

}
