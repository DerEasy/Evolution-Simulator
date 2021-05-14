package com.easy.evolutionsimulator;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Map;

import static com.easy.evolutionsimulator.Blob.foodQueueSize;
import static com.easy.evolutionsimulator.Blob.removeFoodQueue;
import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Main.*;

public class Log {
    public static WeakReference<Log> logRef;
    public static int foodEaten, blobDeaths, blobBirths, blobsEaten, blobsDefeated, blobAmount, foodAmount, day;
    private final DecimalFormat idFmt, timeFmt, xFmt, yFmt, doubleFmt, tripleFmt;

    public Log() {
        logRef = new WeakReference<>(this);
        idFmt = new DecimalFormat("000000");
        timeFmt = new DecimalFormat("0.000");
        doubleFmt = new DecimalFormat("00");
        tripleFmt = new DecimalFormat("000");

        if (dimX < 10)
            xFmt = new DecimalFormat("0");
        else if (dimX < 100)
            xFmt = new DecimalFormat("00");
        else if (dimX < 1000)
            xFmt = new DecimalFormat("000");
        else
            xFmt = new DecimalFormat("0000");

        if (dimY < 10)
            yFmt = new DecimalFormat("0");
        else if (dimY < 100)
            yFmt = new DecimalFormat("00");
        else if (dimY < 1000)
            yFmt = new DecimalFormat("000");
        else
            yFmt = new DecimalFormat("0000");
    }

    public void logEnv() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s\tTime elapsed: %ss\n",
                dimX + 1, dimY + 1, envSize, timeFmt.format(timeElapsed));
        System.out.printf("Food count: %s\tFood eaten: %s\tBlobs eaten: %s\tBlobs defeated: %s\tDay %s\n",
                foodAmount, foodEaten, blobsDefeated, blobsEaten, day);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n",
                blobAmount, blobDeaths, blobBirths);
    }

    public void logBlob(Blob blob) {
        if (blob.foodX != null)
            System.out.printf("ID(%s) (%s|%s)\tEnergy(%s)\tAge(%s) \tSize(%s)\tAgro(%s)\tSense(%s)\tSpeed(%s)\tDirProb(%s)\tPrefDir(%s)\tFood eaten(%s)\tFood(%s|%s)\n",
                idFmt.format(blob.id), xFmt.format(blob.posX), yFmt.format(blob.posY), doubleFmt.format(blob.energy),
                tripleFmt.format(blob.age), tripleFmt.format(blob.size), tripleFmt.format(blob.agro), doubleFmt.format(blob.sense),
                doubleFmt.format(blob.speed), tripleFmt.format(blob.dirProb), blob.prefDir, tripleFmt.format(blob.eatCount),
                xFmt.format(blob.foodX), yFmt.format(blob.foodY));
        else
            System.out.printf("ID(%s) (%s|%s)\tEnergy(%s)\tAge(%s) \tSize(%s)\tAgro(%s)\tSense(%s)\tSpeed(%s)\tDirProb(%s)\tPrefDir(%s)\tFood eaten(%s)\tFood(N/A)\n",
                    idFmt.format(blob.id), xFmt.format(blob.posX), yFmt.format(blob.posY), doubleFmt.format(blob.energy),
                    tripleFmt.format(blob.age), tripleFmt.format(blob.size), tripleFmt.format(blob.agro), doubleFmt.format(blob.sense),
                    doubleFmt.format(blob.speed), tripleFmt.format(blob.dirProb), blob.prefDir, tripleFmt.format(blob.eatCount));
    }

    /**
     * Prints a visual representation of the environment to the console.
     * o is a Blob. x is food. i is both at once.
     */
    public void printEnv() {
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

        if (printEnvEnabled) {
            for (int i = 0; i < foodQueueSize; i++) foodHash.remove(removeFoodQueue.poll());
            foodQueueSize = 0;
        }
    }
}
