package com.joesmate.pageItem.sdcs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joesmate.AppAction;
import com.joesmate.R;
import com.joesmate.bin.sdcs.SDCSConfirmPDF;
import com.joesmate.bin.sdcs.SDCSStartElectronicCardData;
import com.joesmate.page.PlayActivity;
import com.joesmate.pageItem.BasePageItem;

import java.io.File;

/**
 * Created by bill on 2016/5/14.   职员信息
 */
public class StartElectronicCardPage extends BasePageItem implements View.OnClickListener{

    SDCSStartElectronicCardData baseData ;
    SDCSConfirmPDF baseData1 = SDCSConfirmPDF.getInstance();
    ImageView imgPhoto ;
    TextView txtPersonName ;
    TextView txtPersonNumber;
    LinearLayout linearLayoutStarGrp ;
    Context mContext ;
    public StartElectronicCardPage(Context context) {
        super(context);
        mContext = context;
        setButtonType(ButtonType.NEXT);

        //职员信息
        setTitle(getResources().getString(R.string.personinfo));
        baseData = SDCSStartElectronicCardData.getInstance();
        imgPhoto = (ImageView) findViewById(R.id.photo);
        txtPersonName = (TextView) findViewById(R.id.personName);
        txtPersonNumber = (TextView) findViewById(R.id.personNumber);
        //txtRateStar = (TextView) findViewById(R.id.ratenumber);
        linearLayoutStarGrp = (LinearLayout)findViewById(R.id.starlist);


    }

    public void init() {
        if(baseData !=null)
        {
            txtPersonName.setText(baseData.getName());
            txtPersonNumber.setText(baseData.getJobNumber());
           // txtRateStar.setText(getRateString(baseData.getRate()));
            SetStarRate(baseData.getRate());
            imgPhoto.setImageDrawable(getPhotoImage());
            setTimer(baseData.getTimeOut());
            btNext.setText("取消");

        }
    }
    public void SetStarRate(int level)
    {
        if(level < 1 )
            level = 1 ;
        if(level > 6 )
            level = 6;
        linearLayoutStarGrp.removeAllViews();
        for (int i = 0 ; i < level; ++ i) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.star));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(40, 40);
            imageView.setLayoutParams(params);
            linearLayoutStarGrp.addView(imageView);
        }

    }


  public String getRateString( int level)
  {
      String strRate ;
      switch (level) {
          case 1:
              strRate = "一星";
              break;
          case 2:
              strRate = "二星";
              break;
          case 3:
              strRate = "三星";
              break;
          case 4:
              strRate = "四星";
              break;
          case 5:
              strRate = "五星";
              break;
          case 6:
              strRate = "六星";
              break;
          case 7:
              strRate = "七星";
              break;
          default:
              strRate = "三星";
      }
      return  strRate;

  }

    public Drawable getPhotoImage()
    {
        String photoUrl ;
        if(baseData != null)
        {
            if(baseData.getPhotoName() != null )
            {
                photoUrl = "/sdcard/MEDIA/head/"+baseData.getPhotoName();
                File file = new File(photoUrl);
                if (file.isFile() && file.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inSampleSize = 1;
                    Bitmap bmp = BitmapFactory.decodeFile(photoUrl, options);
                    return  new BitmapDrawable(bmp);
                }
                else
                {
                    return getResources().getDrawable(R.drawable.defaulthead);
                }

            }
            else
            {
                return getResources().getDrawable(R.drawable.defaulthead);
            }
        }
        else
        {
            return getResources().getDrawable(R.drawable.defaulthead);
        }

    }



    @Override
    public View getContentView() {
        return inflate(getContext(), R.layout.page_item_startelectronicard, null);
    }

    @Override
    public void onClick(View view) {
        if (view==btNext){
            baseData1.sendResult(3);
        }
        toPlay();
    }

    @Override
    public void timeOut() {
        toPlay();
    }

    public void toPlay() {
        Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
        getContext().sendBroadcast(intent);
    }
}
