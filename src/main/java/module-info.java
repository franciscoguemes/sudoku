module com.franciscoguemes.sudoku {
    // JavaFX — controls re-exports graphics and base transitively
    requires javafx.controls;
//    requires javafx.fxml;

    // SLF4J API — logback-classic is discovered automatically at runtime via
    // ServiceLoader (org.slf4j uses SLF4JServiceProvider; logback provides it).
    // No explicit requires for logback is needed: the JVM includes service
    // providers in the module graph automatically when they are on --module-path.
    requires org.slf4j;

    // JavaFX instantiates Application subclasses (GameApp, EditorApp) and
    // introspects custom-control classes for CSS / property resolution via
    // reflection — all GUI packages must be opened to javafx.graphics.
    opens com.franciscoguemes.sudoku.gui        to javafx.graphics;
    opens com.franciscoguemes.sudoku.gui.game   to javafx.graphics;
    opens com.franciscoguemes.sudoku.gui.editor to javafx.graphics;

//    opens com.franciscoguemes.sudoku.gui to javafx.graphics, javafx.fxml;
//    opens com.franciscoguemes.sudoku.gui.game to javafx.graphics, javafx.fxml;
//    opens com.franciscoguemes.sudoku.gui.editor to javafx.graphics, javafx.fxml;

}
