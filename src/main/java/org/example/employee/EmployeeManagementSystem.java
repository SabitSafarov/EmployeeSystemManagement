package org.example.employee;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EmployeeManagementSystem implements Serializable, AutoCloseable {

    private List<Employee> employees;
    private Map<String, String> supervisors = new HashMap<>();
    private int count;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        String data = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/employeeList.bin";
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(data))) {
            Object employee;
            while ((employee = inputStream.readObject()) != null) {
                employees.add((Employee) employee);
                count++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Created new.");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println();
        }

        for (Employee employee : employees) {
            if (employee.getJobTitle().equals("Директор") || employee.getJobTitle().equals("Старший менеджер") ||
                    employee.getJobTitle().equals("Начальник отдела кадров") || employee.getJobTitle().equals("Главный бухгалтер")
                    || employee.getJobTitle().equals("Начальник склада")) {
                supervisors.put(employee.getJobTitle(), employee.getFullName());
            }
        }
    }

    @Override
    public void close() throws Exception {
        String dataTxt = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/employeeList.txt";
        String dataBin = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/employeeList.bin";
        try (FileWriter writer = new FileWriter(dataTxt);
             ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataBin))) {
            for (Employee employee : employees) {
                out.writeObject(employee);
                writer.write(employee.toString() + "\n");
            }
        }
    }

    public void updateInfoSupervisor() {
        for (Employee employee : employees) {
            String supervisor = "нет";
            if (employee.getJobTitle().equals("Директор")) {
                employee.setSupervisor(supervisor);
            } else if (employee.getJobTitle().equals("Старший менеджер") || employee.getJobTitle().equals("Начальник отдела кадров") ||
                    employee.getJobTitle().equals("Главный бухгалтер") || employee.getJobTitle().equals("Начальник склада")) {
                for (Map.Entry<String, String> entry : supervisors.entrySet()) {
                    if (entry.getKey().equals("Директор")) {
                        employee.setSupervisor(entry.getValue());
                        break;
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : supervisors.entrySet()) {
                    if (employee.getJobTitle().equals("Менеджер") && entry.getKey().equals("Старший менеджер")) {
                        supervisor = entry.getValue();
                        employee.setSupervisor(supervisor);
                    } else if (employee.getJobTitle().equals("Сотрудник по подбору") && entry.getKey().equals("Начальник отдела кадров")) {
                        supervisor = entry.getValue();
                        employee.setSupervisor(supervisor);
                    } else if (employee.getJobTitle().equals("Бухгалтер") && entry.getKey().equals("Главный бухгалтер")) {
                        supervisor = entry.getValue();
                        employee.setSupervisor(supervisor);
                    } else if ((employee.getJobTitle().equals("Уборщик-бригадир") || employee.getJobTitle().equals("Водитель")) && entry.getKey().equals("Начальник склада")) {
                        supervisor = entry.getValue();
                        employee.setSupervisor(supervisor);
                    }
                }
            }
        }
    }

    public void listAllEmployees() {
        System.out.println("---> Информация о сотрудниках <---");

        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public void hireEmployee() throws ParseException {
        System.out.println("---> Нанимаем нового сотрудника <---");

        Scanner scr = new Scanner(System.in);

        int id = (count == 0 ? 1 : count + 1);
        count++;

        System.out.print("Введите ФИО (пример ввода: Иванов Иван Иванович): ");
        String fullName;
        while (true) {
            fullName = scr.nextLine();
            Pattern namePattern = Pattern.compile("^[а-яА-ЯёЁa-zA-Z]+ [а-яА-ЯёЁa-zA-Z]+ ?[а-яА-ЯёЁa-zA-Z]+$");
            Matcher nameMatcher = namePattern.matcher(fullName);
            if (!nameMatcher.matches()) {
                System.out.print("Неверный формат имени! Введите снова (пример ввода: Иванов Иван Иванович): ");
            } else {
                break;
            }
        }

        System.out.print("Введите дату рождения в таком формате дд-мм-гггг: ");
        String dateBirthStr;
        while (true) {
            dateBirthStr = scr.nextLine();
            Pattern datePattern = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
            Matcher dateMatcher = datePattern.matcher(dateBirthStr);
            if (!dateMatcher.matches()) {
                System.out.print("Неверный формат даты! Введите в таком виде дд-мм-гггг: ");
            } else {
                break;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date dateBirth = formatter.parse(dateBirthStr);

        System.out.println("Выберите пол: \n[1] - мужской\n[2] - женский");
        String gender;
        while (true) {
            gender = scr.nextLine();
            if (gender.equals("1")) {
                gender = "мужской";
                break;
            } else if (gender.equals("2")) {
                gender = "женский";
                break;
            } else {
                System.out.print("Неверная команда! Нажмите [1] - мужской, либо [2] - женский: ");
            }
        }

        System.out.print("Введите номер телефона: ");
        String phone;
        while (true) {
            phone = scr.nextLine();
            Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                System.out.println("Неверный формат номера! Введите номер телефона в виде +7ХХХХХХХХХХ или 8ХХХХХХХХХХ");
            } else {
                phone = phone.replaceAll("(?m)^(\\+7|8)", "7");
                break;
            }
        }

        System.out.println("Выберите на какую должность нанимается сотрудник: \n[1] - Директор\n[2] - Старший менеджер\n" +
                "[3] - Начальник отдела кадров\n[4] - Главный бухгалтер\n[5] - Начальник склада\n[6] - Менеджер\n" +
                "[7] - Сотрудник по подбору\n[8] - Бухгалтер\n[9] - Уборщик-бригадир\n[10] - Водитель");
        String jobTitle = "";
        String department = "";
        while (jobTitle.isEmpty()) {
            int input = Integer.parseInt(scr.nextLine());
            switch (input) {
                case 1 -> {
                    jobTitle = "Директор";
                    department = "Исполнительный";
                    supervisors.put(jobTitle, fullName);
                }
                case 2 -> {
                    jobTitle = "Старший менеджер";
                    department = "Отдел по работе с клиентами";
                    supervisors.put(jobTitle, fullName);
                }
                case 3 -> {
                    jobTitle = "Начальник отдела кадров";
                    department = "Отдел кадров";
                    supervisors.put(jobTitle, fullName);
                }
                case 4 -> {
                    jobTitle = "Главный бухгалтер";
                    department = "Бухгалтерия";
                    supervisors.put(jobTitle, fullName);
                }
                case 5 -> {
                    jobTitle = "Начальник склада";
                    department = "Отдел работ";
                    supervisors.put(jobTitle, fullName);
                }
                case 6 -> {
                    jobTitle = "Менеджер";
                    department = "Отдел по работе с клиентами";
                }
                case 7 -> {
                    jobTitle = "Сотрудник по подбору";
                    department = "Отдел кадров";
                }
                case 8 -> {
                    jobTitle = "Бухгалтер";
                    department = "Бухгалтерия";
                }
                case 9 -> {
                    jobTitle = "Уборщик-бригадир";
                    department = "Отдел работ";
                }
                case 10 -> {
                    jobTitle = "Водитель";
                    department = "Отдел работ";
                }
                default -> System.out.println("Неверная команда!");
            }
        }

        String supervisor = "нет";
        if (jobTitle.equals("Директор")) {
            supervisor = "нет";
        } else if (jobTitle.equals("Старший менеджер") || jobTitle.equals("Начальник отдела кадров") ||
                jobTitle.equals("Главный бухгалтер") || jobTitle.equals("Начальник склада")) {
            for (Map.Entry<String, String> entry : supervisors.entrySet()) {
                if (entry.getKey().equals("Директор")) {
                    supervisor = entry.getValue();
                    break;
                }
            }
        } else {
            for (Map.Entry<String, String> entry : supervisors.entrySet()) {
                if (jobTitle.equals("Менеджер") && entry.getKey().equals("Старший менеджер")) {
                    supervisor = entry.getValue();
                } else if (jobTitle.equals("Сотрудник по подбору") && entry.getKey().equals("Начальник отдела кадров")) {
                    supervisor = entry.getValue();
                } else if (jobTitle.equals("Бухгалтер") && entry.getKey().equals("Главный бухгалтер")) {
                    supervisor = entry.getValue();
                } else if ((jobTitle.equals("Уборщик-бригадир") || jobTitle.equals("Водитель")) && entry.getKey().equals("Начальник склада")) {
                    supervisor = entry.getValue();
                }
            }
        }

        System.out.print("Введите дату приема на работу в таком формате дд-мм-гггг: ");
        String hireDateStr;
        while (true) {
            hireDateStr = scr.nextLine();
            Pattern datePattern = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
            Matcher dateMatcher = datePattern.matcher(hireDateStr);
            if (!dateMatcher.matches()) {
                System.out.print("Неверный формат даты! Введите в таком виде дд-мм-гггг: ");
            } else {
                break;
            }
        }
        Date hireDate = formatter.parse(hireDateStr);

        System.out.print("Введите зарплату: ");
        double salary = scr.nextDouble();

        employees.add(new Employee(id, fullName, dateBirth, gender, phone, jobTitle,
                department, supervisor, hireDate, salary));
        System.out.println("Новый сотрудник успешно добавлен!\n");
        updateInfoSupervisor();
    }

    public void fireEmployee() {
        System.out.println("---> Увольняем сотрудника <---");
        boolean run = true;

        while (run) {
            Scanner scr = new Scanner(System.in);
            System.out.println("[1] - Поиск по ФИО\n[2] - Поиск по должности\n[3] - Поиск по отделу\n[4] - Выход");
            int input = Integer.parseInt(scr.nextLine());

            switch (input) {
                case 1 -> {
                    while (true) {
                        System.out.print("Введите ФИО сотрудника (пример ввода Иванов Иван Иванович): ");
                        String name = scr.nextLine();
                        boolean exists = false;

                        for (Employee employee : employees) {
                            if (employee.getFullName().equals(name)) {
                                System.out.println(employee);
                                exists = true;
                            }
                        }
                        if (!exists) {
                            System.out.println("Сотрудник с данным ФИО не найден!");
                        } else {
                            System.out.print("Введите ID сотрудника из списка выше: ");
                            int id = Integer.parseInt(scr.nextLine());
                            for (Employee employee : employees) {
                                if (employee.getId().equals(id)) {
                                    System.out.println("Сотрудник " + employee.getFullName() + " успешно уволен!");
                                    employees.remove(employee);
                                    count--;
                                    break;
                                }
                            }
                            for (Employee employee : employees) {
                                if (employee.getId() >= id) {
                                    employee.setId(employee.getId() - 1);
                                }
                            }
                            break;
                        }
                    }
                }
                case 2 -> {
                    while (true) {
                        System.out.print("Введите должность сотрудника: ");
                        String jobTitle = scr.nextLine();
                        boolean exists = false;

                        for (Employee employee : employees) {
                            if (employee.getJobTitle().equals(jobTitle)) {
                                System.out.println(employee);
                                exists = true;
                            }
                        }
                        if (!exists) {
                            System.out.println("Сотрудники с данной должностью не найдены!");
                        } else {
                            System.out.print("Введите ID сотрудника из списка выше: ");
                            int id = Integer.parseInt(scr.nextLine());
                            for (Employee employee : employees) {
                                if (employee.getId().equals(id)) {
                                    System.out.println("Сотрудник " + employee.getFullName() + " успешно уволен!");
                                    employees.remove(employee);
                                    count--;
                                    break;
                                }
                            }
                            for (Employee employee : employees) {
                                if (employee.getId() >= id) {
                                    employee.setId(employee.getId() - 1);
                                }
                            }
                            break;
                        }
                    }
                }
                case 3 -> {
                    while (true) {
                        System.out.print("Введите название отдела: ");
                        String department = scr.nextLine();
                        boolean exists = false;

                        for (Employee employee : employees) {
                            if (employee.getDepartment().equals(department)) {
                                System.out.println(employee);
                                exists = true;
                            }
                        }
                        if (!exists) {
                            System.out.println("Отдел с данным названием не найден!");
                        } else {
                            System.out.print("Введите ID сотрудника из списка выше: ");
                            int id = Integer.parseInt(scr.nextLine());
                            for (Employee employee : employees) {
                                if (employee.getId().equals(id)) {
                                    System.out.println("Сотрудник " + employee.getFullName() + " успешно уволен!");
                                    employees.remove(employee);
                                    count--;
                                    break;
                                }
                            }
                            for (Employee employee : employees) {
                                if (employee.getId() >= id) {
                                    employee.setId(employee.getId() - 1);
                                }
                            }
                            break;
                        }
                    }
                }
                case 4 -> {
                    run = false;
                }
                default -> System.out.println("Неверная команда! Выберите одну из этих: ");
            }
        }
        updateInfoSupervisor();
    }

    public void editEmployee() {
        System.out.println("---> Редактируем информацию по сотруднику <---");
        boolean run = true;

        while (true) {
            Scanner scr = new Scanner(System.in);
            for (Employee employee : employees) {
                System.out.println(employee);
            }
            System.out.println("Напишите ID сотрудника которому нужно изменить данные: ");
            int id = Integer.parseInt(scr.nextLine());
            for (Employee employee : employees) {
                if (employee.getId().equals(id)) {
                    System.out.println(employee);
                    while (run) {
                        System.out.println("\nКакие данные хотите отредактировать: \n[1] - ФИО\n[2] - Номер телефона" +
                                "\n[3] - Должность\n[4] - Зарплата\n[5] - Выйти из режима редактирования");

                        int input = Integer.parseInt(scr.nextLine());
                        switch (input) {
                            case 1 -> {
                                System.out.print("Введите новое ФИО (пример ввода: Иванов Иван Иванович): ");
                                String newFullName = scr.nextLine();
                                System.out.println("Данные ФИО успешно изменены с " + employee.getFullName() + " на " + newFullName + "!");
                                employee.setFullName(newFullName);
                            }
                            case 2 -> {
                                System.out.print("Введите новый номер телефона: ");
                                String newPhone;
                                while (true) {
                                    newPhone = scr.nextLine();
                                    Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
                                    Matcher matcher = pattern.matcher(newPhone);
                                    if (!matcher.matches()) {
                                        System.out.println("Неверный формат номера! Введите номер телефона в виде +7ХХХХХХХХХХ или 8ХХХХХХХХХХ");
                                    } else {
                                        newPhone = newPhone.replaceAll("(?m)^(\\+7|8)", "7");
                                        System.out.println("Номер телефона успешно изменен с " + employee.getPhoneNumber() + " на " + newPhone + "!");
                                        employee.setPhoneNumber(newPhone);
                                        break;
                                    }
                                }
                            }
                            case 3 -> {
                                System.out.println("Выберите новую должность: \n[1] - Директор\n[2] - Старший менеджер\n" +
                                        "[3] - Начальник отдела кадров\n[4] - Главный бухгалтер\n[5] - Начальник склада\n[6] - Менеджер\n" +
                                        "[7] - Сотрудник по подбору\n[8] - Бухгалтер\n[9] - Уборщик-бригадир\n[10] - Водитель");
                                String newjobTitle = "";
                                String department = "";
                                while (newjobTitle.isEmpty()) {
                                    int tmp = Integer.parseInt(scr.nextLine());
                                    switch (tmp) {
                                        case 1 -> {
                                            newjobTitle = "Директор";
                                            department = "Исполнительный";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            supervisors.put(newjobTitle, employee.getFullName());
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 2 -> {
                                            newjobTitle = "Старший менеджер";
                                            department = "Отдел по работе с клиентами";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            supervisors.put(newjobTitle, employee.getFullName());
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 3 -> {
                                            newjobTitle = "Начальник отдела кадров";
                                            department = "Отдел кадров";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            supervisors.put(newjobTitle, employee.getFullName());
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 4 -> {
                                            newjobTitle = "Главный бухгалтер";
                                            department = "Бухгалтерия";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            supervisors.put(newjobTitle, employee.getFullName());
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 5 -> {
                                            newjobTitle = "Начальник склада";
                                            department = "Отдел работ";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            supervisors.put(newjobTitle, employee.getFullName());
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 6 -> {
                                            newjobTitle = "Менеджер";
                                            department = "Отдел по работе с клиентами";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 7 -> {
                                            newjobTitle = "Сотрудник по подбору";
                                            department = "Отдел кадров";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 8 -> {
                                            newjobTitle = "Бухгалтер";
                                            department = "Бухгалтерия";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 9 -> {
                                            newjobTitle = "Уборщик-бригадир";
                                            department = "Отдел работ";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        case 10 -> {
                                            newjobTitle = "Водитель";
                                            department = "Отдел работ";
                                            employee.setJobTitle(newjobTitle);
                                            employee.setDepartment(department);
                                            System.out.println("Должность успешно изменена!");
                                        }
                                        default -> System.out.println("Неверная команда!");
                                    }
                                }
                            }
                            case 4 -> {
                                System.out.print("Введите новую зарплату: ");
                                double newSalary = Double.parseDouble(scr.nextLine());
                                System.out.println("Зарплата успешно изменена с " + employee.getSalary() + " на " + newSalary + "!");
                                employee.setSalary(newSalary);
                            }
                            case 5 -> run = false;
                        }
                    }
                }
            }
            break;
        }
        updateInfoSupervisor();
    }

    public void searchEmployee() {
        System.out.println("---> Поиск сотрудника <---");

        boolean run = true;
        while (run) {
            Scanner scr = new Scanner(System.in);
            System.out.println("Выберите параметр поиска: \n[1] - По ФИО\n[2] - По должности\n[3] - По отделу\n[4] - По ФИО начальника\n" +
                    "[5] - Выйти из режима поиска");
            int input = Integer.parseInt(scr.nextLine());
            switch (input) {
                case 1 -> {
                    System.out.print("Введите ФИО сотрудника (пример ввода: Иванов Иван Иванович): ");
                    String name = scr.nextLine();
                    int exists = 0;
                    for (Employee employee : employees) {
                        if (employee.getFullName().equals(name)) {
                            System.out.println(employee);
                            exists++;
                        }
                    }
                    if (exists == 0) {
                        System.out.println("Сотрудник с данным ФИО не найден!");
                    }
                }
                case 2 -> {
                    System.out.print("Введите должность сотрудника: ");
                    String jobTitle = scr.nextLine();
                    int exists = 0;
                    for (Employee employee : employees) {
                        if (employee.getJobTitle().equals(jobTitle)) {
                            System.out.println(employee);
                            exists++;
                        }
                    }
                    if (exists == 0) {
                        System.out.println("Сотрудники с данной должностью не найдены!");
                    }
                }
                case 3 -> {
                    System.out.print("Введите название отдела: ");
                    String department = scr.nextLine();
                    int exists = 0;
                    for (Employee employee : employees) {
                        if (employee.getDepartment().equals(department)) {
                            System.out.println(employee);
                            exists++;
                        }
                    }
                    if (exists == 0) {
                        System.out.println("Сотрудники по данному отделу не найдены!");
                    }
                }
                case 4 -> {
                    System.out.print("Введите ФИО начальника: ");
                    String supervisor = scr.nextLine();
                    int exists = 0;
                    for (Employee employee : employees) {
                        if (employee.getSupervisor().equals(supervisor)) {
                            System.out.println(employee);
                            exists++;
                        }
                    }
                    if (exists == 0) {
                        System.out.println("Под данным начальником, сотрудники не найдены!");
                    }
                }
                case 5 -> run = false;
            }
        }
    }

    public void reporting() {
        System.out.println("---> Создание отчетов <---");

        boolean run = true;
        while (run) {
            Scanner scr = new Scanner(System.in);
            System.out.println("Выберите тип отчета: \n[1] - Структура организации\n[2] - Средняя зарплата по организации\n" +
                    "[3] - Средняя зарплата по отделам\n[4] - ТОП-10 самых дорогих сотрудников\n" +
                    "[5] - ТОП-10 самых преданных сотрудников\n[6] - Выйти из режима создания отчетов");
            int input = Integer.parseInt(scr.nextLine());

            switch (input) {
                case 1 -> {
                    System.out.println("---> Структура организации <---");
                    String organizationStructure = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/organizationStructure.txt";
                    try (FileWriter writer = new FileWriter(organizationStructure)) {
                        for (Employee employee : employees) {
                            System.out.println("ID: " + employee.getId());
                            writer.write("ID: " + employee.getId() + "\n");
                            System.out.println("ФИО: " + employee.getFullName());
                            writer.write("ФИО: " + employee.getFullName() + "\n");
                            System.out.println("Должность: " + employee.getJobTitle());
                            writer.write("Должность: " + employee.getJobTitle() + "\n");
                            System.out.println("Отдел: " + employee.getDepartment() + "\n");
                            writer.write("Отдел: " + employee.getDepartment() + "\n\n");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                case 2 -> {
                    System.out.println("---> Средняя зарплата по организации <---");
                    OptionalDouble averageSalary = employees.stream().mapToDouble(Employee::getSalary).average();
                    System.out.println("Средняя зарплата по организации составляет: " + averageSalary.getAsDouble() + " рублей");
                }
                case 3 -> {
                    System.out.println("---> Средняя зарплата по отделам <---");
                    String averageSalaryByDepartment = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/averageSalaryByDepartment.txt";
                    try (FileWriter writer = new FileWriter(averageSalaryByDepartment)) {
                        OptionalDouble averageSalary1 = employees.stream()
                                .filter(employee -> employee.getDepartment().equals("Исполнительный"))
                                .mapToDouble(Employee::getSalary)
                                .average();
                        OptionalDouble averageSalary2 = employees.stream()
                                .filter(employee -> employee.getDepartment().equals("Отдел по работе с клиентами"))
                                .mapToDouble(Employee::getSalary)
                                .average();
                        OptionalDouble averageSalary3 = employees.stream()
                                .filter(employee -> employee.getDepartment().equals("Отдел кадров"))
                                .mapToDouble(Employee::getSalary)
                                .average();
                        OptionalDouble averageSalary4 = employees.stream()
                                .filter(employee -> employee.getDepartment().equals("Бухгалтерия"))
                                .mapToDouble(Employee::getSalary)
                                .average();
                        OptionalDouble averageSalary5 = employees.stream()
                                .filter(employee -> employee.getDepartment().equals("Отдел работ"))
                                .mapToDouble(Employee::getSalary)
                                .average();
                        System.out.println("Средняя зарплата в исполнительном отделе: " + averageSalary1.getAsDouble() + " рублей");
                        writer.write("Средняя зарплата в исполнительном отделе: " + averageSalary1.getAsDouble() + " рублей\n");
                        System.out.println("Средняя зарплата в отделе по работе с клиентами: " + averageSalary2.getAsDouble() + " рублей");
                        writer.write("Средняя зарплата в отделе по работе с клиентами: " + averageSalary2.getAsDouble() + " рублей\n");
                        System.out.println("Средняя зарплата в отделе кадров: " + averageSalary3.getAsDouble() + " рублей");
                        writer.write("Средняя зарплата в отделе кадров: " + averageSalary3.getAsDouble() + " рублей\n");
                        System.out.println("Средняя зарплата в бухгалтерии: " + averageSalary4.getAsDouble() + " рублей");
                        writer.write("Средняя зарплата в бухгалтерии: " + averageSalary4.getAsDouble() + " рублей\n");
                        System.out.println("Средняя зарплата в отделе работ: " + averageSalary5.getAsDouble() + " рублей");
                        writer.write("Средняя зарплата в отделе работ: " + averageSalary5.getAsDouble() + " рублей\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                case 4 -> {
                    System.out.println("---> ТОП-10 самых дорогих сотрудников <---");
                    String topExpensive = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/topExpensive.txt";
                    try (FileWriter writer = new FileWriter(topExpensive)) {
                        Stream<Employee> top = employees.stream()
                                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                                .limit(10);
                        top.forEach(employee -> {
                            System.out.println(employee.getFullName() + " - " + employee.getSalary() + " рублей");
                            try {
                                writer.write(employee.getFullName() + " - " + employee.getSalary() + " рублей\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                case 5 -> {
                    System.out.println("---> ТОП-10 самых преданных сотрудников <---");
                    String topLoyal = "C:/Users/ssabi/Desktop/Exams/EmployeeSystemManagement/src/main/java/org/example/data/topLoyal.txt";
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    try (FileWriter writer = new FileWriter(topLoyal)) {
                        Stream<Employee> top = employees.stream()
                                .sorted(Comparator.comparing(Employee::getHireDate))
                                .limit(10);
                        top.forEach(employee -> {
                            System.out.println(employee.getFullName() + " - " + formatter.format(employee.getHireDate()));
                            try {
                                writer.write(employee.getFullName() + " - " + formatter.format(employee.getHireDate()) + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 6 -> run = false;
            }
        }
    }
}
