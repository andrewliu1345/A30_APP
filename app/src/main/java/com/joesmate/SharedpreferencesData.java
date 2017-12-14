package com.joesmate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.joesmate.bin.SecretKeyData;
import com.joesmate.bin.icbc.ICBCSetVolumeData;

public class SharedpreferencesData {

	public static final String NAME = "settings.xml";
	private static SharedpreferencesData data;
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;
	
	public static final String KEY_SHOWTIME = "show_time";
	public static final String KEY_PEN_WIDTH = "pen_width";
	public static final String KEY_PEN_COLOR = "pen_color";
	public static final String KEY_JH_TEXT_SIZE = "jh_text_size";
	public static final String KEY_BAUD = "baud";

    //字体：0-黑体、1-宋体、2-仿宋 GB_2312、3-楷体
	public static final String KEY_TOP_FONT_FAMILY = "top_font_family";
	//字体大小：单位 pt
	public static final String KEY_TOP_FONT_SIZE = "top_font_size";
	//字体粗细：0-普通，1-加粗
	public static final String KEY_TOP_FONT_WEIGHT = "top_font_weight";
	//字体颜色：0-黑，1-白，2-红，3-橙，4-黄，5-绿，6-青，7-蓝，8-紫
	public static final String KEY_TOP_FONT_COLOR = "top_font_color" ;
	//背景颜色：0-黑，1-白，2-红，3-橙，4-黄，5-绿，6-青，7-蓝，8-紫
	public static final String KEY_TOP_BGCOLOR = "top_bgcolor" ;
	//状态区的高度，单位像素
	public static final String KEY_TOP_HEIGHT = "top_height" ;
	//播放广告类型
	public static final String KEY_POSTER_TYPE ="poster_type";
	//当前音量类型
	public static final String KEY_CURRENT_VOLUME_TYPE = "current_volume_type";
	//业务音量大小
	public static final String KEY_BUSINESS_VOLUME = "business_volume";
	//广告音量大小
	public static final String KEY_ADVERTISEMENT_VOLUME = "advertisement_volume";
	
	public static final int DEF_SHOWTIME = 10;
	public static final int DEF_PEN_WIDTH = 1;
	public static final int DET_PEN_COLOR = 0;
	public static final int DEF_JH_FONTSIZE = 30;
	public static final int DEF_BAUD = 2;


	public static final  int DEF_TOP_FONT_FAMILY = 1 ;
	public static final  int DEF_TOP_FONT_SIZE = 20 ;
	public static final  int DEF_TOP_FONT_WEIGHT = 0 ;
	public static final  int DEF_TOP_FONT_COLOR = 1 ;
	public static final  int DEF_TOP_BGCOLOR = 2 ;
	public static final  int DEF_TOP_HEIGHT = 80 ;
	public static final  int DEF_POSTER_TYPE = 0 ;

	public static final int DEF_CURRENT_VOLUME_TYPE = ICBCSetVolumeData.Volume_TYPE_Business ;
	public static final int DEF_BUSINESS_VOLUME = 2;
	public static final int DEF_ADVERTISEMENT_VOLUME = 3 ;
	
	private int showTime;
	private int penWidth;
	private int penColor;
	private int jhFontSize;
	private int baud;


	private  int top_font_family ;
	private  int top_font_size ;
	private  int top_font_weight ;
	private  int top_font_color ;
	private  int top_bgcolor ;
	private  int top_height ;
	private  int poster_type ;



	private  int current_volume_type ;
	private  int business_volume ;
	private  int advertisement_volume ;

	public int getCurrent_volume_type() {
		return current_volume_type;
	}

	public void setCurrent_volume_type(int current_volume_type) {
		this.current_volume_type = current_volume_type;
		editor.putInt(KEY_CURRENT_VOLUME_TYPE, current_volume_type);
		editor.commit();
	}

	public int getBusiness_volume() {
		return business_volume;
	}

	public void setBusiness_volume(int business_volume) {
		this.business_volume = business_volume;
		editor.putInt(KEY_BUSINESS_VOLUME, business_volume);
		editor.commit();
	}

	public int getAdvertisement_volume() {
		return advertisement_volume;
	}

	public void setAdvertisement_volume(int advertisement_volume) {
		this.advertisement_volume = advertisement_volume;
		editor.putInt(KEY_ADVERTISEMENT_VOLUME, advertisement_volume);
		editor.commit();
	}



	public SharedpreferencesData(){
		preferences = App.getInstance().getSharedPreferences(NAME, Activity.MODE_PRIVATE);
		editor = preferences.edit();
		init();
	}
	private void init(){
		showTime = preferences.getInt(KEY_SHOWTIME, DEF_SHOWTIME);
		penWidth = preferences.getInt(KEY_PEN_WIDTH, DEF_PEN_WIDTH);
		jhFontSize = preferences.getInt(KEY_JH_TEXT_SIZE, DEF_JH_FONTSIZE);
		baud = preferences.getInt(KEY_BAUD, DEF_BAUD);

		top_font_family = preferences.getInt(KEY_TOP_FONT_FAMILY,DEF_TOP_FONT_FAMILY);
		top_font_size = preferences.getInt(KEY_TOP_FONT_SIZE,DEF_TOP_FONT_SIZE);
		top_font_weight = preferences.getInt(KEY_TOP_FONT_WEIGHT,DEF_TOP_FONT_WEIGHT);
		top_font_color = preferences.getInt(KEY_TOP_FONT_COLOR,DEF_TOP_FONT_COLOR);
		top_bgcolor = preferences.getInt(KEY_TOP_BGCOLOR, DEF_TOP_BGCOLOR);
		top_height = preferences.getInt(KEY_TOP_HEIGHT, DEF_TOP_HEIGHT);
		poster_type = preferences.getInt(KEY_POSTER_TYPE,DEF_POSTER_TYPE);
		current_volume_type = preferences.getInt(KEY_CURRENT_VOLUME_TYPE,DEF_CURRENT_VOLUME_TYPE);
		business_volume = preferences.getInt(KEY_BUSINESS_VOLUME,DEF_BUSINESS_VOLUME);
		advertisement_volume = preferences.getInt(KEY_ADVERTISEMENT_VOLUME,DEF_ADVERTISEMENT_VOLUME);

		new SecretKeyData.SaxKeyXml(FileInf.KEYS_PATH, FileInf.KEY_NAME);
	}

	public static SharedpreferencesData getInstance(){
		if(data == null){
			data = new SharedpreferencesData();
		}
		return data;
	}
	
	public int getJhFontSize() {
		return jhFontSize;
	}
	public void setJhFontSize(int jhFontSize) {
		this.jhFontSize = (jhFontSize== 0?1:jhFontSize);
		editor.putInt(KEY_JH_TEXT_SIZE, jhFontSize);
		editor.commit();
	}
	public int getShowTime() {
		return showTime;
	}

	public void setShowTime(int showTime) {
		this.showTime = showTime;
		editor.putInt(KEY_SHOWTIME, showTime);
		editor.commit();
	}
	public int getPenWidth() {
		return penWidth;
	}
	public void setPenWidth(int penWidth) {
		if(penWidth == 0){
			penWidth =1;
		}
		this.penWidth = penWidth;
		editor.putInt(KEY_PEN_WIDTH, penWidth);
		editor.commit();
	}
	public int getPenColor() {
		return penColor;
	}
	public int getPenColorValue(){
		if(penColor == 0){
			return Color.BLACK;
		}else if(penColor == 1){
			return Color.RED;
		}else if(penColor == 2){
			return Color.BLUE;
		}
		return Color.BLACK;
	}
	public void setPenColor(int penColor) {
		this.penColor = penColor;
		editor.putInt(KEY_PEN_COLOR, penColor);
		editor.commit();
	}
	public int getBaud() {
		return baud;
	}
	public int getBaudValue(){
		if(baud == 1){
			return 1200;
		}else if(baud == 2){
			return 9600;
		}else if(baud == 3){
			return 115200;
		}
		return 9600;
	}
	public void setBaud(int baud) {
		this.baud = baud;
		editor.putInt(KEY_BAUD, baud);
		editor.commit();
	}

	public int getTop_font_family()
	{
		return  top_font_family;
	}
	public  void setTop_font_family(int top_font_famil_m )
	{
		this.top_font_family = top_font_famil_m ;
		editor.putInt(KEY_TOP_FONT_FAMILY,top_font_famil_m);
		editor.commit();
	}


	public  int getDefTopFontSize ()
	{
		return  top_font_size ;
	}

	public  void setTop_font_size( int top_font_size_m)
	{
		this.top_font_size = top_font_size_m ;
		editor.putInt(KEY_TOP_FONT_SIZE,top_font_size_m);
		editor.commit();
	}


	public  int getTop_font_weight ()
	{
		return top_font_weight ;
	}

	public void setTop_font_weight(int top_font_weight_m)
	{
		this.top_font_weight = top_font_weight_m ;
		editor.putInt(KEY_TOP_FONT_WEIGHT,top_font_weight_m);
		editor.commit() ;
	}


	public  int getTop_font_color()
	{
		 return  top_font_color ;
	}

	public  void setTop_font_color(int top_font_color_m)
	{
		this.top_font_color = top_font_color_m ;
		editor.putInt(KEY_TOP_FONT_COLOR,top_font_color_m);
		editor.commit() ;
	}

	public   int getTop_bgcolor()
	{
		return top_bgcolor ;
	}


	public  void setTop_bgcolor( int top_bgcolor_m)
	{
		this.top_bgcolor = top_bgcolor_m ;
        editor.putInt(KEY_TOP_BGCOLOR, top_bgcolor_m);
		editor.commit();
	}


	public  int getTop_height()
	{
		return  top_height ;
	}

	public void setTop_height ( int top_height_m)
	{
		this.top_height = top_height_m ;
		editor.putInt(KEY_TOP_HEIGHT,top_height_m);
		editor.commit() ;
	}


	public  int  getPoster_type ()
	{
		return  poster_type ;
	}

	public  void setPoster_type(int poster_type_m)
	{
		this.poster_type = poster_type_m ;
		editor.putInt(KEY_POSTER_TYPE,poster_type_m);
		editor.commit();
	}



	
	

}
