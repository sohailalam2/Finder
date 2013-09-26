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

package com.sohail.alam.keywordfinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 1:38 PM
 */
public class PropertiesLoader {
    public static final PropertiesLoader PROP = new PropertiesLoader();
    public String KEYWORD_TO_SEARCH;
    public File PATH_TO_SEARCH;
    public boolean isDirectory;
    public boolean enableDeepSearch;
    public boolean enableStatistics;

    private PropertiesLoader() {

    }

    public void initialize() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("configurations/system.properties")));
            // Load the keyword
            loadKeyword(properties.getProperty("KEYWORD_TO_SEARCH").trim());
            // Load the path
            loadPath(properties.getProperty("PATH_TO_SEARCH").trim());
            // Others
            enableDeepSearch = Boolean.parseBoolean(properties.getProperty("ENABLE_DEEP_SEARCH").trim());
            enableStatistics = Boolean.parseBoolean(properties.getProperty("ENABLE_STATISTICS").trim());
        } catch (IOException e) {
            System.err.println("Failed to load properties...");
            System.exit(-1);
        }
    }

    public void loadKeyword(String keyword) {
        if (!(keyword == null) && !(keyword.equals(""))) {
            KEYWORD_TO_SEARCH = keyword;
        } else {
            System.err.println("THE KEYWORD MUST NOT BE EMPTY");
            KeywordFinder.showUsage();
            System.exit(-1);
        }
    }

    public void loadPath(String path) {
        if (!(path == null) && !(path.equals(""))) {
            PATH_TO_SEARCH = new File(path);
            if (PATH_TO_SEARCH.exists()) {
                isDirectory = PATH_TO_SEARCH.isDirectory();
            } else {
                System.err.println("The Path Does NOT Exists: " + PATH_TO_SEARCH.getAbsoluteFile());
            }
        } else {
            System.err.println("THE PATH MUST NOT BE EMPTY");
            KeywordFinder.showUsage();
            System.exit(-1);
        }
    }
}
