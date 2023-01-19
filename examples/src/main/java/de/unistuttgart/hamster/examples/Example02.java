package de.unistuttgart.hamster.examples;

import de.unistuttgart.hamster.main.SimpleLadybugGame;
import de.unistuttgart.hamster.facade.Ladybug;

public class Example02 extends SimpleLadybugGame {
    public static void main(final String[] args) {
        createInstance(Example02.class);
    }

    Ladybug kara1;

    /**
     * Another hamster program. The idea is to create Paula close to Paula with
     * grain in her mouth. She drops it and Paule picks it up.
     */
    @Override
    protected void run() {
        displayInNewGameWindow();

        kara1 = new Ladybug(game.getTerritory(), kara2.getLocation(), kara2.getDirection());
        kara1.move();
        kara1.putClover();
        kara1.move();

        kara2.move();
        kara2.pickClover();
        kara2.turnLeft();
        kara2.turnLeft();
        kara2.move();
        kara2.putClover();
    }
}
