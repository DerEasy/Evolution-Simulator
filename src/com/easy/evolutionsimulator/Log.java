package com.easy.evolutionsimulator;

import java.text.DecimalFormat;
import java.util.Map;

import static com.easy.evolutionsimulator.Blob.foodQueueSize;
import static com.easy.evolutionsimulator.Blob.removeFoodQueue;
import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Main.*;

public class Log {
    public static int foodEaten, blobDeaths, blobBirths, blobsEaten, blobsDefeated, blobAmount, foodAmount, day;
    private static final DecimalFormat df = new DecimalFormat("0.000");

    public static void logEnv() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s\tTime elapsed: %ss\n", dimX + 1, dimY + 1, envSize, df.format(timeElapsed));
        System.out.printf("Food count: %s\tFood eaten: %s\tBlobs eaten: %s\tBlobs defeated: %s\tDay %s\n", foodAmount, foodEaten, blobsDefeated, blobsEaten, day);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n", blobAmount, blobDeaths, blobBirths);
    }

    public static void logBlob(Blob blob) {
        System.out.printf("ID(%s) (%s|%s)     \tEnergy(%s)\tAge(%s) \tSize(%s)\tAgro(%s)\tSense(%s)\tSpeed(%s)\n",
                blob.getID(), blob.posX, blob.posY, blob.getEnergy(), blob.getAge(), blob.getSize(), blob.getAgro(), blob.getSense(), blob.getSpeed());
    }

    /**
     * Prints a visual representation of the environment to the console.
     * o is a Blob. x is food. i is both at once.
     */
    public static void printEnv() {
        Blob blob;
        Food food;

        for (int y = dimY; y >= 0; y--) {
            for (int x = 0; x <= dimX; x++) {
                for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
                    blob = blobEntity.getValue();
                    hasBlob[x][y] = blob.posX == x && blob.posY == y;
                    if (hasBlob[x][y]) break;
                }
                for (Map.Entry<Integer, Food> foodEntity : foodHash.entrySet()) {
                    food = foodEntity.getValue();
                    hasFood[x][y] = food.posX == x && food.posY == y;
                    if (hasFood[x][y]) break;
                }
            }
        }

        for (int y = dimY; y >= 0; y--) {
            System.out.print(y + "\t\t");
            for (int x = 0; x <= dimX; x++) {
                if (hasBlob[x][y] && !hasFood[x][y]) {
                    System.out.print("|o");
                } else if (hasBlob[x][y] && hasFood[x][y]) {
                    System.out.print("|i");
                } else if (!hasBlob[x][y] && hasFood[x][y]) {
                    System.out.print("|x");
                } else {
                    System.out.print("| ");
                }
            }
            System.out.println("|");
        }

        for (int i = 0; i < foodQueueSize; i++) foodHash.remove(removeFoodQueue.poll());
        foodQueueSize = 0;
    }
}
