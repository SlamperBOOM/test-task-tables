package org.slamperboom.dataAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PagesAnalyzer {
    private final Properties previousPages;
    private final Properties currentPages;
    private List<String> deletedPages;
    private List<String> modifiedPages;
    private List<String> addedPages;

    public PagesAnalyzer(java.lang.String previousDataPath, java.lang.String currentDataPath) throws IOException{
        InputStream previousReader;
        InputStream currentReader;
        File previousFile = new File(previousDataPath);
        File currentFile = new File(currentDataPath);

        //сначала проверяем, что переданные файлы существуют
        if(!previousFile.exists() || !currentFile.exists()){
            throw new IOException("Не удалось найти файлы");
        }
        //затем проверяем, что мы можем их прочитать
        if(!(previousFile.isFile() && previousFile.canRead()) ||
        !(currentFile.isFile() && currentFile.canRead())){
            throw new IOException("Нет возможности прочитать указанные файлы");
        }

        //создаем потоки для чтения
        try{
            previousReader = new FileInputStream(previousFile);
        }catch (IOException e){
            e.printStackTrace();
            throw new IOException("Не удалось открыть файл с вчерашними страницами");
        }
        try{
            currentReader = new FileInputStream(currentFile);
        }catch (IOException e){
            //при ошибке создания потока чтения закрываем уже открытые
            previousReader.close();
            e.printStackTrace();
            throw new IOException("Не удалось открыть файл с сегодняшними страницами");
        }

        //properies позволяет удобно считать и использовать данные из файлов
        //и ключ и значение в properties является String поэтому этот класс подходит под эту задачу
        previousPages = new Properties();
        currentPages = new Properties();
        try{
            previousPages.load(previousReader);
            currentPages.load(currentReader);
        }catch (IOException e){
            previousReader.close();
            currentReader.close();
            throw new IOException("Не удалось прочитать файлы");
        }
        previousReader.close();
        currentReader.close();
    }

    public void analyzePages(){
        deletedPages = new ArrayList<>();
        modifiedPages = new ArrayList<>();
        addedPages = new ArrayList<>();
        //сначала смотрим была ли удалена страница
        //для этого смотрим есть ли страница из вчерашнего списка в сегодняшнем
        //затем, если она не была удалена, сравниваем коды страниц
        for(String key : previousPages.keySet().toArray(new String[0])){
            if(currentPages.getProperty(key) == null){
                deletedPages.add(key);
            } else if (!previousPages.getProperty(key).equals(currentPages.getProperty(key))) {
                modifiedPages.add(key);
            }
        }
        //смотрим была ли страница добавлена
        //по аналогии с поиском удаленных страниц
        for(String key : currentPages.keySet().toArray(new String[0])){
            if(previousPages.getProperty(key) == null){
                addedPages.add(key);
            }
        }
    }

    public List<String> getDeletedPages() {
        return deletedPages;
    }

    public List<String> getModifiedPages() {
        return modifiedPages;
    }

    public List<String> getAddedPages() {
        return addedPages;
    }
}
