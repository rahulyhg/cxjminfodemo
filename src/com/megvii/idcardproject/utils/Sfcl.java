package com.megvii.idcardproject.utils;

public class Sfcl {
	public static int Calculate(int sum, String lb, String sfcl) {
		switch (sfcl) {
		case "B":
			sum = sum + switchB(lb);
			break;
		case "C":
			sum = sum + switchC(lb);
			break;
		case "G":
			sum = sum + switchG(lb);
			break;
		}
		return sum;
	}

	private static int switchB(String lb) {
		if (lb.equals("普通中小学生"))
			return 180;
		if (lb.equals("普通大学生"))
			return 180;
		if (lb.equals("普通城乡居民"))
			return 180;
		if (lb.equals("处于劳动年龄段非从业居民"))
			return 180;
		return 0;
	}

	private static int switchC(String lb) {
		if (lb.equals("普通中小学生"))
			return 150;
		if (lb.equals("普通大学生"))
			return 150;
		if (lb.equals("普通城乡居民"))
			return 150;
		if (lb.equals("低收入家庭60岁以上老年人"))
			return 60;
		return 0;
	}

	private static int switchG(String lb) {
		return 150;
	}
}
