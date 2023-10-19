package org.example.menu;

import org.example.employee.EmployeeManagementSystem;

import java.text.ParseException;
import java.util.Scanner;

public class Menu {

    private EmployeeManagementSystem employees = new EmployeeManagementSystem();
    public void menu() {
        boolean run = true;

        while (run) {
            Scanner scr = new Scanner(System.in);
            System.out.println("---> Меню <---");
            System.out.println("[1] - Просмотр информации о сотрудниках\n[2] - Добавление нового сотрудника\n" +
                    "[3] - Увольнение сотрудника\n[4] - Редактирование инфоромации о сотруднике\n" +
                    "[5] - Поиск сотрудников\n[6] - Создание отчетов\n[7] - Сохранить и выйти");

            int input = Integer.parseInt(scr.nextLine());
            switch (input) {
                case 1 -> employees.listAllEmployees();
                case 2 -> {
                    try {
                        employees.hireEmployee();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> employees.fireEmployee();
                case 4 -> employees.editEmployee();
                case 5 -> employees.searchEmployee();
                case 6 -> employees.reporting();
                case 7 -> {
                    try {
                        employees.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    run = false;
                }
            }
        }
    }
}
