package com.sohail.alam.keywordfinder;

import com.sohail.alam.keywordfinder.search.directory.DirectorySearch;
import com.sohail.alam.keywordfinder.search.file.FileSearch;

import java.io.IOException;

import static com.sohail.alam.keywordfinder.PropertiesLoader.PROP;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 12:53 PM
 */
public class KeywordFinderBootstrap {

    private static String keyword;
    private static String path;

    public static void main(String[] args) throws IOException {

        int argsLength = args.length;

        // If no command line arguments were provided then load from properties file
        if (argsLength == 0) {
            PROP.initialize();
            start();
        } else if (args.length < 2) {
            showUsage();
            System.exit(-1);
        } else {
            PROP.loadKeyword(args[0]);
            PROP.loadPath(args[1]);
            start();
        }
    }

    private static void start() {
        if (PROP.isDirectory) {
            try {
                DirectorySearch.DIR_SEARCH.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                FileSearch.FILE_SEARCH.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void showUsage() {
        System.err.println("You can configure the system.properties file");
        System.err.println("or pass command line parameters as follows");
        System.err.println("Usage: KeywordFinderBootstrap \"<some keyword>\" \"<path to search>\" ");
    }
}
