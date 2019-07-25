package de.elliepotato.sleepy.util;

import java.util.Optional;

/**
 * @author Ellie :: 24/07/2019
 */
public class NumberTools {

    public static Optional<Integer> tryParse (String value) {

        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
        }

        return Optional.empty();
    }

    public static boolean equals (int a, int b) {
        return a == b;
    }

    public static double halfFloor (int x) {
        return Math.floor (x / 2);
    }

}
