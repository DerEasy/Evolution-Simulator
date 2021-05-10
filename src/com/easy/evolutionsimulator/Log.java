package com.easy.evolutionsimulator;

import java.text.DecimalFormat;

import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Main.timeElapsed;

public class Log {
    public static int foodEaten, blobDeaths, blobBirths, blobsEaten, blobsDefeated, blobAmount, foodAmount, day;
    private static final DecimalFormat df = new DecimalFormat("0.000");

    public static void logEnv() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s\tTime elapsed: %ss\n", dimX + 1, dimY + 1, envSize, df.format(timeElapsed));
        System.out.printf("Food count: %s\tFood eaten: %s\tBlobs eaten: %s\tBlobs defeated: %s\tDay %s\n", foodAmount, foodEaten, blobsDefeated, blobsEaten, day);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n", blobAmount, blobDeaths, blobBirths);
    }

    public static void logBlob(Blob blob) {
        System.out.printf("BlobID(%s)\tx: %s\ty: %s\tEnergy: %s\tAge: %s\tSize: %s\tAgro: %s\n",
                blob.id, blob.posX, blob.posY, blob.energy, blob.age, blob.size, blob.agro);
    }
}
