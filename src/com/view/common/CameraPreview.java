package com.view.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("NewApi")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static String TAG = "CameraPreview";
    private boolean isPreview;
    private Context mContext;
    private Uri picUri;
    private Camera.Parameters myParam;
    private int flashState = 2;// 0 关闭, 1, 打开 , 2自动
    boolean changeCarmer;

    public void takePicture() {
        mCamera.takePicture(null, null, mPicture);
    }

    public void setFlashState(int flashState) {
        this.flashState = flashState;
    }

    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = getCameraInstance();
            if (null != mCamera) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            Log.d(TAG, "crx Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // 当surfaceview关闭时，关闭预览并释放资源
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        mCamera = null;
        holder = null;
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        initCamera();
    }

    public void changeCarmer() {
        if (cameraPosition == 0) {
            cameraPosition = 1;
        } else if (cameraPosition == 1) {
            cameraPosition = 0;
        } else {
            cameraPosition = 0;
        }
        mCamera = getCameraInstance();
    }

    //直接开启
    public int getFlashState() {
        return flashState;
    }

    //直接开启
    public void openFlash() {
        myParam.setFlashMode(Parameters.FLASH_MODE_TORCH);//开启
        mCamera.setParameters(myParam);
    }

    //直接关闭
    public void closeFlash() {
        myParam.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
        mCamera.setParameters(myParam);
    }

    //自动
    public void autoFlash() {
        myParam.setFlashMode(Parameters.ANTIBANDING_AUTO);//关闭
        mCamera.setParameters(myParam);
    }

    // 初始化相机
    public void initCamera() {
        if (isPreview && mCamera != null) {
            mCamera.stopPreview();
        }
        if (null != mCamera) {
            setParam();
            mCamera.startPreview();
            isPreview = true;
        }
    }

    public void setPicUri(Uri uri) {
        picUri = uri;
    }

    public Uri getPicUri() {
        return picUri;
    }

    private boolean isAlready = false;

    @SuppressWarnings("deprecation")
    private void setParam() {
        myParam = mCamera.getParameters();
        myParam.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式
        // 查询camera支持的picturesize和previewsize
        List<Size> pictureSizes = myParam.getSupportedPictureSizes();
        List<Size> previewSizes = myParam.getSupportedPreviewSizes();
        // 设置大小和方向等参数
        myParam.setPreviewSize(previewSizes.get(0).width, previewSizes.get(0).height);
        myParam.setPictureSize(previewSizes.get(0).width, previewSizes.get(0).height);
        myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        myParam.setFlashMode(Parameters.ANTIBANDING_AUTO);//关闭
        mCamera.setParameters(myParam);
        mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
        if (isAlready) {
            startFaceDetection(); // start face detection feature
            isAlready = true;
        }
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @SuppressLint("NewApi")
    class MyFaceDetectionListener implements Camera.FaceDetectionListener {

        @SuppressLint("NewApi")
        @Override
        public void onFaceDetection(Face[] faces, Camera camera) {
            if (faces.length > 0) {
                Log.i(TAG, "crx face detected: " + faces.length + " Face 1 Location X: " + faces[0].rect.centerX() + "Y: " + faces[0].rect.centerY());
            }
        }
    }

    public void startFaceDetection() {
        Camera.Parameters params = mCamera.getParameters();
        Log.i(TAG, "crx FaceDetection" + params.getMaxNumDetectedFaces());
        if (params.getMaxNumDetectedFaces() > 0) {
            mCamera.startFaceDetection();
        }
    }


    private int cameraPosition = 0;// 0代表前置摄像头，1代表后置摄像头

    public int getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(int position) {
        cameraPosition = position;
    }

    public Camera getCameraInstance() {
        try {
            if (checkCameraHardware(mContext)) {
                CameraInfo cameraInfo = new CameraInfo();
                int cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
                if (cameraCount >= 1) {
                    Camera.getCameraInfo(cameraPosition, cameraInfo);// 得到每一个摄像头的信息

                    if (cameraPosition == 1) {
                        // 现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            if (mCamera != null) {
                                mCamera.stopPreview();// 停掉原来摄像头的预览
                                mCamera.release();
                            }
                            mCamera = null;
                            mCamera = Camera.open(cameraPosition);// 打开当前选中的摄像头
                            try {
                                mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mCamera.startPreview();// 开始预览
                            mCamera.setDisplayOrientation(90);
                        }
                    } else {
                        // 现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            if (mCamera != null) {
                                mCamera.stopPreview();// 停掉原来摄像头的预览
                                mCamera.release();
                            }
                            mCamera = null;
                            mCamera = Camera.open(cameraPosition);// 打开当前选中的摄像头
                            try {
                                mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mCamera.startPreview();// 开始预览
                            mCamera.setDisplayOrientation(90);
                        }
                    }
                }

                if (mCamera == null) {
                    return null;
                } else {
                    setParam();
                }
            }
        } catch (Exception e) {
        }
        return mCamera; // returns null if camera is unavailable
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private MediaRecorder mMediaRecorder;
    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                /**
                 * 裁剪正方形
                 */
                Bitmap bitmap = ImageUtils.ImageCrop(data);
                ImageUtils.saveBitmapToLacal(bitmap, pictureFile.getAbsolutePath());
//                fos.write(data);
//                fos.close();
                setPicUri(Uri.fromFile(pictureFile));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "crx File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "crx Error accessing file: " + e.getMessage());
            }
        }
    };

    @SuppressWarnings("unused")
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @SuppressLint("SimpleDateFormat")
    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "crx failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset(); // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }
}