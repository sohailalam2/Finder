/*
 * Copyright 2013 The Finder
 *
 *  The The Finder Project licenses this file to you under the Apache License, version 2.0 (the "License");
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

package com.sohail.alam.finder.search.directory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sohail.alam.finder.PropertiesLoader.PROP;
import static com.sohail.alam.finder.SearchResultDumper.DUMPER;
import static com.sohail.alam.finder.search.directory.DirectorySearch.DIR_SEARCH;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 27/9/13
 * Time: 9:50 PM
 */
public class DirectoryPatternSearchTask implements Runnable {
    private File file;

    public DirectoryPatternSearchTask(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        Pattern pattern = Pattern.compile(PROP.WHAT_TO_SEARCH, PROP.PATTERN_MATCH_FLAG);
        Matcher patternMatcher;
        BufferedReader reader;
        String currentLine;
        long count = 0;
        boolean found = false;
        String foundMsg = "PATTERN FOUND IN FILE: " + file.getAbsoluteFile() + "\n";
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((currentLine = reader.readLine()) != null) {
                patternMatcher = pattern.matcher(currentLine);
                if (patternMatcher.find()) {
                    found = true;
                    if (PROP.ENABLE_STATISTICS) {
                        count++;
                    } else {
                        break;
                    }
                }
            }
            // Print the statistics
            if (found) {
                if (PROP.ENABLE_STATISTICS) {
                    String countMsg = String.format("%-15s", "COUNTS: " + count);
                    System.out.println(countMsg + foundMsg);
                    DUMPER.dumpSearchResult(countMsg + foundMsg, false);
                } else {
                    System.out.println(foundMsg);
                    DUMPER.dumpSearchResult(foundMsg, false);
                }
            }

            // Shut down if all tasks were completed
            if (DIR_SEARCH.FILES_COMPLETED.incrementAndGet() == DIR_SEARCH.FILES_FOUND_TO_SEARCH.get()) {
                String completedMsg = "\n\nDirectory Search Task Completed Successfully!";
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
