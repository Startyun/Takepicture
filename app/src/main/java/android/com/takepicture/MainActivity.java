package android.com.takepicture;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends Activity implements CameraInface.CamOpenOverCallback,View.OnClickListener
{
    MySurfaceView surfaceView ;
    private TextView tet1;
    float previewRate = -1f;
    public static String TAG = "test";
    ImageView shutterBtn ;
    private final int CAMERA_REQUEST_CODE = 1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initUI();
        initViewParams();
        isGrantExternalRW(this);

    }

    @Override
    protected void onResume() {
        requestPermission();   //打开摄像头
        super.onResume();


    }




    //打开预览
    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        CameraInface.getInstance().doStartPreview(holder, previewRate,this);
    }

    @Override
    public void previewHasGet(int width, int height) {
        //        用于展示camera预览图像的View，就是将preview获得的数据，放在这个View上。
//        所以如果preview的宽高比和surfaceview的宽高比不一样，就会导致看到的图像拉伸变形。
//        解决办法就是在确定preview的分辨率后，重新设置surfaceview的宽高。


//        Message msg = new Message() ;
//        msg.what = 0 ;
//        msg.arg1 = width ;
//        msg.arg2 = height ;
//        myhandler.handleMessage(msg);
//


    }
    public  void initUI()
    {
        surfaceView = (MySurfaceView) findViewById(R.id.mySruface);
        tet1=(TextView)findViewById(R.id.tet1);
        shutterBtn = (ImageView) findViewById(R.id.myBtn);
        shutterBtn.setOnClickListener(this);
        tet1.setOnClickListener(this);
    }
    private void initViewParams(){
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
//
//        //让 surfaceView  满屏方法
//        params.width = 1200;
//        params.height = 1847;

//        Log.d(TAG, "initViewParams:  --------手机的高度"+p.y+"-----手机的宽度："+p.x);
        previewRate = DisplayUtil.getScreenRate(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.myBtn:
                CameraInface.getInstance().doTakePicture();
                break;
            case R.id.tet1:
                CameraInface.getInstance().doStopCamera();
                finish();
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "requestPermission:  相机权限已申请1");
            Thread openThread = new Thread(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    CameraInface.getInstance().doOpenCamera(MainActivity.this);
                }
            };
            openThread.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: 相机权限已申请");
                new Thread(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        CameraInface.getInstance().doOpenCamera(MainActivity.this);
                    }
                }.start();
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 获取储存权限
     * @param activity
     * @return
     */

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


}
