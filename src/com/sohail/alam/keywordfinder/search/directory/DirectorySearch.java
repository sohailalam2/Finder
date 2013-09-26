package com.sohail.alam.keywordfinder.search.directory;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.sohail.alam.keywordfinder.PropertiesLoader.PROP;
import static com.sohail.alam.keywordfinder.SearchResultDumper.DUMPER;

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
                System.out.println("Files Remaining: " + DirectorySearch.DIR_SEARCH.FILE_COUNTER.get());
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void addTask(File[] allFiles) {
        for (File file : allFiles) {
            if (file.isFile()) {
                DirectorySearch.DIR_SEARCH.FILE_COUNTER.incrementAndGet();
                SERVICE.execute(new DirectorySearchTask(file));
            } else {
                if (PROP.enableDeepSearch) {
                    addTask(file.listFiles());
                }
            }
        }
    }

    public void start() {
        File[] allFiles = PROP.PATH_TO_SEARCH.listFiles();

        String starting = "Starting Directory Search =>" + PROP.PATH_TO_SEARCH.getAbsoluteFile();
        String lookingFor = "Looking for keyword =>" + PROP.KEYWORD_TO_SEARCH;
        String deepSearch = PROP.enableDeepSearch ? "Deep Search is Enabled" : "Deep Search is Disabled";
        String statistics = PROP.enableStatistics ? "Statistics is Enabled" : "Statistics is Disabled";
        System.out.println(starting);
        System.out.println(lookingFor);
        System.out.println(deepSearch);
        System.out.println(statistics);
        DUMPER.dumpSearchResult(starting, false);
        DUMPER.dumpSearchResult(lookingFor, false);
        DUMPER.dumpSearchResult(deepSearch, false);
        DUMPER.dumpSearchResult(statistics, false);
        if (allFiles != null) {
            addTask(allFiles);
            String msg = "Total Number of files to search => " + DirectorySearch.DIR_SEARCH.FILE_COUNTER.get();
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, false);
            startTaskCounterDisplay();
        } else {
            String msg = "There are no files in this directory! Exiting Search";
            System.out.println(msg);
            DUMPER.dumpSearchResult(msg, true);
        }
    }
}
