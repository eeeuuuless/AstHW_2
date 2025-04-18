package com.example;


import dao.UserDao;
import dao.UserDaoImpl;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.HibernateTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            logger.info("Запуск приложения");

            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createUser();
                        break;
                    case "2":
                        findUserById();
                        break;
                    case "3":
                        findAllUsers();
                        break;
                    case "4":
                        updateUser();
                        break;
                    case "5":
                        deleteUser();
                        break;
                    case "6":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте еще раз.");
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка приложения: " + e.getMessage(), e);
            System.err.println("Произошла ошибка: " + e.getMessage());
        } finally {
            HibernateTest.shutdown();
            scanner.close();
            logger.info("Приложение остановлено");
        }
    }

    private static void printMenu() {
        System.out.println("\nUser Service Menu:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по идентификатору");
        System.out.println("3. Найти всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("6. Выход");
        System.out.print("Введите свой выбор: ");
    }

    private static void createUser() {
        System.out.println("\nСоздать нового пользователя");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        User savedUser = userDao.save(user);
        System.out.println("Пользователь успешно создан: " + savedUser);
        logger.info("Пользователь создан: {}", savedUser);
    }

    private static void findUserById() {
        System.out.println("\nНайти пользователя по ID");
        System.out.print("Введите ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user != null) {
            System.out.println("Пользователь найден: " + user);
            logger.info("Нашел пользователя по id {}: {}", id, user);
        } else {
            System.out.println("Пользователь с id не найден: " + id);
            logger.warn("Пользователь с id не найден: {}", id);
        }
    }

    private static void findAllUsers() {
        System.out.println("\nВсе пользователи");
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены");
            logger.info("В базе данных не найдено ни одного пользователя");
        } else {
            users.forEach(System.out::println);
            logger.info("Найдено {} пользователей", users.size());
        }
    }

    private static void updateUser() {
        System.out.println("\nПользователь обновлен");
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь с ID не найден: " + id);
            logger.warn("Пользователь с id не найден для обновления: {}", id);
            return;
        }

        System.out.println("Текущие данные пользователя: " + user);
        System.out.print("Введите новое имя (оставьте пустым, чтобы сохранить текущее): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Введите новый email (оставьте пустым, чтобы сохранить текущий): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Введите новый возраст (оставьте пустым, чтобы сохранить текущий): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        User updatedUser = userDao.update(user);
        System.out.println("Пользователь успешно обновлен: " + updatedUser);
        logger.info("Обновленный пользователь: {}", updatedUser);
    }

    private static void deleteUser() {
        System.out.println("\nУдалить пользователя");
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь с ID не найден: " + id);
            logger.warn("Пользователь с id не найден для удаления: {}", id);
            return;
        }

        userDao.delete(id);
        System.out.println("Пользователь успешно удален");
        logger.info("Удален пользователь с id: {}", id);
    }
}