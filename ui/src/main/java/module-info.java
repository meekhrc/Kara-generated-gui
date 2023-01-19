module de.unistuttgart.hamster.ui {
    requires transitive de.unistuttgart.hamster.core;
    requires mpw.framework.utils;

    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;

    exports de.unistuttgart.hamster.ui;
    opens de.unistuttgart.hamster.ui;
    opens fxml;
    opens css;
    opens images;
}