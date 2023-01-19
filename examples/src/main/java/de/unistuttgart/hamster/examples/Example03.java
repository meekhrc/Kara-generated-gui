package de.unistuttgart.hamster.examples;

import de.unistuttgart.hamster.main.SimpleLadybugGame;

public class Example03 extends SimpleLadybugGame {
    public static void main(final String[] args) {
        createInstance(Example03.class);
    }

    /**
     * Another hamster program, which tests the loading of another territory.
     */
    @Override
    protected void run() {
        displayInNewGameWindow();
        loadTerritoryFromResourceFile("de.unistuttgart.hamster.territories/example03.ter");
        startGame();

        turnRight();
        kara2.move();
    }

    void turnRight() {
        kara2.turnLeft();
        kara2.turnLeft();
        kara2.turnLeft();
    }
}
