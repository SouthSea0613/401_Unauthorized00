package controller;

import bean.PhoneCompanyInfo;
import bean.PhoneInfo;
import bean.PhoneUnivInfo;
import common.Menu;

// PhoneBookManager가 너무 길어져서 생성자 부분만 분리
public class PhoneMaker {
	
	// 각 생성자 공통부분
	private static String[] inputDataDefault() {
		String[] tmp = new String[2];

		System.out.printf("이름 : ");
		tmp[0] = Menu.SCANNER.next();
		System.out.printf("전화번호 : ");
		tmp[1] = Menu.SCANNER.next();

		return tmp;
	}

	public static PhoneInfo inputDataBasic() {
		String[] tmp = inputDataDefault();

		return new PhoneInfo(tmp[0], tmp[1]);
	}

	public static PhoneInfo inputDataUniv() {
		String[] tmp = inputDataDefault();
		System.out.printf("전공 : ");
		String major = Menu.SCANNER.next();
		System.out.printf("학년 : ");
		int grade = Menu.SCANNER.nextInt();

		return new PhoneUnivInfo(tmp[0], tmp[1], major, grade);
	}

	public static PhoneInfo inputDataCompany() {
		String[] tmp = inputDataDefault();
		System.out.printf("회사 : ");
		String company = Menu.SCANNER.next();

		return new PhoneCompanyInfo(tmp[0], tmp[1], company);
	}
}
