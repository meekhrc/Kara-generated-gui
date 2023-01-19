package de.unistuttgart.hamster.examples;

import de.unistuttgart.hamster.main.SimpleLadybugGame;

public class Example01 extends SimpleLadybugGame {
    public static void main(final String[] args) {
        createInstance(Example01.class);
    }

    /**
     * How often Paule should repeat its behaviour.
     */
    private static final int SCENARIO_REPETITION_COUNT = 20;

    @Override
    protected void run() {
        displayInNewGameWindow();
        for (int i = 0; i < SCENARIO_REPETITION_COUNT; i++) {
            kara2.write("Hallo!");
            kara2.move();
            kara2.move();
            kara2.pickClover();
            kara2.pickClover();
            kara2.putClover();
            kara2.putClover();
            kara2.turnLeft();
            kara2.turnLeft();
            kara2.move();
            kara2.move();
            kara2.turnLeft();
            kara2.turnLeft();
        }
    }

}
