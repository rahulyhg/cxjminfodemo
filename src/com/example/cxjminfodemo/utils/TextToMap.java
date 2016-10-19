package com.example.cxjminfodemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建�?个file，后�?名为cfg. 里面文本格式为每行：A=X结构，其中注释在�?前面加上# 然后将该文件保存到map中（map(A，X);
 * 
 * @author Administrator
 * 
 */
public class TextToMap {

	private String line = System.getProperty("line.separator");
	private String separ = "=";
	static String basePath = System.getProperty("user.dir");
	static String filePath = "countryCode.txt";// 文件相对路径

	public static void main(String[] args) {
		TextToMap fileToMap1 = new TextToMap();
		Map<String, String> oldMap = fileToMap1.fTM();
	}

	/**
	 * 有参构�?�函�?
	 * 
	 * @param separ
	 * @param filePath
	 */
	public TextToMap(String separ, String filePath) {
		this.filePath = filePath;
		this.separ = separ;
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("文件不存�?");
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("路径:" + filePath + "创建失败");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 无参构�?�函数，用类默认的配置�??
	 */
	public TextToMap() {
		/*File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("文件不存�?");
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("路径:" + filePath + "创建失败");
				e.printStackTrace();
			}
		}*/
	}

	public Map<String, String> TextToMap(InputStream inputStream) {
		// TODO Auto-generated constructor stub
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		/*
		 * StringBuffer sb = new StringBuffer(""); String line; try { while
		 * ((line = reader.readLine()) != null) { sb.append(line);
		 * sb.append("\n"); } } catch (IOException e) { e.printStackTrace(); }
		 * return sb.toString();
		 */
		Map<String, String> map = new HashMap<String, String>();
		try {
			System.out.println("duasdfasdfvadfvb");
			String tempString = null;
			int line = 1;
			// �?次读入一行，直到读入null为文件结�?
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				if (!tempString.startsWith("#")) {
					String[] strArray = tempString.split("	");
					map.put(strArray[0], strArray[1]);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		for (Map.Entry entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		return map;
	}

	/*
	 * 将map写入到file文件中�?�默认map（String A,String A')file中以A=A'来表示，map中每个键值对显示�?�?
	 * 
	 * @throws IOException /
	 */
	private File mapToFileDefault(Map<String, String> map, File file, String separ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		FileWriter writer = new FileWriter(file, false);
		for (Map.Entry entry : map.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			buffer.append(key + "=" + value).append(line);
		}
		writer.write(buffer.toString());
		writer.close();
		return file;

	}

	/**
	 * 在newMap替换oldMap时，是否覆盖（isOverwrite)如果是，就直接替换，如果否，则将oldMap中的key前加"#"，默认为�?
	 * 
	 * @param newMap
	 * @param oldMap
	 * @return
	 */
	private Map<String, String> newMapToOldMapDefault(Map<String, String> newMap, Map<String, String> oldMap) {
		return newMapToOldMap(newMap, oldMap, false);
	}

	/**
	 * 在newMap替换oldMap时，是否覆盖（isOverwrite)如果是，就直接替换，如果否，则将oldMap中的key前加"#"，默认为�?
	 */
	private Map<String, String> newMapToOldMap(Map<String, String> newMap, Map<String, String> oldMap,
			boolean isOverwrite) {
		// 由于oldMap中包含了file中更多内容，�?以newMap中内容在oldMap中调整后，最后返回oldMap修改之后的map.
		// 如果选择true覆盖相同的key
		if (isOverwrite) {
			// 循环遍历newMap
			for (Map.Entry entry : newMap.entrySet()) {
				String newKey = (String) entry.getKey();
				String newValue = (String) entry.getValue();
				oldMap.put(newKey, newValue);
			}
		} else {
			// 不覆盖oldMap,�?要在key相同的oldMap的key前加#�?
			// 循环遍历newMap
			for (Map.Entry entry : newMap.entrySet()) {
				String newKey = (String) entry.getKey();
				String newValue = (String) entry.getValue();
				String oldValue = oldMap.get(newKey);
				oldMap.put("#" + newKey, oldValue);
				oldMap.put(newKey, newValue);
			}
		}
		return oldMap;
	}

	/**
	 * 将文件转换成map存储
	 * 
	 * @return
	 */
	public Map<String, String> fTM() {
		Map<String, String> map = new HashMap<String, String>();
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，�?次读�?整行�?");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// �?次读入一行，直到读入null为文件结�?
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				if (!tempString.startsWith("#")) {
					String[] strArray = tempString.split("	");
					map.put(strArray[0], strArray[1]);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		for (Map.Entry entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		return map;
	}
}
