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

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.sohail.alam.finder.PropertiesLoader.PROP;
import static com.sohail.alam.finder.SearchResultDumper.DUMPER;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 26/9/13
 * Time: 1:51 PM
 */
public class DirectorySearch {
    public static final DirectorySearch DIR_SEARCH = new DirectorySearch();
    protected final AtomicLong FILE_COUNTER;
    protected final ExecutorService SERVICE;
    protected final ScheduledExecutorService TASK_COUNTER_SERVICE;

    private DirectorySearch() {
        FILE_COUNTER = new AtomicLong(0);
        SERVICE = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
        TASK_COUNTER_SERVICE = new ScheduledThreadPoolExecutor(1);

    }

    private void startTaskCounterDisplay() {
        TASK_COUNTER_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("\nFiles Remaining: " + DirectorySearch.DIR_SEARCH.FILE_COUNTER.get());
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void addTaskForPatternSearch(File[] allFiles) {
        for (File file : allFiles) {
            if (file.isFile()) {
                DirectorySearch.DIR_SEARCH.FILE_COUNTER.incrementAndGet();
                SERVICE.execute(new DirectoryPatternSearchTask(file));
            } else {
                if (PROP.ENABLE_DEEP_SEARCH) {
                    addTaskForPatternSearch(file.listFiles());
                }
            }
        }
    }

    private void addTaskForNormalSearch(File[] allFiles) {
        for (File file : allFiles) {
            if (file.isFile()) {
                DirectorySearch.DIR_SEARCH.FILE_COUNTER.incrementAndGet();
                SERVICE.execute(new DirectorySearchTask(file));
            } else {
                if (PROP.ENABLE_DEEP_SEARCH) {
                    addTaskForNormalSearch(file.listFiles());
                }
            }
        }
    }

    public void start() {
        File[] allFiles = PROP.PATH_TO_SEARCH.listFiles();
        if (allFiles != null) {
            if (PROP.ENABLE_PATTERN_SEARCH) {
                addTaskForPatternSearch(allFiles);
            } else {
                addTaskForNormalSearch(allFiles);
            }
            String msg = "\nTotal Number of files to search => " + DirectorySearch.DIR_SEARCH.FILE_COUNTER.get();
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, false);
            startTaskCounterDisplay();
        } else {
            String msg = "\nThere are no files in this directory! Exiting Search";
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, true);
        }
    }
}
