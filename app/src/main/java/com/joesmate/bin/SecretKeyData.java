package com.joesmate.bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
/**
 * 密钥
 * 根据密钥号，存在就覆盖、否则增加
 * 每次提取xml文件数据，然后删除xml文件重新写入xml文件
 * @author yc.zhang
 *
 */
public class SecretKeyData extends BaseData{

	public static final String XML_TAG = "Key";
	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_TYPE = "type";
	public static final String XML_ATTR_VALUE = "value";
	private static SecretKeyData data;
	public static boolean isCreateXml;
	private List<KeyData> keyDatas = new ArrayList<SecretKeyData.KeyData>();
	public static SecretKeyData getInstance(){
		if(data == null){
			data = new SecretKeyData();
		}
		return data;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		
		int pos = 2;
		KeyData keyData = new KeyData();
		keyData.id = buffer[pos];
		Log.d(TAG, "key_id"+keyData.id);
		
		pos = 3;
		keyData.type = buffer[pos];
		Log.d(TAG, "key_type"+keyData.type);
		
		int keyLen = getKeyLen(keyData.type);
		if(keyLen == -1){
			Log.d(TAG, "keyLen is -1");
			return;
		}
		pos = 4;
		keyData.keys = new byte[keyLen];
		//if(keyLen < buffer.length - pos)
		System.arraycopy(buffer, pos, keyData.keys, 0, keyLen);
		String keys = AssitTool.combinationArray( keyData.keys);
		Log.d(TAG, "key_value："+keys);
		//
		int keyIndex = isExistKey(keyData);
		if(keyIndex != -1){
			KeyData exitKeyData = keyDatas.get(keyIndex);
			exitKeyData.type = keyData.type;
			exitKeyData.keys = new byte[keyData.keys.length];
			System.arraycopy(keyData.keys, 0, exitKeyData.keys, 0, keyData.keys.length);
		}else {
			keyDatas.add(keyData);
		}
		backData((Cmds.BACK_KA+BackCode.CODE_00).getBytes());
		if(!isCreateXml){
			isCreateXml = true;
			//handler.sendEmptyMessageDelayed(0, 0x1F * 1000);
			new CreateKeyXml(keyDatas, FileInf.KEYS_PATH, FileInf.KEY_NAME);
		}
		
	}

	private int isExistKey(KeyData data){
		for(int i = 0; i < keyDatas.size(); i ++){
			Log.d(TAG, "key_id");
			if(keyDatas.get(i) != null && keyDatas.get(i).id == data.id){
				return i;
			}
		}
		return -1;
	}
	private int getKeyLen(int type){
		if(type == 1){
			return 8;
		}else if(type == 2){
			return 16;
		}else if(type == 3){
			return 24;
		}
		return -1;
	}
	
	
	public List<KeyData> getKeyDatas() {
		return keyDatas;
	}
	public void setKeyDatas(List<KeyData> keyDatas) {
		this.keyDatas = keyDatas;
	}
	public static class SaxKeyXml implements Runnable{
		InputStream is = null;
		public SaxKeyXml(String xmlPath,String xmlName){
			File dir = new File(xmlPath);
			if(dir == null || !dir.exists()){
				return;
			}
			File file = new File(dir, xmlName);
			if(file == null || !file.exists()){
				return;
			}
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread(this).start();
		}
		@Override
		public void run() {
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp = null;
			try {
				sp = factory.newSAXParser();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			if (null != sp) {
				
				try {
					sp.parse(is, new XMLDefaultHandler());
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
							is = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		
		
		}
		class XMLDefaultHandler extends DefaultHandler{

			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				// TODO Auto-generated method stub
				if(XML_TAG.equals(localName)){
					KeyData keyData = new KeyData();
					for (int i = 0; i < attributes.getLength(); i++) {
						String attrName = attributes.getLocalName(i);
						String value = attributes.getValue(i);
						if(XML_ATTR_ID.equals(attrName)){
							int keyId = AssitTool.getIntenter(value);
							if(keyId == -1){
								continue;
							}
							Log.d(TAG, "keyId："+keyId);
							keyData.setId(keyId);
						}else if(XML_ATTR_VALUE.equals(attrName)){
							String[] keyValue = AssitTool.getSplit(value, AssitTool.SPLIT_LINE);
							keyData.keys = new byte[keyValue.length];
							for(int n = 0 ; n < keyValue.length; n ++){
								byte key = AssitTool.parseByte(keyValue[n]);
								if(key == -1){
									continue;
								}
								keyData.keys[i] = key;
							}
						}
					}
				}
			}
			
			
			
		}
	}
	
	public static class CreateKeyXml implements Runnable{
		private FileOutputStream fos;
		private XmlSerializer serializer;
		private List<KeyData> keyDatas;
		private String xmlPath, xmlName;
		public CreateKeyXml(List<KeyData> keyDatas,String xmlPath,String xmlName){
			this.keyDatas = keyDatas;
			this.xmlName = xmlName;
			this.xmlPath = xmlPath;
			new Thread(this).start();
		}

		@Override
		public void run() {
			File dir = new File(xmlPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(dir, xmlName);
			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
				serializer = Xml.newSerializer();
				serializer.setOutput(fos, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				serializer.startDocument("UTF-8", null);
				for(KeyData keyData : keyDatas){
					if(keyData == null){
						continue;
					}
					serializer.startTag(null, XML_TAG);
					serializer.attribute(null, XML_ATTR_ID, ""+keyData.id);
					serializer.attribute(null, XML_ATTR_TYPE, ""+keyData.type);
					serializer.attribute(null, XML_ATTR_VALUE, AssitTool.combinationArray(keyData.keys));
					serializer.endTag(null, "Key");
				}
				serializer.endDocument();
				serializer.flush();
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				isCreateXml = false;
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}


	public static class KeyData{
		private int id,type;
		private byte[] keys;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public byte[] getKeys() {
			return keys;
		}
		public void setKeys(byte[] keys) {
			this.keys = keys;
		}
		
		
	}

}
