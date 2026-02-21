module com.franciscoguemes.sudoku {
    // JavaFX — controls re-exports graphics and base transitively
    requires javafx.controls;

    // SLF4J API; the logback-classic backend is discovered at runtime
    // via ServiceLoader (org.slf4j uses SLF4JServiceProvider) and therefore
    // does not need an explicit requires here.
    requires org.slf4j;

    // JavaFX instantiates Application subclasses (GameApp, EditorApp) and
    // introspects custom-control classes for CSS / property resolution via
    // reflection — all GUI packages must be opened to javafx.graphics.
    opens com.franciscoguemes.sudoku.gui        to javafx.graphics;
    opens com.franciscoguemes.sudoku.gui.game   to javafx.graphics;
    opens com.franciscoguemes.sudoku.gui.editor to javafx.graphics;
}
