package net.rickiekarp.flc.settings;

import net.rickiekarp.core.settings.LoadSave;
import net.rickiekarp.flc.model.Directorylist;
import net.rickiekarp.flc.model.Filelist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppConfiguration {

    /** Observable lists **/
    public static ObservableList<Filelist> fileData = FXCollections.observableArrayList();
    public static ObservableList<Directorylist> dirData = FXCollections.observableArrayList();
    public static ObservableList<String> unitList = FXCollections.observableArrayList("B", "KB", "MB", "GB", "TB");

    /** settings **/
    @LoadSave
    public static boolean subFolderCheck = true;

}