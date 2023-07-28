package org.slamperboom;

import org.slamperboom.dataAnalyzer.MailCreator;
import org.slamperboom.dataAnalyzer.PagesAnalyzer;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner reader = new Scanner(System.in);

        //читаем пути к файлам, в которых лежит информация о страницах
        System.out.print("Введите путь к файлу со вчерашними страницами: ");
        String previousPath = reader.nextLine();
        System.out.print("Введите путь к файлу с сегодняшними страницами: ");
        String currentPath = reader.nextLine();

        PagesAnalyzer pagesAnalyzer;
        try {
            //инициируем класс-анализатор, передавая ему пути к файлам со страницами
            pagesAnalyzer = new PagesAnalyzer(previousPath, currentPath);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return;
        }
        //анализируем страницы
        pagesAnalyzer.analyzePages();
        MailCreator mailCreator = new MailCreator(
                pagesAnalyzer.getDeletedPages(),
                pagesAnalyzer.getModifiedPages(),
                pagesAnalyzer.getAddedPages());
        try {
            mailCreator.createMail();
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
