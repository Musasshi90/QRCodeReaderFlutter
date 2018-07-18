package io.github.ponnamkarthik.qrcodereader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


public class QRScanActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private boolean qrRead;
    private QRCodeReaderView view;
    private ImageView mImgLine;
    private TranslateAnimation mAnimation;

    public static String EXTRA_RESULT = "extra_result";

    public static String EXTRA_FOCUS_INTERVAL = "extra_focus_interval";
    public static String EXTRA_FORCE_FOCUS = "extra_force_focus";
    public static String EXTRA_TORCH_ENABLED = "extra_torch_enabled";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_read);
        view = (QRCodeReaderView) findViewById(R.id.activity_qr_read_reader);
        Intent intent = getIntent();
        view.setOnQRCodeReadListener(this);
        view.setQRDecodingEnabled(true);
        if (intent.getBooleanExtra(EXTRA_FORCE_FOCUS, false)) {
            view.forceAutoFocus();
        }
        view.setAutofocusInterval(intent.getIntExtra(EXTRA_FOCUS_INTERVAL, 2000));
        view.setTorchEnabled(intent.getBooleanExtra(EXTRA_TORCH_ENABLED, false));

        mImgLine = (ImageView) findViewById(R.id.image_view_line);
        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
        mAnimation.setDuration(3000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new LinearInterpolator());
        mImgLine.setAnimation(mAnimation);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (!qrRead) {
            synchronized (this) {
                qrRead = true;
                Intent data = new Intent();
                data.putExtra(EXTRA_RESULT, text);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.stopCamera();
    }
}
