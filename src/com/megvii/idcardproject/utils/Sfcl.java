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
		if (lb.equals("��ͨ��Сѧ��"))
			return 180;
		if (lb.equals("��ͨ��ѧ��"))
			return 180;
		if (lb.equals("��ͨ�������"))
			return 180;
		if (lb.equals("�����Ͷ�����ηǴ�ҵ����"))
			return 180;
		return 0;
	}

	private static int switchC(String lb) {
		if (lb.equals("��ͨ��Сѧ��"))
			return 150;
		if (lb.equals("��ͨ��ѧ��"))
			return 150;
		if (lb.equals("��ͨ�������"))
			return 150;
		if (lb.equals("�������ͥ60������������"))
			return 60;
		return 0;
	}

	private static int switchG(String lb) {
		return 150;
	}
}
