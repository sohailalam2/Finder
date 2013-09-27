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

package com.sohail.alam.finder.search.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sohail.alam.finder.PropertiesLoader.PROP;
import static com.sohail.alam.finder.SearchResultDumper.DUMPER;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 27/9/13
 * Time: 9:12 PM
 */
public class FilePatternSearch {
    public static final FilePatternSearch FILE_PATTERN_SEARCH = new FilePatternSearch();

    private FilePatternSearch() {
    }

    public void start() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PROP.PATH_TO_SEARCH));
            Pattern pattern = Pattern.compile(PROP.WHAT_TO_SEARCH, PROP.PATTERN_MATCH_FLAG);
            Matcher patternMatcher;
            String line;
            long count = 0;
            boolean found = false;
            String foundMsg = "\nPATTERN FOUND IN FILE";
            String notFoundMsg = "\nPATTERN WAS NOT FOUND IN FILE";

            while ((line = reader.readLine()) != null) {
                patternMatcher = pattern.matcher(line);
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
                    DUMPER.dumpSearchResult(foundMsg, true);
                }
            } else {
                System.out.println(notFoundMsg);
                DUMPER.dumpSearchResult(notFoundMsg, true);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
