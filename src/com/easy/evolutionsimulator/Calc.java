package com.easy.evolutionsimulator;

import java.util.ArrayList;
import java.util.Random;

public class Calc {
    public static Random rng = new Random();

    //Defines the valid mutation shifts
    //To control the chances of a specific mutation occurring more often, some values are duplicates.
    public static final int[] dirProbMutations = {-3, -2, -1, 0, 1, 2, 3};
    public static final int[] agroMutations = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
    public static final int[] sizeMutations = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
    public static final int[] speedMutations = {-1, 0, 0, 0, 0, 0, 1};
    public static final int[] senseMutations = {-1, 0, 0, 0, 0, 0, 1};

    //Defines the accepted direction keys incorporating the exceptions mandated by the exclusion key.
    //Read e.g. validTopLeft as "Except movements to the top left".
    //To control the chances of a specific direction key occurring more often, some keys are duplicates.
    public static final int[] freeMovement      = {0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8};  //exKey: 0
    public static final int[] validTopLeft      = {0, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6};                                         //exKey: 1
    public static final int[] validTop          = {0, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8 ,8};                 //exKey: 2
    public static final int[] validTopRight     = {0, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8};                                         //exKey: 3
    public static final int[] validRight        = {0, 1, 1, 1, 1, 2, 2, 2, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8};                 //exKey: 4
    public static final int[] validBottomRight  = {0, 1, 1, 1, 1, 1, 2, 2, 2, 8, 8, 8};                                         //exKey: 5
    public static final int[] validBottom       = {0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 8, 8, 8};                 //exKey: 6
    public static final int[] validBottomLeft   = {0, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4};                                         //exKey: 7
    public static final int[] validLeft         = {0, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6};                 //exKey: 8

    //Inverted version of the valid arrays
    public static final int[] exTopLeft     = {1, 2, 3, 7, 8};
    public static final int[] exTop         = {1, 2, 3};
    public static final int[] exTopRight    = {1, 2, 3, 4, 5};
    public static final int[] exRight       = {3, 4, 5};
    public static final int[] exBottomRight = {3, 4, 5, 6, 7};
    public static final int[] exBottom      = {5, 6, 7};
    public static final int[] exBottomLeft  = {1, 5, 6, 7, 8};
    public static final int[] exLeft        = {1, 7, 8};

    //Defines the prohibited direction keys when trying to set the flee mode
    public static final int[] prTopLeft     = {8, 1, 2};    //prKey: 1
    public static final int[] prTop         = {1, 2, 3};    //prKey: 2
    public static final int[] prTopRight    = {2, 3, 4};    //prKey: 3
    public static final int[] prRight       = {3, 4, 5};    //prKey: 4
    public static final int[] prBottomRight = {4, 5, 6};    //prKey: 5
    public static final int[] prBottom      = {5, 6, 7};    //prKey: 6
    public static final int[] prBottomLeft  = {6, 7, 8};    //prKey: 7
    public static final int[] prLeft        = {7, 8, 1};    //prKey: 8


    /**
     * Gets a valid direction key. Exceptions are ruled out with the exclusion key.
     *
     * If a Blob is at an edge or corner of the environment, it must be prohibited from
     * moving outside that environment. This is done by excluding direction keys that
     * lead outside the environment. The exclusion key equals the innermost value of
     * the excluded direction keys.
     *
     * E.g. validTop excludes 1 2 3 and 2 is the innermost/middle value of these, so
     * that's the exclusion key for Top exceptions.
     *
     * This method randomly chooses an index and returns the valid direction key at
     * that index of the corresponding array that is determined by the exclusion key.
     * @param exKey The exclusion key
     * @return A valid direction key
     */
    public static int getValidDirKey(int exKey) {
        /*
        1   2   3
        8   0   4
        7   6   5
        */

        int index = 0;

        //Check if index range is 4 or 6.
        //Odd exclusion keys exclude 5 of 9 direction keys.
        //Even exclusion keys exclude 3 of 9 direction keys.
        if (exKey % 2 == 1) {
            index = rng.nextInt(12);
        } else if (exKey % 2 == 0) {
            index = rng.nextInt(20);
        }

        switch (exKey) {
            case 1:
                return validTopLeft[index];
            case 2:
                return validTop[index];
            case 3:
                return validTopRight[index];
            case 4:
                return validRight[index];
            case 5:
                return validBottomRight[index];
            case 6:
                return validBottom[index];
            case 7:
                return validBottomLeft[index];
            case 8:
                return validLeft[index];
        }

        throw new IndexOutOfBoundsException("Calc: Exclusion key is out of range.");
    }

    public static int[] getValidDirArray(int exKey) {
        switch (exKey) {
            case 1:
                return validTopLeft;
            case 2:
                return validTop;
            case 3:
                return validTopRight;
            case 4:
                return validRight;
            case 5:
                return validBottomRight;
            case 6:
                return validBottom;
            case 7:
                return validBottomLeft;
            case 8:
                return validLeft;
        }

        throw new IndexOutOfBoundsException("Calc: Exclusion key is out of range.");
    }

    public static ArrayList<Integer> removeUnsafeDir(ArrayList<Integer> safeDir, int prKey) {
        switch (prKey) {
            case 1:
                for (int i : prTopLeft)
                    safeDir.remove((Integer) i);
                break;
            case 2:
                for (int i : prTop)
                    safeDir.remove((Integer) i);
                break;
            case 3:
                for (int i : prTopRight)
                    safeDir.remove((Integer) i);
                break;
            case 4:
                for (int i : prRight)
                    safeDir.remove((Integer) i);
                break;
            case 5:
                for (int i : prBottomRight)
                    safeDir.remove((Integer) i);
                break;
            case 6:
                for (int i : prBottom)
                    safeDir.remove((Integer) i);
                break;
            case 7:
                for (int i : prBottomLeft)
                    safeDir.remove((Integer) i);
                break;
            case 8:
                for (int i : prLeft)
                    safeDir.remove((Integer) i);
                break;
        }
        return safeDir;
    }

    public static ArrayList<Integer> removeExDir(ArrayList<Integer> safeDir, int exKey) {
        switch (exKey) {
            case 1:
                for (int i : exTopLeft)
                    safeDir.remove((Integer) i);
                break;
            case 2:
                for (int i : exTop)
                    safeDir.remove((Integer) i);
                break;
            case 3:
                for (int i : exTopRight)
                    safeDir.remove((Integer) i);
                break;
            case 4:
                for (int i : exRight)
                    safeDir.remove((Integer) i);
                break;
            case 5:
                for (int i : exBottomRight)
                    safeDir.remove((Integer) i);
                break;
            case 6:
                for (int i : exBottom)
                    safeDir.remove((Integer) i);
                break;
            case 7:
                for (int i : exBottomLeft)
                    safeDir.remove((Integer) i);
                break;
            case 8:
                for (int i : exLeft)
                    safeDir.remove((Integer) i);
                break;
        }
        return safeDir;
    }

    public static int getDirProbMutation() {
        return dirProbMutations[rng.nextInt(7)];
    }

    public static int getSpeedMutation(Blob blob) {
        if (blob.speed > 1)
            return speedMutations[rng.nextInt(7)];
        else return
                senseMutations[rng.nextInt(6) + 1];
    }

    public static int getSenseMutation(Blob blob) {
        if (blob.sense > 1)
            return senseMutations[rng.nextInt(7)];
        else return
                senseMutations[rng.nextInt(6) + 1];
    }

    public static int getAgroMutation() {
        return agroMutations[rng.nextInt(9)];
    }

    public static int getSizeMutation() {
        return sizeMutations[rng.nextInt(9)];
    }

    /**
     * Calculates probabilities by providing a percentage from (typically) 0 to 100.
     * Example: If the percentage parameter is 25, there is a 1/4 chance of returning true.
     * @param percent Percentage probability of returning true
     * @return True if random number was in the given range
     */
    public static boolean calcProb(int percent) {
        if (percent >= 100)
            return true;
        if (percent <= 0)
            return false;

        percent = 100 - percent;
        return (rng.nextInt(99) + 1) >= percent;
    }
}
