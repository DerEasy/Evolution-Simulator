package com.easy.evolutionsimulator;

import java.security.SecureRandom;

public class Calc {
    public static SecureRandom rng = new SecureRandom();

    /*
    1   2   3
    8   0   4
    7   6   5
    */


    //Defines the accepted direction keys incorporating the exceptions mandated by the exclusion key.
    //Read e.g. exTopLeft as "Except movements to the top left".

    public static final int[] exTopLeft     = {0, 4, 5, 6};         //exKey: 1
    public static final int[] exTop         = {0, 4, 5, 6, 7, 8};   //exKey: 2
    public static final int[] exTopRight    = {0, 6, 7, 8};         //exKey: 3
    public static final int[] exRight       = {0, 1, 2, 6, 7, 8};   //exKey: 4
    public static final int[] exBottomRight = {0, 1, 2, 8};         //exKey: 5
    public static final int[] exBottom      = {0, 1, 2, 3, 4, 8};   //exKey: 6
    public static final int[] exBottomLeft  = {0, 2, 3, 4};         //exKey: 7
    public static final int[] exLeft        = {0, 2, 3, 4, 5, 6};   //exKey: 8

    /**
     * Gets a valid direction key. Exceptions are ruled out with the exclusion key.
     *
     * If a Blob is at an edge or corner of the environment, it must be prohibited from
     * moving outside that environment. This is done by excluding direction keys that
     * lead outside the environment. The exclusion key equals the innermost value of
     * the excluded direction keys.
     *
     * E.g. exTop excludes 1 2 3 and 2 is the innermost/middle value of these, so
     * that's the exclusion key for Top exceptions.
     *
     * This method randomly chooses an index and returns the valid direction key at
     * that index of the corresponding array that is determined by the exclusion key.
     * @param exKey The exclusion key
     * @return A valid direction key
     */
    public static int getValidDirKey(int exKey) {
        int index = 0;

        //Check if index range is 4 or 6.
        //Odd exclusion keys exclude 5 of 9 direction keys.
        //Even exclusion keys exclude 3 of 9 direction keys.
        if (exKey % 2 == 1) {
            index = rng.nextInt(4);
        } else if (exKey % 2 == 0) {
            index = rng.nextInt(6);
        }

        switch (exKey) {
            case 1:
                return exTopLeft[index];
            case 2:
                return exTop[index];
            case 3:
                return exTopRight[index];
            case 4:
                return exRight[index];
            case 5:
                return exBottomRight[index];
            case 6:
                return exBottom[index];
            case 7:
                return exBottomLeft[index];
            case 8:
                return exLeft[index];
        }

        throw new IndexOutOfBoundsException("Calc: Exclusion key is out of range.");
    }

    //Defines valid mutations of the agro gene. This is used as nextInt() does not exclude 0
    //and if you try to shift the result by subtracting a certain amount of it, either the
    //positive or the negative side will have a higher chance of occurring.
    public static final int[] agroMutations = {-4, -3, -2, -1, 0, 1, 2, 3, 4};

    /**
     * Gets a valid agro mutation defined by the agroMutations array. All mutations have an
     * equal chance of occurring.
     * @return The mutation shift integer
     */
    public static int getValidAgroMutation() {
        return agroMutations[rng.nextInt(9)];
    }

    //Defines valid mutations of the agro gene. This is used as nextInt() does not exclude 0
    //and if you try to shift the result by subtracting a certain amount of it, either the
    //positive or the negative side will have a higher chance of occurring.
    public static final int[] sizeMutations = {-4, -3, -2, -1, 0, 1, 2, 3, 4};

    /**
     * Gets a valid size mutation defined by the sizeMutations array. All mutations have an
     * equal chance of occurring.
     * @return The mutation shift integer
     */
    public static int getValidSizeMutation() {
        return sizeMutations[rng.nextInt(9)];
    }

    /**
     * Calculates probabilities by providing a percentage from (typically) 0 to 100.
     * Example: If the percentage parameter is 25, there is a 1/4 chance of returning true.
     * @param percent Percentage probability of returning true
     * @return True if random number was in the given range
     */
    public static boolean calcProb(int percent) {
        if (percent >= 100) return true;
        if (percent <= 0) return false;

        percent = 100 - percent;
        int i = rng.nextInt(99) + 1;
        return i >= percent;
    }

    /**
     * Calculates the result of a logarithm with a custom base.
     * @param base The desired base
     * @param a The number that shall be calculated with
     * @return Result as an int
     */
    public static int logN(int base, int a) {
        return (int) Math.round((Math.log(a) / Math.log(base)));
    }
}
