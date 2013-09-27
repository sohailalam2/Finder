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

import com.sohail.alam.finder.search.directory.DirectorySearch;
import com.sohail.alam.finder.search.file.FilePatternSearch;
import com.sohail.alam.finder.search.file.FileSearch;

import java.io.IOException;

import static com.sohail.alam.finder.PropertiesLoader.PROP;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 12:53 PM
 */
public class Finder {

    private static String keyword;
    private static String path;

    public static void main(String[] args) throws IOException {

        int argsLength = args.length;

        // If no command line arguments were provided then load from properties file
        if (argsLength > 0) {
            processCliOptions(args);
        } else {
            PROP.initialize();
        }

        // Start the Search
        start();
    }

    private static void start() {
        if (PROP.IS_DIRECTORY) {
            try {
                DirectorySearch.DIR_SEARCH.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                if (PROP.ENABLE_PATTERN_SEARCH) {
                    FilePatternSearch.FILE_PATTERN_SEARCH.start();
                } else {
                    FileSearch.FILE_SEARCH.start();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void processCliOptions(String[] args) {
        String arg;
        String value;
        // java -jar finder.jar -s "Hello world" -p "./test_dir" -pat true
        try {
            for (int i = 0; i < args.length; i = i + 2) {
                arg = (args[i].toUpperCase()).replaceAll("-", "");
                CliOptions options = CliOptions.valueOf(arg);
                if (options.equals(CliOptions.HELP)) {
                    showHelp();
                    System.exit(-1);
                    return;
                }
                if ((value = args[i + 1]) != null) {
                    switch (options) {
                        case HELP:
                            showHelp();
                            System.exit(-1);
                            return;
                        case S: // WHAT_TO_SEARCH
                            PROP.loadWhatToSearch(value);
                            break;
                        case P: // PATH_TO_SEARCH
                            PROP.loadPath(value);
                            break;
                        case D: // ENABLE_DEEP_SEARCH
                            PROP.loadEnableDeepSearch(value);
                            break;
                        case STATS: // ENABLE_STATISTICS
                            PROP.loadEnableStatistics(value);
                            break;
                        case PAT: // ENABLE_PATTERN_SEARCH
                            PROP.loadEnablePatternSearch(value);
                            break;
                        case FLAG: // PATTERN_MATCH_FLAG
                            PROP.loadPatternSearchFlag(value);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showHelp();
        }
    }

    private static void createHelp(StringBuilder builder, String arg, String value, String description) {
        builder.append("\n")
                .append(String.format("%-10s", arg))
                .append(String.format("%-20s", value))
                .append(String.format("%-50s", description));
    }

    public static void showHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n")
                .append("You can configure the system.properties file or pass command line parameters as follows").append("\n\n");

        createHelp(builder, "-s", "search", "Anything that you wish to search for");
        createHelp(builder, "-p", "path", "The path where you want to search, can be a directory or a file");
        createHelp(builder, "-d", "true/false", "Enabling deep search will search all nested directories recursively");
        createHelp(builder, "-stats", "true/false", "Enabling Statistics will keep the search on beyond first match and hence calculate the total occurrences");
        createHelp(builder, "-pat", "true/false", "Enable Regular Expression Search, the -s parameter can hold any valid RegEx");
        createHelp(builder, "-flag", "integer", "Additional RegEx options: 1 => UNIX_LINES, 2 => CASE_INSENSITIVE, 4 => COMMENTS, 8 => MULTILINE");
        createHelp(builder, "-flag", "integer", "Additional RegEx options: 16 => LITERAL, 32 => DOTALL, 64 => UNICODE_CASE and 128 => CANON_EQ");

        System.err.println(builder.toString());
    }

    private static enum CliOptions {
        HELP, // help
        S, // WHAT_TO_SEARCH
        P, // PATH_TO_SEARCH
        D, // ENABLE_DEEP_SEARCH
        STATS, // ENABLE_STATISTICS
        PAT, // ENABLE_PATTERN_SEARCH
        FLAG; // PATTERN_MATCH_FLAG

        private CliOptions() {
        }
    }
}
