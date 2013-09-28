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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import static com.sohail.alam.finder.SearchResultDumper.DUMPER;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 1:38 PM
 */
public class PropertiesLoader {
    public static final PropertiesLoader PROP = new PropertiesLoader();
    public String WHAT_TO_SEARCH;
    public File PATH_TO_SEARCH;
    public boolean IS_DIRECTORY;
    public boolean ENABLE_DEEP_SEARCH;
    public boolean ENABLE_STATISTICS;
    public int STATISTICS_UPDATE_PERIOD; // TODO: add CLI support
    public boolean ENABLE_PATTERN_SEARCH;
    public int PATTERN_MATCH_FLAG = 1;
    // TODO: Add filtering support
    public boolean ENABLE_FILE_FILTER; // TODO: add CLI support
    public ArrayList<String> FILTER_FILE_TYPE; // TODO: add CLI support

    private PropertiesLoader() {

    }

    public void initialize() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("configurations/system.properties")));
            // Load the keyword
            loadWhatToSearch(properties.getProperty("WHAT_TO_SEARCH").trim());
            // Load the path
            loadPath(properties.getProperty("PATH_TO_SEARCH").trim());
            // Others
            loadEnableDeepSearch(properties.getProperty("ENABLE_DEEP_SEARCH", "true").trim());
            loadEnableStatistics(properties.getProperty("ENABLE_STATISTICS", "false").trim());
            loadStatisticsUpdatePeriod(properties.getProperty("STATISTICS_UPDATE_PERIOD", "10").trim());
            loadEnablePatternSearch(properties.getProperty("ENABLE_PATTERN_SEARCH", "false").trim());
            loadPatternSearchFlag(properties.getProperty("PATTERN_MATCH_FLAG", "1").trim());
            loadEnableFileFilter(properties.getProperty("ENABLE_FILE_FILTER", "false").trim());
            loadFilterFileType(properties.getProperty("FILTER_FILE_TYPE", "txt").trim());

        } catch (Exception e) {
            System.err.println("Failed to load properties...");
            System.exit(-1);
        }
    }

    public void loadWhatToSearch(String whatToSearch) {
        if (!(whatToSearch == null) && !(whatToSearch.equals(""))) {
            WHAT_TO_SEARCH = whatToSearch;
            final String msg = "\nSearching For: " + WHAT_TO_SEARCH;
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, false);
        } else {
            System.err.println("THE KEYWORD MUST NOT BE EMPTY");
            Finder.showHelp();
            System.exit(-1);
        }
    }

    public void loadPath(String path) {
        if (!(path == null) && !(path.equals(""))) {
            PATH_TO_SEARCH = new File(path);
            if (PATH_TO_SEARCH.exists()) {
                IS_DIRECTORY = PATH_TO_SEARCH.isDirectory();
                final String pathMsg = "\nSearching in Path: " + PATH_TO_SEARCH.getAbsoluteFile();
                final String dirMsg = "\nIs it a directory path: " + IS_DIRECTORY;
                System.out.println(pathMsg);
                System.out.println(dirMsg);
                DUMPER.dumpSearchResult(pathMsg, false);
                DUMPER.dumpSearchResult(dirMsg, false);
            } else {
                final String pathNotExistsMsg = "The Path Does NOT Exists: " + PATH_TO_SEARCH.getAbsoluteFile();
                System.err.println(pathNotExistsMsg);
                DUMPER.dumpSearchResult(pathNotExistsMsg, false);
            }
        } else {
            System.err.println("THE PATH MUST NOT BE EMPTY");
            Finder.showHelp();
            System.exit(-1);
        }
    }

    public void loadEnableDeepSearch(String enableDeepSearch) {
        ENABLE_DEEP_SEARCH = Boolean.parseBoolean(enableDeepSearch);
        final String msg = "\nIs Deep Search Enabled: " + ENABLE_DEEP_SEARCH;
        System.out.println(msg);
        DUMPER.dumpSearchResult(msg, false);
    }

    public void loadEnableStatistics(String enableStatistics) {
        ENABLE_STATISTICS = Boolean.parseBoolean(enableStatistics);
        final String msg = "\nIs Statistics Enabled: " + ENABLE_STATISTICS;
        System.out.println(msg);
        DUMPER.dumpSearchResult(msg, false);
    }

    public void loadStatisticsUpdatePeriod(String updatePeriod) {
        if (ENABLE_STATISTICS) {
            try {
                STATISTICS_UPDATE_PERIOD = Integer.parseInt(updatePeriod);
            } catch (NumberFormatException e) {
                System.err.println("The Statistics Update Period MUST be an integer value");
            }
        }
    }

    public void loadEnablePatternSearch(String enablePatternSearch) {
        ENABLE_PATTERN_SEARCH = Boolean.parseBoolean(enablePatternSearch);
        final String msg = "\nIs Pattern Search Enabled: " + ENABLE_PATTERN_SEARCH;
        System.out.println(msg);
        DUMPER.dumpSearchResult(msg, false);
    }

    public void loadPatternSearchFlag(String flag) {
        if (ENABLE_PATTERN_SEARCH) {
            try {
                if (flag != null && !flag.isEmpty()) {
                    PATTERN_MATCH_FLAG = Integer.parseInt(flag);
                    switch (PATTERN_MATCH_FLAG) {
                        case 1: // UNIX_LINES
                        case 2: // CASE_INSENSITIVE
                        case 4: // COMMENTS
                        case 8: // MULTILINE
                        case 16: // LITERAL
                        case 32: // DOTALL
                        case 64: // UNICODE_CASE
                        case 128: // CANON_EQ
                            final String msg = "\nPattern Flag Loaded: " + PATTERN_MATCH_FLAG;
                            System.out.println(msg);
                            DUMPER.dumpSearchResult(msg, false);
                            break;
                        default:
                            System.err.println("PATTERN_MATCH_FLAG must have an integer value among one of the given values");
                            System.exit(-1);
                            break;
                    }
                } else {
                    PATTERN_MATCH_FLAG = 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("PATTERN_MATCH_FLAG must have an integer value among one of the given values");
                System.exit(-1);
            }
        }
    }

    public void loadEnableFileFilter(String enableFileFilter) {
        ENABLE_FILE_FILTER = Boolean.parseBoolean(enableFileFilter);
        final String msg = "\nIs File Filter Enabled: " + ENABLE_FILE_FILTER;
        System.out.println(msg);
        DUMPER.dumpSearchResult(msg, false);
    }

    public void loadFilterFileType(String filterFileType) {
        if (ENABLE_FILE_FILTER) {
            FILTER_FILE_TYPE = new ArrayList<String>();
            StringTokenizer tokenizer = new StringTokenizer(filterFileType, ",");
            while (tokenizer.hasMoreElements()) {
                FILTER_FILE_TYPE.add(tokenizer.nextToken().trim());
            }
            final String msg = "\nFiltering File Types: " + filterFileType;
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, false);
        }
    }
}