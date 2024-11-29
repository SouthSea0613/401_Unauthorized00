package controller;

import java.util.InputMismatchException;
import common.MainMenu;
import common.Menu;
import exception.MenuInputException;

public class PhoneBookMain {
	public static void main(String[] args) {
		PhoneBookManager manager = new PhoneBookManager();

		while (true) {
			Menu.showMenu();

			try {
				int menuChoice = MenuInputException.readMainMenu();
				System.out.printf("\n");

				switch (menuChoice) {
					case MainMenu.INPUT: {
						manager.inputData();
						break;
					}
					case MainMenu.SEARCH: {
						manager.searchData();
						break;
					}
					case MainMenu.SHOWALL: {
						manager.showAllData();
						break;
					}
					case MainMenu.UPDATE: {
						manager.updateData();
						break;
					}
					case MainMenu.DELETE: {
						manager.deleteData();
						break;
					}
					case MainMenu.EXIT: {
						System.out.printf("exit");
						return;
					}
				}
			} catch (InputMismatchException | MenuInputException e) {	// 메뉴 숫자 예외처리
				e.printStackTrace();
			}

			Menu.SCANNER.nextLine();	// 버퍼 클리어
		}
	}
}