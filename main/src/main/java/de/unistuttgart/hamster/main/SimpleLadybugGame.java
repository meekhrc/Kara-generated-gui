package de.unistuttgart.hamster.main;

import de.unistuttgart.hamster.facade.TerritoryLoader;
import de.unistuttgart.hamster.facade.Ladybug;
import de.unistuttgart.hamster.facade.HamsterGame;
import de.unistuttgart.hamster.ui.JavaFXUI;
import de.unistuttgart.iste.sqa.mpw.framework.exceptions.GameAbortedException;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static de.unistuttgart.iste.sqa.mpw.framework.utils.Preconditions.*;

public abstract class SimpleLadybugGame {
    private static final String DEFAULT_HAMSTER_TERRITORY = "de.unistuttgart.hamster.territories/example01.ter";

    protected static void createInstance(Class<? extends SimpleLadybugGame> hamsterProgramClass) {
        try {
            var program = hamsterProgramClass.getConstructor().newInstance();
            program.doRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Name of the environment variable used to determine the output interface
     */
    private static final String OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME = "OUTPUT_INTERFACE";

    /**
     * Variable inherited to child classes containing the default hamster
     * which is named hamster here. Intentionally, no getter or setter is used
     * as they are introduced only after lecture 2.
     */
    protected final Ladybug kara2;

    /**
     * The game object of this simple game. Can be used to start, stop, reset,
     * or display the game.
     */
    protected final HamsterGame game = new HamsterGame();

    /**
     * The current SimpleHamsterGame. Can be used to load a territory or to
     * display the game in a new game window.
     */
    protected final SimpleLadybugGame currentGame = this;

    /**
     * A console object to demonstrate IO besides using the read or write methods
     * of hamsters.
     */
    protected final Console console = System.console();

    /**
     * Initialized a simple hamster game by loading a default territory
     * and setting protected references to contain hamster and
     * the game.
     */
    public SimpleLadybugGame() {
        initializeGame();
        game.startGamePaused();

        this.kara2 = this.game.getTerritory().getDefaultHamster();
    }

    /**
     * Initialized a simple hamster game by loading a default territory.
     * This resets the game if it was already started. After the territory was loaded, the game is
     * in mode INITIALIZING. To start the game, game.startGame() should be called.
     */
    protected void initializeGame() {
        try {
            game.hardReset();
            TerritoryLoader.initializeFor(game).loadFromResourceFile(DEFAULT_HAMSTER_TERRITORY);
        } catch (IOException e) {
            throw new RuntimeException("failed to load the default territory", e);
        }
    }

    /**
     * Convenient method to start the current game.
     * The game has to be in mode INITIALIZING.
     */
    protected void startGame() {
        game.startGame();
    }

    /**
     * Predefined hamster method designed to be overridden in subclass.
     * Put the hamster code into this method. This parent class version
     * is empty, so that the hamster does not do anything by default.
     */
    protected abstract void run();

    /**
     * Method to start the execution of a hamster game and handle any exceptions happening
     * while running.
     */
    public final void doRun() {
        try {
            this.run();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final RuntimeException e) {
            this.game.confirmAlert(e);
            throw e;
        }
        this.game.stopGame();
    }

    /**
     * Displays the hamster game in a new game window
     * The UI type can be specified in the config file or in the environment variable
     * OUTPUT_INTERFACE. Possible values are JAVA_FX and NONE
     * The default is JAVA_FX.
     */
    protected void displayInNewGameWindow() {
        final String mode = UIMode.JAVA_FX;
        switch (mode) {
            case UIMode.JAVA_FX:
                JavaFXUI.displayInNewGameWindow(this.game);
                break;
            case UIMode.NONE:
                // ignore
                break;
            default:
                throw new IllegalStateException("Unknown output interface type, possible values are: " +
                        UIMode.JAVA_FX + " or " + UIMode.NONE);
        }
    }

    /*@
     @ requires true;
     @ ensures game.getCurrentGameMode() == Mode.INITIALIZING;
     @*/
    /**
     * Loads the Territory from a resources file.
     * Only resource paths are allowed. E.g. the fileName "territory.ter" represents the file
     * territory.ter in the resources directory
     * This resets the game if it was already started. After the territory was loaded, the game is
     * in mode INITIALIZING. To start the game, game.startGame() should be called
     *
     * @param fileName A resource path to the resource file.
     * @throws IllegalArgumentException if fileName is no valid resource path
     *                                  or if the file was not found
     */
    protected final void loadTerritoryFromResourceFile(final String fileName) {
        checkNotNull(fileName);
        final InputStream territoryFileStream = getClass().getClassLoader().getResourceAsStream(fileName);
        checkArgument(territoryFileStream != null, "territory file '" + fileName + "' not found");
        try {
            game.hardReset();
            TerritoryLoader.initializeFor(game).loadFromResourceFile(fileName);
        } catch (IOException e) {
            game.confirmAlert(e);
        }
    }

    /**
     * Loads the UI Mode from the environment variable if possible
     *
     * @return The UI mode if the environment variable was set, otherwise an empty optional
     * @throws IllegalStateException if an illegal value is set
     */
    private static Optional<String> getUIModeFromEnvironmentVariable() {
        final String value = System.getenv(SimpleLadybugGame.OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME);
        if (value != null) {
            try {
                return Optional.of(value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException("Illegal environmental variable", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Different UI types
     * No enum is used because enums use reflection for valueOf which can cause issues if reflection is forbidden
     */
    private static final class UIMode {
        public static final String JAVA_FX = "JAVA_FX";
        public static final String NONE = "NONE";
    }
}
