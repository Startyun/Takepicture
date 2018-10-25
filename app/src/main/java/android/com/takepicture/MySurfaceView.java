package android.com.takepicture;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Camera.AutoFocusCallback {

    public  static  String TAG = "MySurfaceView" ;
    float previewRate = -1f;

    CameraInface mCameraInterface;
    Context mContext;
    SurfaceHolder mSurfaceHolder;
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        mSurfaceHolder = getHolder();//获取SurfaceHolder对象
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);//注册SurfaceHolder的回调方法
    }



    @Override
    public void onAutoFocus(boolean b, Camera camera) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: -----相机创建");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceCreated: ----相机变换-");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        CameraInface.getInstance().doStopCamera();
        Log.d(TAG, "surfaceCreated: -----相机销毁");
    }

    public SurfaceHolder getSurfaceHolder(){
        return mSurfaceHolder;
    }
}
