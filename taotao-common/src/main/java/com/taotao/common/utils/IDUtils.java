package com.taotao.common.utils;

import java.util.Random;

/**
 * @author medxing
 * 各种id生成策略，
 */
public class IDUtils {

	/**
	 * 图名生成
	 */
	public static String genImageName() {
//		取当前时间的长整型值，包含毫秒
		long millis = System.currentTimeMillis();
//		加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
//		如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		return str;
	}
	
	/**
	 * 商品id生成
	 */
	public static long genItemId() {
		long millis = System.currentTimeMillis();
//		加上两位随机数
		Random random = new Random();
		int end2 = random.nextInt(99);
		
		String str = millis + String.format("%02d", end2);
		long id = new Long(str);
		return id;
	}
	
}
