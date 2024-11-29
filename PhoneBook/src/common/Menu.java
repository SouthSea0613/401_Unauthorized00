package common;

import java.util.Scanner;

public class Menu {
	public static final Scanner SCANNER = new Scanner(System.in);	// 싱글톤
	
	public static void showMenu() {
		System.out.printf("메뉴를 선택하세요\n");
		System.out.printf("1. 데이터 입력\n");
		System.out.printf("2. 검색\n");
		System.out.printf("3. 전체 출력\n");
		System.out.printf("4. 수정\n");
		System.out.printf("5. 삭제\n");
		System.out.printf("6. 종료\n");
		System.out.printf("\n");
		System.out.printf("메뉴 선택 : ");
	}
}
