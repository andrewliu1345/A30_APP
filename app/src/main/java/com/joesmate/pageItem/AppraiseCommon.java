package com.joesmate.pageItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joesmate.App;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.sdcs.SDCSStartEvaluateData;
import com.joesmate.page.PlayActivity;
import com.joesmate.widget.AppraiseButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户普通评价
 *
 * @author yc.zhang
 */
public class AppraiseCommon extends BasePageItem {

    Context mContext;
    TextView tvId, tvName, tvHit;
    ImageView ivPhoto;
    LinearLayout layoutTellerInf, layoutBtGroup, linearLayoutStarGrp;
    //AppraiseCommonData commonData;

    SDCSStartEvaluateData baseData;


    private List<String> listButton = new ArrayList<String>();
    private List<String> listButtonEn = new ArrayList<String>();

    final int[] resId = {R.drawable.appraise_very_good,
            R.drawable.appraise_good, R.drawable.appraise_general,
            R.drawable.appraise_bad, R.drawable.appraise_very_bad};
    List<AppraiseButton> appraiseButtons = new ArrayList<AppraiseButton>();

    public AppraiseCommon(Context context) {
        super(context);
        mContext = context;
        layoutTellerInf = (LinearLayout) findViewById(id.tellerinf);
        layoutBtGroup = (LinearLayout) findViewById(id.appraise_bt_group);
        tvHit = (TextView) findViewById(id.appraise_hit);
        tvId = (TextView) findViewById(id.personNumber);
        tvName = (TextView) findViewById(id.personName);
        ivPhoto = (ImageView) findViewById(id.photo);
        linearLayoutStarGrp = (LinearLayout) findViewById(id.starlist);
        baseData = SDCSStartEvaluateData.getInstance();

        //commonData = AppraiseCommonData.getinstance();
        // 非常满意,Excellent|满意,ok|不满意,No Comment
        listButton.add("不满意");
        listButton.add("一般");
        listButton.add("满意");

        listButtonEn.add("No Comment");
        listButtonEn.add("OK");
        listButtonEn.add("Excellent");
    }

    public void init() {
        super.init(baseData, ButtonType.NOTHING);

        tvHit.setVisibility(View.GONE);
        layoutTellerInf.setVisibility(View.VISIBLE);
        ivPhoto.setBackgroundColor(getResources().getColor(R.color.gray));


        appraiseButtons.clear();
        layoutBtGroup.removeAllViews();

        SetStarRate(baseData.getRate());
        tvId.setText(baseData.getJobNumber());
        tvName.setText(baseData.getName());
        setTitle(baseData.getBeforeMsg());
        setTimer(baseData.getTimeOut());
        App.getInstance().tts.Read(baseData.getVoiceText(), 1);

        Drawable db = getPhotoImage();
//        Bitmap dm=db.getBi
//        ViewGroup.LayoutParams layoutParams= ivPhoto.getLayoutParams();
        ivPhoto.setImageDrawable(db);

        for (int i = 0; i < listButton.size(); i++) {
            int bgResid = i;
            if (bgResid >= resId.length) {
                bgResid = 0;
            }
            layoutBtGroup.addView(
                    getBtLayout(listButton.get(i), listButtonEn.get(i), resId[bgResid]),
                    getWeightParams());
        }
    }

    public void SetStarRate(int level) {
        if (level < 1)
            level = 1;
        if (level > 5)
            level = 5;
        linearLayoutStarGrp.removeAllViews();
        for (int i = 0; i < level; ++i) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.star));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(40, 40);
            imageView.setLayoutParams(params);
            linearLayoutStarGrp.addView(imageView);
        }

    }

    public Drawable getPhotoImage() {
        String photoUrl;
        if (baseData != null) {
            if (baseData.getPhotoName() != null) {
                photoUrl = "/sdcard/MEDIA/head/" + baseData.getPhotoName();
                File file = new File(photoUrl);
                if (file.isFile() && file.exists()) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.RGB_565;
//                    options.inPurgeable = true;
//                    options.inInputShareable = true;
//                    options.inSampleSize = 1;
//                    Drawable drawable = Drawable.createFromPath(photoUrl);
//                    Bitmap bmp = BitmapFactory.decodeFile(photoUrl, options);
                    Drawable zoomDrawable = zoomDrawable(Drawable.createFromPath(photoUrl), 250, 300);
                    return zoomDrawable;
                } else {
                    return getResources().getDrawable(R.drawable.defaulthead);
                }

            } else {
                return getResources().getDrawable(R.drawable.defaulthead);
            }
        } else {
            return getResources().getDrawable(R.drawable.defaulthead);
        }

    }

    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap
    {
        int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;         // 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应 bitmap
        Canvas canvas = new Canvas(bitmap);         // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);      // 把 drawable 内容画到画布中
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float) w / width);   // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回
    }


    private LayoutParams getWeightParams() {
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        return params;
    }

    private LinearLayout getBtLayout(String cn, String en, int bgResid) {
        LinearLayout layout = new LinearLayout(getContext());
        //layout.setBackgroundResource(R.drawable.timer_bg);
        int width = (int) res.getDimension(R.dimen.appraise_bt_width);
        int height = (int) res.getDimension(R.dimen.appraise_bt_height);
        LayoutParams params = new LayoutParams(-1, height);
        layout.addView(getAppraiseButton(cn, en, bgResid), params);
        return layout;
    }

    private AppraiseButton getAppraiseButton(String cn, String en, int bgResid) {
        AppraiseButton button = new AppraiseButton(getContext());
        button.setOnClickListener(this);
        appraiseButtons.add(button);
        button.setText(cn, en, bgResid);
        return button;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < appraiseButtons.size(); i++) {
            if (v == appraiseButtons.get(i)) {
                Log.d(TAG, "appraiseButtons：" + i);

                if (i == 0) {
                    baseData.sendConfirmResult(String.valueOf(3));
                    baseData.setResult("不满意");
                }
                if (i == 1) {
                    baseData.sendConfirmResult(String.valueOf(2));
                    baseData.setResult("一般");
                }
                if (i == 2) {
                    baseData.sendConfirmResult(String.valueOf(1));
                    baseData.setResult("满意");
                }
                toPlay(PlayActivity.PAGE_APPRAISE_OVER);
                App.getInstance().tts.Read("感谢您的评价", 1);
            }
        }
    }

    @Override
    public void timeOut() {
        baseData.sendConfirmResult("4");
        toPlay();
    }

    @Override
    public View getContentView() {
        return inflate(getContext(), R.layout.page_item_appraisecommon, null);
    }

}
