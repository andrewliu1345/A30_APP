package com.joesmate.pdf;

import android.util.Log;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.io.FileOutputStream;


public class HandWriteToPDF {
	private String InPdfFilePath;
	private String outPdfFilePath;
	private String InPicFilePath;
	public static int writePageNumble;//要签名的页码
	HandWriteToPDF(){
		
	}
	public HandWriteToPDF(String InPdfFilePath, String outPdfFilePath, String InPicFilePath){
		this.InPdfFilePath = InPdfFilePath;
		this.outPdfFilePath = outPdfFilePath;
		this.InPicFilePath = InPicFilePath;
	}
	public String getInPdfFilePath() {
		return InPdfFilePath;
	}
	public void setInPdfFilePath(String inPdfFilePath) {
		InPdfFilePath = inPdfFilePath;
	}
	public String getOutPdfFilePath() {
		return outPdfFilePath;
	}
	public void setOutPdfFilePath(String outPdfFilePath) {
		this.outPdfFilePath = outPdfFilePath;
	}
	public String getInPicFilePath() {
		return InPicFilePath;
	}
	public void setInPicFilePath(String inPicFilePath) {
		InPicFilePath = inPicFilePath;
	}

	
	
	/**
	* pagenumber： 从1开始，  picwidth ：图片宽度   picheight : 图片高度     左下角开始坐标原点 leftDownx
	*@author bill
	* created at  2016/1/14  11:22
	**/
	public void addText(int pageNumber,float picWidth, float picHeight, float leftDownX, float leftDownY){
		try{
			PdfReader reader = new PdfReader(InPdfFilePath);//选择需要印章的pdf
            float pdfwidth = reader.getPageSize(pageNumber).getWidth() ;
			float pdfheight = reader.getPageSize(pageNumber).getHeight() ;

            Log.e("bill","00d0000   " + reader.getPageSize(pageNumber).toString());
            FileOutputStream outStream = new FileOutputStream(outPdfFilePath);
			PdfStamper stamp;
			stamp = new PdfStamper(reader, outStream);//加完印章后的pdf
			PdfContentByte over = stamp.getOverContent(pageNumber);//设置在第几页打印印章
			//用pdfreader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信
			//PdfDictionary p = reader.getPageN(1);
			//拿到mediaBox 里面放着该页pdf的大小信息.  
	        //PdfObject po =  p.get(new PdfName("MediaBox"));
	        //po是一个数组对象.里面包含了该页pdf的坐标轴范围.  
	        //PdfArray pa = (PdfArray) po;
			Image img = Image.getInstance(InPicFilePath);//选择图片
			img.setAlignment(1);
			img.scaleAbsolute(picWidth,picHeight);//控制图片大小,原始比例720:360
			//调用书写pdf位置方法
			//writingPosition(img ,pa.getAsNumber(pa.size()-1).floatValue());
			//img.setAbsolutePosition(rectangle.getWidth() / 2 - 200, rectangle.getHeight() - 40);
			img.setAbsolutePosition(leftDownX,leftDownY);
			if(over != null) {
				over.addImage(img);
			}
			stamp.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
