package controller;

import java.util.ArrayList;
import java.util.InputMismatchException;

import bean.PhoneInfo;
import common.Menu;
import common.SubMenu;
import exception.MenuInputException;

public class PhoneBookManager {
	PhoneDao phoneDao = new PhoneDao();	// 기능마다 쓰므로 전역으로 선언
	
	public void inputData() {
		System.out.printf("메뉴를 선택하세요\n");
		System.out.printf("1. 일반\n");
		System.out.printf("2. 대학\n");
		System.out.printf("3. 회사\n");
		System.out.printf("\n");
		System.out.printf("메뉴 선택 : ");

		try {
			int menuChoice = MenuInputException.readSubMenu();
			System.out.printf("\n");
			switch (menuChoice) {
			case SubMenu.NORMAL: {
				phoneDao.insert(PhoneMaker.inputDataBasic());
				break;
			}
			case SubMenu.UNIVERSE: {
				phoneDao.insert(PhoneMaker.inputDataUniv());
				break;
			}
			case SubMenu.COMPANY: {
				phoneDao.insert(PhoneMaker.inputDataCompany());
				break;
			}
			default:
			}
		} catch (InputMismatchException | MenuInputException e) {	// 메뉴 숫자 예외처리
			e.printStackTrace();
		}
	}

	// 이름기준으로 동명이인까지 전부 출력
	public void searchData() {
		System.out.printf("검색할 이름 입력 : ");
		String name = Menu.SCANNER.next();
		ArrayList<PhoneInfo> phoneList = phoneDao.search(name);
		System.out.printf("\n");
		if (phoneList.size() == 0) {	// #1. 아무것도 없다면 길이가 0일것, 무엇보다 == null 안먹힐꺼임
			System.out.printf("검색결과가 없습니다.\n");
		}
		else {
			for (PhoneInfo phoneInfo : phoneList) {
				System.out.printf("%s\n", phoneInfo);
			}
		}
		System.out.printf("\n");
	}

	// 전체출력
	public void showAllData() {
		ArrayList<PhoneInfo> phoneList = phoneDao.showAll();
		System.out.printf("\n");
		if (phoneList.size() == 0) {	// #1
			System.out.printf("저장된 데이터가 없습니다.\n");
		}
		else {
			for (PhoneInfo phoneInfo : phoneList) {
				System.out.printf("%s\n", phoneInfo);
			}
		}
		System.out.printf("\n"); 	
	}
	
	public void updateData() {
		System.out.printf("수정할 이름 입력 : ");
		String name = Menu.SCANNER.next();
		System.out.printf("수정 피타입 선택하세요\n");
		System.out.printf("1. 일반\n");
		System.out.printf("2. 대학\n");
		System.out.printf("3. 회사\n");
		System.out.printf("\n");
		System.out.printf("메뉴 선택 : ");		
		try {
			int menuChoice = MenuInputException.readSubMenu();
			System.out.printf("\n");
			switch (menuChoice) {
			case SubMenu.NORMAL: {
				phoneDao.update(name, PhoneMaker.inputDataBasic());
				break;
			}
			case SubMenu.UNIVERSE: {
				phoneDao.update(name, PhoneMaker.inputDataUniv());
				break;
			}
			case SubMenu.COMPANY: {
				phoneDao.update(name, PhoneMaker.inputDataCompany());
				break;
			}
			default:
			}
		} catch (InputMismatchException | MenuInputException e) {	// 메뉴 숫자 예외처리
			e.printStackTrace();
		}
	}

	// 전화번호기준으로 삭제
	public void deleteData() {
		System.out.printf("삭제할 전화번호 입력 : ");
		String phone = Menu.SCANNER.next();
		phoneDao.delete(phone);
	}
}