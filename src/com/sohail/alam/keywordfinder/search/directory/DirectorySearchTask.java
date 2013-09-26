/*
 * Copyright 2013 The Keyword Finder
 *
 *  The The Keyword Finder Project licenses this file to you under the Apache License, version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
