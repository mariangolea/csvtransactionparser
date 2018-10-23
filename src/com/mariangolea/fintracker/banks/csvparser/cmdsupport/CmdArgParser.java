package com.mariangolea.fintracker.banks.csvparser.cmdsupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdArgParser {

    public static final String INPUT_FOLDER_ARG = "-folder";
    public static final String FOLDER_NAME_SEPARATOR = "=";
    public static final String FOLDER_PATH_ESCAPE = "\"";
    public static final String SUPPORTED_FILE_EXTENSION = ".csv";

    public List<File> getCSVFiles(final String[] args) {
        if (args == null || args.length < 1) {
            printError();
            return null;
        }

        final List<File> folderFiles = getFolderFiles(args);
        if (folderFiles == null) {
            return null;
        }

        final List<File> csvFiles = new ArrayList<>();

        folderFiles.forEach(folderFile -> {
            File[] csvsPerFolder = folderFile.listFiles((File dir, String name) -> {
                return name.endsWith(SUPPORTED_FILE_EXTENSION);
            });
            csvFiles.addAll(Arrays.asList(csvsPerFolder));

        });

        return csvFiles;
    }

    private void printError() {
        System.out.println("Please input the path to a folder which contains CSV reports generated by supported banks (only ING for now).");
        System.out.println("Absolute folder path needs to be specified: -folder=\"pathOnYourOperatingSystem\" ");
        System.out.println("-inputFolder can be used multiple times to designate more folders: -folder=\"sss\" -folder=\"fff\"");
    }

    public List<File> getFolderFiles(String[] args) {
        final List<File> folderFiles = new ArrayList<>();
        for (String arg : args) {
            String[] argParams = arg.split(FOLDER_NAME_SEPARATOR);
            if (argParams.length != 2 || !INPUT_FOLDER_ARG.equals(argParams[0])) {
                printError();
                return null;
            }

            String folderPath = argParams[1].replaceAll("\"", "");
            File folderFile = new File(folderPath);
            if (!folderFile.exists() || !folderFile.isDirectory()) {
                printError();
                return null;
            } else {
                folderFiles.add(folderFile);
            }
        }

        return folderFiles;
    }
}
