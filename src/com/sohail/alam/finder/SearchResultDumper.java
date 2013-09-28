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

package com.sohail.alam.finder;

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
        // Create the Search_Results directory if not exists
        File resultsDir = new File("./Search_Result");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }
        String currentDate = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss.SSS").format(new Date());
        SEARCH_RESULT_FILE
                = new File(resultsDir.getAbsoluteFile() + "/" + currentDate + ".txt");
        try {
            searchResultOut = new FileOutputStream(SEARCH_RESULT_FILE);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void dumpSearchResult(String result, boolean close) {
        try {
            searchResultOut.write((result + "\r\n").getBytes());
            searchResultOut.flush();
            if (close) {
                searchResultOut.close();
                System.out.println("\nSearch Result has been dumped successfully to => " + SEARCH_RESULT_FILE.getAbsoluteFile());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
