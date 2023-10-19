package org.example.login;

import org.example.menu.Menu;

import java.util.Scanner;

public class Main {

    private static Authentication log = new Authentication();
    private static Menu start = new Menu();

    public static void main(String[] args) {
        System.out.println("\t\t---> Система учета сотрудников <---\n");
        System.out.println("Добро пожаловать в систему учета сотрудников!\n");
        boolean run = true;

        while (run) {
            Scanner scr = new Scanner(System.in);
            System.out.println("[1] - Вход\n[2] - Выход");
            int input = Integer.parseInt(scr.nextLine());
            switch (input) {
                case 1 -> {
                    System.out.print("Логин: ");
                    String login = scr.nextLine();
                    System.out.print("Пароль: ");
                    String password = scr.nextLine();

                    if (log.authentication(login, password)) {
                        System.out.println("\nВход выполнен успешно!\n");
                        start.menu();
                    } else {
                        System.out.println("\nНеверные данные!\n");
                    }
                }
                case 2 -> run = false;
                default -> System.out.println("Неверная команда! Выберите одну из этих: ");
            }
        }
    }
}