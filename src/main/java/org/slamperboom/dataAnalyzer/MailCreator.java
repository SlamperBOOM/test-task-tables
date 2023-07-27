package org.slamperboom.dataAnalyzer;

import java.io.*;
import java.util.List;

public class MailCreator {
    private final List<String> deletedPages;
    private final List<String> modifiedPages;
    private final List<String> addedPages;

    public MailCreator(List<String> deletedPages, List<String> modifiedPages, List<String> addedPages) {
        this.deletedPages = deletedPages;
        this.modifiedPages = modifiedPages;
        this.addedPages = addedPages;
    }

    public void createMail() throws IOException {
        File outputFile = new File("generatedMail.txt");
        if (!outputFile.exists() && !outputFile.createNewFile()){
            throw new IOException("Не удалось создать файл");
        }
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))){
            writer.write("Здравствуйте, дорогая и.о. секретаря");
            writer.newLine();
            writer.newLine();
            writer.write("За последние сутки во вверенных Вам сайтах произошли следующие изменения:");
            writer.newLine();
            writer.newLine();

            writer.write("Изчезли следующие страницы:");
            writer.newLine();
            for(String url : deletedPages){
                writer.write("\t"+url);
                writer.newLine();
            }
            writer.newLine();
            writer.write("Появились следующие страницы:");
            writer.newLine();
            for(String url : addedPages){
                writer.write("\t"+url);
                writer.newLine();
            }
            writer.newLine();
            writer.write("Изменились следующие страницы:");
            writer.newLine();
            for(String url : modifiedPages){
                writer.write("\t"+url);
                writer.newLine();
            }
            writer.newLine();

            writer.write("С уважением,");
            writer.newLine();
            writer.write("автоматизированная система мониторинга.");
        }catch (IOException e){
            throw new IOException("Возникла ошибка при записи письма. "+e.getMessage());
        }
        System.out.println("Письмо создано в файле \"generatedMail.txt\"");
    }
}
