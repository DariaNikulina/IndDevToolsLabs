package org.example.lab3;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Необходимо передать путь к директории с файлами");
            return;
        }
        String dirPath = args[0];
        WordCountService.getInstance().processDirectory(dirPath);
        System.out.println("Запись данных в базу успешно завершена");
    }
}
