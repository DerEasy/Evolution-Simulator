package com.easy.evolutionsimulator;

import java.text.DecimalFormat;

import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Main.timeElapsed;

public class Log {
    static int foodEaten, blobDeaths, blobBirths, blobAmount, foodAmount, day;
    static DecimalFormat df = new DecimalFormat("#.###");

    public static void logEnv() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s\tTime elapsed: %ss\n", dimX + 1, dimY + 1, envSize, df.format(timeElapsed));
        System.out.printf("Food count: %s\tFood eaten: %s\tDay %s\n", foodAmount, foodEaten, day);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n", blobAmount, blobDeaths, blobBirths);
    }

    public static void logBlob(Blob blob) {
        System.out.printf("BlobID(%s)\tx: %s\ty: %s\tEnergy: %s\tFoodDir: %s\tx: %s\ty: %s\tAdjSpeed: %s\tExKey: %s\n",
                blob.id, blob.posX, blob.posY, blob.energy, blob.dirFood, blob.foodX, blob.foodY, blob.adjustedSpeed, blob.getExKey());
    }
}
