package de.unistuttgart.hamster.viewmodel.impl;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Color;

public class HamsterColors {
    private HamsterColors() {}

    private static final Color[] hamsterColors = new Color[] {
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.PINK,
            Color.MAGENTA,
            Color.RED
    };

    public static Color getColorForNthHamster(final int number) {
        final int index = number % HamsterColors.hamsterColors.length;
        return HamsterColors.hamsterColors[index];
    }
}
