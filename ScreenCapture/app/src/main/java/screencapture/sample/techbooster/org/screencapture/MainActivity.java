package screencapture.sample.techbooster.org.screencapture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;


public class MainActivity extends Activity {

    private static final String TAG = "ScreenCapture";
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private ImageReader mImageReader; // スクリーンショット用
    private int mWidth;
    private int mHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenshot = getScreenshot();

                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(screenshot);
            }
        });

        // Lintによるサジェスト "Must be one of..."が赤い下線で出ますがここでは無視します（ビルドできる）
        mMediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (REQUEST_CODE_SCREEN_CAPTURE == requestCode) {
            if (resultCode != RESULT_OK) {
                //パーミッションなし
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                return;
            }
            //スタート
            mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, intent);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
            int density = metrics.densityDpi;

            mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.RGB_565, 2);
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("Capturing Display",
                    mWidth, mHeight, density,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    @Override
    protected void onPause() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
        super.onPause();
    }

    private Bitmap getScreenshot() {
        Image image = mImageReader.acquireLatestImage();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int offset = 0;
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * mWidth;

        // create bitmap
        Bitmap bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.RGB_565);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
