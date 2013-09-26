package com.sohail.alam.keywordfinder.search.directory;

import java.io.*;

import static com.sohail.alam.keywordfinder.PropertiesLoader.PROP;
import static com.sohail.alam.keywordfinder.SearchResultDumper.DUMPER;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 3:24 PM
 */
public class DirectorySearchTask implements Runnable {
    private File file;

    public DirectorySearchTask(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        BufferedReader reader;
        String currentLine;
        String msgWithOutStats = "KEYWORD FOUND IN FILE: " + file.getAbsoluteFile();
        try {
            reader = new BufferedReader(new FileReader(file));
            long count = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(PROP.KEYWORD_TO_SEARCH)) {
                    if (PROP.enableStatistics) {
                        count++;
                    } else {
                        System.out.println(msgWithOutStats);
                        DUMPER.dumpSearchResult(msgWithOutStats, false);
                        break;
                    }
                }
            }
            // Print the statistics
            if (PROP.enableStatistics) {
                String countMsg = String.format("%-15s", "COUNTS: " + count);
                System.out.println(countMsg + msgWithOutStats);
                DUMPER.dumpSearchResult(countMsg + msgWithOutStats, false);
            }

            // Shut down if all tasks were completed
            if (DirectorySearch.DIR_SEARCH.FILE_COUNTER.decrementAndGet() == 0) {
                String completedMsg = "Directory Search Task Completed Successfully!";
                System.out.println(completedMsg);
                DUMPER.dumpSearchResult(completedMsg, true);
                DirectorySearch.DIR_SEARCH.TASK_COUNTER_SERVICE.shutdown();
                DirectorySearch.DIR_SEARCH.SERVICE.shutdown();
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
