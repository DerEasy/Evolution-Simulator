package com.easy.evolutionsimulator;

public class S {
    public static void l() {
        System.out.println();
    }

    public static void p(String s) {
        System.out.println(s);
    }

    public static final String TITLE =
            "\nEvolution Simulator\n";

    public static final String HELP =
            "Help:\nYou will want to spawn at least one type of Blob in the environment.\n" +
                 "The dimensions of the environment should not be extremely large to avoid performance problems.\n" +
                 "Optimal dimension values normally have two or three digits. For demonstration purposes, one digit is good.\n\n" +
                 "Do not create an overabundance of food entities. Adjust the amount to a reasonable value according\n to the total amount of blocks in the environment.\n" +
                 "If you increase the amount of moves that Blobs will do in a day, the amount of food must also be\n increased to level out the increased energy consumption\n" +
                 "It is most reasonable to keep size and agro values somewhere at the lower two-digit numbers.\n" +
                 "Sense and speed values should be kept very low in accordance to the dimensions.\n\n" +
                 "Legend for the visual representation system:\no stands for a/multiple Blob entities\nx stands for a/multiple food entities\n" +
                 "i is displayed whenever a/multiple Blob entities stand on the same block as a/multiple food entities.\n" +
                 "The environment will not be printed if either the x or y dimension value exceeds 80.\n\n\n\n";

    public static final String CONFIG =
            "Do you want to configure the simulator (0) or run demo mode (1) or dev mode (2)? 0/1/2:";

    public static final String DIMX =
            "Set the size of the x dimension of the environment:";

    public static final String DIMY =
            "Set the size of the y dimension of the environment:";

    public static final String POPLIMIT =
            "How many Blobs may be in the environment at once? 0 to disable population limit:";

    public static final String SPAWNBLOBS =
            "Spawn (additional) Blobs? 0/1:";

    public static final String CLEARENTITIES =
            "Clear all previously created entities? 0/1:";

    public static final String BLOBAMOUNT =
            "Amount of Blobs to be spawned:";

    public static final String SENSE =
            "Sense value (Keep it very low, but nonzero):";

    public static final String SPEED =
            "Speed value (Keep it very low, but nonzero):";

    public static final String SIZE =
            "Size value (1 - 100):";

    public static final String AGRO =
            "Agro value (0 - 100):";

    public static final String DIRPROB =
            "Probability of travelling in straight lines (0 - 100):";

    public static final String DAYS =
            "How many days shall be simulated? 0 to continue running indefinitely:";

    public static final String FOODEATEN =
            "At what number of food that has been eaten shall the simulation stop? 0 to disable:";

    public static final String RUNTIME =
            "How long in seconds shall the simulation run? 0 to continue running indefinitely:";

    public static final String PAUSETIME =
            "For how long in ms do you want to pause execution after a day has been simulated? 0 to disable:";

    public static final String GRAPHICS =
            "Do you want to print a visual representation of the environment in the console? 0/1:";

    public static final String MOVESPERDAY =
            "How many moves shall the Blobs do in one day? Default is 1:";

    public static final String FOODAMOUNT =
            "How many food entities shall be available everyday?";

    public static final String FOODSATIETY =
            "Food satiety (Amount of energy Blobs will get if they eat food)? Default is 15:";

    public static final String START =
            "Ready. Start simulation? 0/1:";

    public static final String EXIT =
            "\nEnter anything to exit.";

    public static final String LOGBLOB =
            "ID(%s) (%s|%s)  Energy(%s)  Age(%s)  Size(%s)  Agro(%s)  Sense(%s)  Speed(%s)  Orig./DirProb(%s/%s)  PrefDir(%s)  Food(%s|%s)  eaten(%s)\n";

    public static final String LOGBLOB_NA =
            "ID(%s) (%s|%s)  Energy(%s)  Age(%s)  Size(%s)  Agro(%s)  Sense(%s)  Speed(%s)  Orig./DirProb(%s/%s)  PrefDir(%s)  Food(N/A)  eaten(%s)\n";
}
