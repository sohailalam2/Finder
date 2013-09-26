package com.sohail.alam.keywordfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 6:00 PM
 */
public class SearchResultDumper {
    public static final SearchResultDumper DUMPER = new SearchResultDumper();
    private final File SEARCH_RESULT_FILE;
    private FileOutputStream searchResultOut;

    private SearchResultDumper() {
        SEARCH_RESULT_FILE = new File("Search_Result_"
                + new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss.SSS").format(new Date()) + ".txt");
        try {
            searchResultOut = new FileOutputStream(SEARCH_RESULT_FILE);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void dumpSearchResult(String result, boolean close) {
        try {
            searchResultOut.write(result.getBytes());
            searchResultOut.write("\r\n".getBytes());
            searchResultOut.flush();
            if (close) {
                searchResultOut.close();
                System.out.println("Search Result has been dumped successfully to => " + SEARCH_RESULT_FILE.getAbsoluteFile());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
