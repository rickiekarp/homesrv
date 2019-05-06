module flc {
    requires javafx.controls;
    requires api;
    requires core;
    requires java.xml;
    requires java.logging;
    requires java.desktop;

    opens net.rickiekarp.flc.model;
    opens net.rickiekarp.flc;
    opens net.rickiekarp.flc.settings;
    exports net.rickiekarp.flc;
}