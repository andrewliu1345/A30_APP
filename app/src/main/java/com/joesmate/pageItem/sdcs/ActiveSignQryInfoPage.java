package com.joesmate.pageItem.sdcs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.githang.android.snippet.adapter.ChoiceListAdapter;
import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.R;
import com.joesmate.bin.sdcs.ActiveSignQryInfoData;
import com.joesmate.page.PlayActivity;
import com.joesmate.pageItem.BasePageItem;

import java.util.ArrayList;

/**
 * Created by bill on 2016/5/14.
 */
public class ActiveSignQryInfoPage extends BasePageItem
{

    ActiveSignQryInfoData baseData ;

    LinearLayout tableHeadLinearLayout;
    ListView tableListView;
    TextView txtAccount ,txtmainTitle;
    Context mContext ;
    int selectType ;
    int totalNumber ;
    ChoiceListAdapter adapter ;

    public ActiveSignQryInfoPage(Context context) {
        super(context);
        mContext = context;
        baseData = ActiveSignQryInfoData.getInstance();
        setButtonType(ButtonType.OK_CANCEL);
        tvTitle.setVisibility(GONE);
        tableHeadLinearLayout= (LinearLayout)findViewById(R.id.table_head);
        tableListView = (ListView)findViewById(R.id.listView);
        txtAccount = (TextView)findViewById(R.id.txtaccount);
        txtAccount.setText(baseData.getAccount());
        txtmainTitle = (TextView)findViewById(R.id.mainTitle);
        txtmainTitle.setText(baseData.getTitle());

    }


    public void init() {
        if(baseData !=null)
        {
            App.getInstance().tts.Read(baseData.getVoiceText(), 1);
            setTimer(baseData.getTimeOut());
            CreateTableHead();
            selectType = baseData.getQuestionClass();
            totalNumber = baseData.getTableContentNumber();
            btOK.setText(baseData.getRightButtonText());
            btCancel.setText(baseData.getLeftButtonText());
            adapter = new ChoiceListAdapter<ArrayList<String>>(mContext, R.layout.layout_listdata_item,
                    getContentDataList() , R.id.chbSelect) {
                @Override
                protected void holdView(ChoiceLayout view) {
                    view.hold(R.id.content);
                }

                @Override
                protected void bindData(ChoiceLayout view, int position, ArrayList<String> data) {
                    LinearLayout linearLayout = view.get(R.id.content);
                    linearLayout.setWeightSum(getWeightSum());
                    linearLayout.removeAllViews();

                    String[] heads = getTableHead();
                    ArrayList<Float> widths = getWeight();
                    for(int i = 0 ; i <  heads.length ; ++ i)
                    {
                        TextView t1 = new TextView(mContext);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 50,widths.get(i).floatValue());
                        t1.setGravity(Gravity.CENTER);
                        t1.setText(data.get(i));
                        t1.setTextSize(20);
                        t1.setTextColor(Color.BLACK);
                        t1.setLayoutParams(params1);
                        linearLayout.addView(t1);
                    }

                }
            };

            if(selectType == ActiveSignQryInfoData.TYPE_OPTION_SINGLE) {
                tableListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
            else{
                tableListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            tableListView.setAdapter(adapter);


        }
    }

    public  String[] getTableHead()
    {
        if(baseData != null)
        {
            return   AssitTool.getSplit(baseData.getTableTitle(),"\\|");
        }
        else
        {
            return  null;
        }
    }

    public ArrayList<Float> getWeight()
    {
        if(baseData != null)
        {
            String[] strWeight =  AssitTool.getSplit(baseData.getColWidthAbs(),"\\|");
            ArrayList<Float>  weightList = new ArrayList<Float>(strWeight.length);
            for (int i = 0 ; i < strWeight.length ; ++ i)
            {
                weightList.add(Float.parseFloat(strWeight[i]));
            }
            return  weightList ;
        }
        else
        {
            return  null;
        }
    }

    public float getWeightSum()
    {
        float sum  = 0.0f;
        ArrayList<Float> list =  getWeight();
        for (int i = 0 ;i < list.size(); ++ i )
        {
            sum = sum + list.get(i).floatValue();
        }
        return  sum ;
    }


    public ArrayList<ArrayList<String>> getContentDataList()
    {

        if(baseData != null) {
            ArrayList<ArrayList<String>> list  = new ArrayList<ArrayList<String>>(totalNumber);
            String[] data =  AssitTool.getSplit(baseData.getTableContent(), "\\|");
            String[] heads = getTableHead();
            int size = heads.length;
            for(int i = 0 ; i < totalNumber ; ++ i)
            {
                ArrayList<String> list1 = new ArrayList<String>(size);
                for(int j = 0; j < size ;++ j)
                {
                    list1.add(data[i*size+j]);
                }
                list.add(list1);
            }
            return  list;
        }
        return  null;
    }



    public  void CreateTableHead()
    {
        tableHeadLinearLayout.removeAllViews();
        tableHeadLinearLayout.setWeightSum(getWeightSum());
        String[] heads = getTableHead();
        ArrayList<Float> widths = getWeight();
        for(int i = 0 ; i <  heads.length ; ++ i)
        {
            TextView t1 = new TextView(mContext);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,widths.get(i).floatValue());
            t1.setGravity(Gravity.CENTER);
            t1.setText(heads[i]);
            t1.setTextSize(20);
            t1.setTextColor(Color.BLACK);
            t1.setLayoutParams(params1);
            tableHeadLinearLayout.addView(t1);
        }
    }


    public String getResult()
    {
        StringBuilder result = new StringBuilder();
        SparseBooleanArray data =  tableListView.getCheckedItemPositions();
        for(int i= 0; i < data.size(); ++ i)
        {
            result.append(data.keyAt(i)+1+",");
        }
        if(result.length()>0) {
            result.deleteCharAt(result.length() - 1);
        }
        Log.d("bill","result:"+result.toString());
        return  result.toString() ;

    }


    @Override
    public View getContentView() {
        return inflate(getContext(), R.layout.page_item_active_sign_qryinfo_page, null);
    }

    @Override
    public void onClick(View view) {


        if(view == btOK )
        {
            if(!"".equals(getResult())) {
                ActiveSignQryInfoData.getInstance().sendConfirmResult("0", getResult());
                toPlay();
            }
        }

        if(view == btCancel)
        {
            ActiveSignQryInfoData.getInstance().sendConfirmResult("1","");
            toPlay();
        }
    }

    @Override
    public void timeOut() {
        ActiveSignQryInfoData.getInstance().sendConfirmResult("2","");
        toPlay();
    }

    public void toPlay() {
        Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
        getContext().sendBroadcast(intent);
    }
}
