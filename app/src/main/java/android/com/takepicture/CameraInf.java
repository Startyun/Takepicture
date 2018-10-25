package android.com.takepicture;


public interface CameraInf
{

    //    /*
//          打开 Camera
//     */
//     void doOpenCamera();
//    /*
//           开始预览 功能
//     */
//    void doStartPreview(SurfaceHolder holder, float previewRate);
    /*
        关闭摄像头
     */
    void doStopCamera();
    /*
       拍照
     */
    void doTakePicture();
}
