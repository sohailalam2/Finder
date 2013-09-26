package com.sohail.alam.keywordfinder.search.file;

import com.sohail.alam.keywordfinder.SearchResultDumper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.sohail.alam.keywordfinder.PropertiesLoader.PROP;
import static com.sohail.alam.keywordfinder.SearchResultDumper.DUMPER;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 5:58 PM
 */
public class FileSearch {
    public static final FileSearch FILE_SEARCH = new FileSearch();

    private FileSearch() {
    }

    public void start() {
        String starting = "Starting File Search =>" + PROP.PATH_TO_SEARCH.getAbsoluteFile();
        String lookingFor = "Looking for keyword =>" + PROP.KEYWORD_TO_SEARCH;
        String statistics = PROP.enableStatistics ? "Statistics is Enabled" : "Statistics is Disabled";
        System.out.println(starting);
        System.out.println(lookingFor);
        System.out.println(statistics);
        DUMPER.dumpSearchResult(starting, false);
        DUMPER.dumpSearchResult(lookingFor, false);
        DUMPER.dumpSearchResult(statistics, false);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PROP.PATH_TO_SEARCH));
            String line;
            long count = 0;
            String msg = "KEYWORD FOUND IN FILE";

            while ((line = reader.readLine()) != null) {
                if (line.contains(PROP.KEYWORD_TO_SEARCH)) {
                    if (PROP.enableStatistics) {
                        count++;
                    } else {
                        SearchResultDumper.DUMPER.dumpSearchResult(msg, true);
                        break;
                    }
                }
            }

            // Print the statistics
            if (PROP.enableStatistics) {
                String countMsg = String.format("%-15s", "COUNTS: " + count);
                System.out.println(countMsg + msg);
                DUMPER.dumpSearchResult(countMsg + msg, false);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
