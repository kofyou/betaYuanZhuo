/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fht.yuanzhuo.Userclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfScale;
import es.voghdev.pdfviewpager.library.util.EmptyClickListener;
import uk.co.senab.photoview.PhotoViewAttacher;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PDFPagerAdapter2 extends BasePDFPagerAdapter
        implements PhotoViewAttacher.OnMatrixChangedListener {

    private static final float DEFAULT_SCALE = 1f;
    private boolean first =true;
    private  int[] positionInside;

    SparseArray<WeakReference<PhotoViewAttacher>> attachers;
    PdfScale scale = new PdfScale();
    View.OnClickListener pageClickListener = new EmptyClickListener();

    public PDFPagerAdapter2(Context context, String pdfPath) {
        super(context, pdfPath);
        attachers = new SparseArray<>();
    }

    @Override
    @SuppressWarnings("NewApi")
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(es.voghdev.pdfviewpager.library.R.layout.view_pdf_page, container, false);
        final ImageView iv = (ImageView) v.findViewById(es.voghdev.pdfviewpager.library.R.id.imageView);
        if (renderer == null || getCount() < position) {
            return v;
        }

        PdfRenderer.Page page = getPDFPage(renderer, position);

        Bitmap bitmap = bitmapContainer.get(position);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        page.close();

        PhotoViewAttacher attacher = new PhotoViewAttacher(iv);
        attacher.setScale(scale.getScale(), scale.getCenterX(), scale.getCenterY(), true);
        attacher.setOnMatrixChangeListener(this);

        attachers.put(position, new WeakReference<PhotoViewAttacher>(attacher));

        iv.setImageBitmap(bitmap);
        //Log.e("positionn",String.valueOf(position));

        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                pageClickListener.onClick(view);
            }
        });
        attacher.update();

        ((ViewPager) container).addView(v, 0);

        if(first==TRUE)
        {
            iv.post(new Runnable() {
                @Override
                public void run() {
                    positionInside=getBitmapPositionInsideImageView(iv);
                    Log.e("bad ass",positionInside[2]+" "+positionInside[3]); //height is ready
                }
            });
            first=FALSE;
        }
        return v;
    }

    @Override
    public void close() {
        super.close();
        if (attachers != null) {
            attachers.clear();
            attachers = null;
        }
    }

    @Override
    public void onMatrixChanged(RectF rect) {
        if (scale.getScale() != PdfScale.DEFAULT_SCALE) {
//            scale.setCenterX(rect.centerX());
//            scale.setCenterY(rect.centerY());
        }
    }

    public static class Builder {
        Context context;
        String pdfPath = "";
        float scale = DEFAULT_SCALE;
        float centerX = 0f, centerY = 0f;
        int offScreenSize = DEFAULT_OFFSCREENSIZE;
        float renderQuality = DEFAULT_QUALITY;
        View.OnClickListener pageClickListener = new EmptyClickListener();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setScale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder setScale(PdfScale scale) {
            this.scale = scale.getScale();
            this.centerX = scale.getCenterX();
            this.centerY = scale.getCenterY();
            return this;
        }

        public Builder setCenterX(float centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder setCenterY(float centerY) {
            this.centerY = centerY;
            return this;
        }

        public Builder setRenderQuality(float renderQuality) {
            this.renderQuality = renderQuality;
            return this;
        }

        public Builder setOffScreenSize(int offScreenSize) {
            this.offScreenSize = offScreenSize;
            return this;
        }

        public Builder setPdfPath(String path) {
            this.pdfPath = path;
            return this;
        }

        public Builder setOnPageClickListener(View.OnClickListener listener) {
            if (listener != null) {
                pageClickListener = listener;
            }
            return this;
        }

        public PDFPagerAdapter2 create() {
            PDFPagerAdapter2 adapter = new PDFPagerAdapter2(context, pdfPath);
            adapter.scale.setScale(scale);
            adapter.scale.setCenterX(centerX);
            adapter.scale.setCenterY(centerY);
            adapter.offScreenSize = offScreenSize;
            adapter.renderQuality = renderQuality;
            adapter.pageClickListener = pageClickListener;
            int size=adapter.getCount();
            Log.e("sizeeeee",String.valueOf(size));
            return adapter;
        }
    }

    public Bitmap getBitmap(int i) {
//        PdfRenderer.Page page=renderer.openPage(i);
//        Bitmap bitmap1 = bitmapContainer.get(i);
//        Bitmap bitmap2 =Bitmap.createBitmap(bitmap1.getWidth(),bitmap1.getHeight(),Bitmap.Config.ARGB_8888);
//        Canvas canvas =new Canvas(bitmap2);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(bitmap2,0,0,null);
//        Rect r =new Rect(0,0,bitmap1.getWidth(),bitmap1.getHeight());
//        page.render(bitmap2,r,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        page.close();
//        return bitmap2;

        PdfRenderer.Page page = getPDFPage(renderer, i);
        Bitmap bitmap = bitmapContainer.get(i);
        Canvas canvas =new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0,0,null);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        Log.e("page",String.valueOf(page.getWidth())+" "+String.valueOf(page.getHeight()));

        page.close();
        return bitmap;
        //return myBitmaps[i];

    }

    /**
     * Returns the bitmap position inside an imageView.
     * @param imageView source ImageView
     * @return 0: left, 1: top, 2: width, 3: height
     */
    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public  int[] getPositionInside()
    {
        return positionInside;
    }

}


