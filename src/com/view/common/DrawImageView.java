package com.view.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.zhengbang.TeMe.R;

public class DrawImageView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext;
	public DrawImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		sh = getHolder();
		sh.addCallback(this);
		sh.setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
	}

	protected SurfaceHolder sh;

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
	}

	public void surfaceCreated(SurfaceHolder arg0) {

	}

	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

    public void clearDraw() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Canvas canvas = sh.lockCanvas();
				canvas.drawColor(Color.TRANSPARENT);
				sh.unlockCanvasAndPost(canvas);
			}
		}, 200);
	}

	public void drawRect(int width, int height) {
		Canvas canvas = sh.lockCanvas();
		if (canvas != null) {
			Paint paint = new Paint();
			Matrix matrix = new Matrix();
			
			Bitmap coverage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.coverage);
			Bitmap createBitmap = Bitmap.createScaledBitmap(coverage, width, height, true);
			canvas.drawBitmap(createBitmap, matrix, paint);
//			Paint paint = new Paint();
//			paint.setColor(Color.RED);
//			paint.setTextSize(24);
//			paint.setStrokeJoin(Paint.Join.ROUND);
//			paint.setStrokeCap(Paint.Cap.ROUND);
//			paint.setStrokeWidth(3);
//			paint.setAntiAlias(true);
//			Path path = new Path(); // ����һ��·��
//			int s1 = width - 20;
//			path.moveTo(s1, 0); // �ƶ��� ���10,10
//			path.lineTo(s1, s1);
//			canvas.drawTextOnPath("��ע��:", path, 10, 10, paint);
//			path.reset();
//			int s2 = s1 - 30;
//			path.moveTo(s2, 0); // �ƶ��� ���10,10
//			path.lineTo(s2, s2);
//			canvas.drawTextOnPath("��Ƭ�е���Ϣ������ʵ��Ч������ɼ�", path, 10, 10, paint);
//			path.reset();
//			int s3 = s2 - 30;
//			path.moveTo(s3, 0); // �ƶ��� ���10,10
//			path.lineTo(s3, s3);
//			canvas.drawTextOnPath("-----------", path, 10, 10, paint);
//			path.reset();
//			int s4 = s3 - 30;
//			path.moveTo(s4, 0); // �ƶ��� ���10,10
//			path.lineTo(s4, s4);
//			canvas.drawTextOnPath("1,�ֳ�֤���˵���� ", path, 10, 10, paint);
//			path.reset();
//			int s5 = s4 - 30;
//			path.moveTo(s5, 0); // �ƶ��� ���10,10
//			path.lineTo(s5, s5);
//			canvas.drawTextOnPath("2,���֤�ϵ�������Ϣ ", path, 10, 10, paint);
//			paint.setStyle(Style.STROKE);
//			// ���ƾ��� l t r b
//
//			int l = 30;
//			int t = height / 3;
//			int r = width / 2;
//			int b = height - height / 5;
//			canvas.drawRect(l, t, r, b, paint);
//
//			Path path1 = new Path();// ����
//			int rectHeight = b - t;
//			path1.moveTo(r, t + rectHeight / 3);// �滭���
//			path1.quadTo((width * 9) / 10, t + rectHeight * 1 / 20, (width * 3) / 3, t + rectHeight / 2);
//			path1.quadTo((width * 9) / 10, t + rectHeight, r, t + rectHeight * 4 / 6);
//			path1.close();// �ѿ�ʼ�ĵ�����ĵ�������һ�𣬹���һ�����ͼ��
//			/*
//			 * ����Ҫ�ľ���movtTo��close,�����Style.FILL�Ļ���������close,Ҳû����𣬿��������STROKEģʽ��
//			 * �������close,ͼ�β���ա�
//			 * 
//			 * ��Ȼ����Ҳ���Բ�����close�������һ���ߣ�Ч��һ��
//			 */
//			canvas.drawPath(path1, paint);

			sh.unlockCanvasAndPost(canvas);
		}

	}
	
	/**
	 * Bitmapת����InputStream����
	 * 
	 * @param bm
	 * @return
	 */
	public static InputStream bitmap2IS(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

}
