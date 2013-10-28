package com.leading.baselibrary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.leading.baselibrary.entity.ConfigureEntity;

/**
 * 配置文件工具类.
 * @author Jiantao.tu
 *
 */
public class ConfigureUtil {
	private final String fileName="configure.json";
	private Gson gson = new Gson();
	private static ConfigureUtil configureUtil;
	private ConfigureUtil(){}
	
	/**
	 * 单例.
	 * @author JianTao.tu
	 * @return 返回自身
	 */
	public static ConfigureUtil getConfigureUtil(){
		if(configureUtil==null){
			return configureUtil=new ConfigureUtil();
		}
		else
			return configureUtil;
	}
	
	/**
	 * 将配置对象转换为JSON进行文件储存.
	 * @author JianTao.tu
	 * @param context 上下文
	 * @param configureEntity 配置对象
	 */
	public void save(Context context, ConfigureEntity configureEntity) {
		try {
			FileOutputStream outStream = context.openFileOutput(
					"configure.json", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			String json = gson.toJson(configureEntity);
			outStream.write(json.getBytes("UTF-8"));
			outStream.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * 取出配置文件转换为配置对象.
	 * @author JianTao.tu
	 * @param context 上下文
	 * @return 配置对象
	 */
	public ConfigureEntity get(Context context) {
		try {
			FileInputStream inputStream=context.openFileInput(fileName);
			return readFile(inputStream, context);
		} catch (FileNotFoundException e) {
			save(context,new ConfigureEntity());
			return get(context);
		}
	}
	
	/**
	 * 同进程跨应用取出配置文件转换为配置对象.
	 * @param context
	 * @param file 上下文
	 * @return 配置对象
	 */
	public ConfigureEntity getStride(Context context,File file){
		try {
			FileInputStream inputStream=new FileInputStream(file);
			return readFile(inputStream, context);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	public ConfigureEntity readFile(FileInputStream inputStream,Context context){
		StringBuffer sb=new StringBuffer();
		try {
			byte[] bytes = new byte[1024];  
           while (inputStream.read(bytes) != -1) {  
           	  sb.append(new String(bytes,"UTF-8"));    
           	  bytes=new byte[1024];//重新生成，避免和上次读取的数据重复
           } 
			return gson.fromJson(sb.toString().trim(), ConfigureEntity.class);
		} catch (JsonSyntaxException e) {
			return new ConfigureEntity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null)
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
