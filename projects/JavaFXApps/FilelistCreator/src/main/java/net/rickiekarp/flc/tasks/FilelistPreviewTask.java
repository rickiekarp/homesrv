package net.rickiekarp.flc.tasks;

import net.rickiekarp.core.controller.LanguageController;
import net.rickiekarp.core.debug.DebugHelper;
import net.rickiekarp.core.debug.LogFileHandler;
import net.rickiekarp.core.view.MessageDialog;
import net.rickiekarp.flc.controller.FilelistController;
import net.rickiekarp.flc.view.layout.MainLayout;
import javafx.concurrent.Task;

public class FilelistPreviewTask extends Task<Void> {

    private String listStr;

    @Override
    protected Void call() throws Exception {
        listStr = FilelistController.flController.getList();
        return null;
    }

    public FilelistPreviewTask() {

        this.setOnRunning(event -> {
            MainLayout.mainLayout.setStatus("neutral", LanguageController.getString("status_build_fileList"));
        });

        this.setOnSucceeded(event1 -> {
            MainLayout.previewTA.setText(listStr);
            DebugHelper.profile("stop", "FilelistPreviewTask");
            MainLayout.mainLayout.setStatus("neutral", LanguageController.getString("ready"));
            listStr = null;
            System.gc();
        });

        this.setOnFailed(event -> {
            DebugHelper.profile("stop", "FilelistPreviewTask");
            listStr = null;
            System.gc();
            new MessageDialog(0, LanguageController.getString("unknownError"), 450, 220);
            LogFileHandler.logger.info("filePreview.failed");
        });

        MainLayout.previewTA.clear();
        DebugHelper.profile("start", "FilelistPreviewTask");

//        System.out.println("availableProcessors" + Runtime.getRuntime().availableProcessors());
//        Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
//        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(new Runnable() {
//                public void run() {
//                    // some code to run in parallel
//                }
//            });
//            threads[i].start();
//        }

        //start the task in a new thread
        Thread previewThread = new Thread(this);
        previewThread.setDaemon(true);
        previewThread.start();
    }

}