module de.unistuttgart.hamster.core {
    requires transitive mpw.framework.core;
    requires mpw.framework.utils;
    requires javafx.base;

    exports de.unistuttgart.hamster.facade;
    exports de.unistuttgart.hamster.hamster to de.unistuttgart.hamster.main;
    exports de.unistuttgart.hamster.viewmodel.impl to de.unistuttgart.hamster.ui;

    opens de.unistuttgart.hamster.hamster;
    opens de.unistuttgart.hamster.territories;
}