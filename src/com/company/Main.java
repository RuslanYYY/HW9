package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static final String INPUT_ZIP_FILE = "C:\\Program Files\\Java\\jdk1.8.0_92\\src.zip";
    private static final String OUTPUT_FOLDER = "D:\\unzipJava";
    private static final String EXTENSION = ".java";
    private final static String ANNOTATION_NAME = "@FunctionalInterface";
    private static final File FILES_FOLDER = new File(OUTPUT_FOLDER);
    private static final int BUFFER_SIZE = 1024;
    private static List<File> listFiles = new ArrayList<>();

    public static void main(String[] args) {

        unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
        searchJavaExtension(FILES_FOLDER);
        System.out.println("Amount of java files of the jdk1.8.0_92: " + listFiles.size() + " files.");
        System.out.println("Amount of java files with annotation @FunctionalInterface:");
        searchJavaAnnotation(listFiles, ANNOTATION_NAME);
    }

    private static void unZipIt(String zipFile, String outputFolder) {

        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void searchJavaExtension(final File file) {
        for (final File tmpResult : file.listFiles()) {
            if (tmpResult.isDirectory()) {
                searchJavaExtension(tmpResult);
            } else {
                if (tmpResult.toString().endsWith(EXTENSION)) {
                    listFiles.add(tmpResult);
                }
            }
        }
    }

    private static void searchJavaAnnotation(List<File> files, String annotation) {
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(annotation)) {
                        System.out.println(file);
                        continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}