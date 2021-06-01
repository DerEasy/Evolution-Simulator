package com.easy.evolutionsimulator;

import java.text.DecimalFormat;
import java.util.Map;

import static com.easy.evolutionsimulator.S.*;

public class Log {
    private final Main main;
    private final Environment env;
    private final DecimalFormat idFmt, timeFmt, xFmt, yFmt, doubleFmt, tripleFmt;

    public int foodEaten, blobDeaths, blobBirths, blobsEaten, blobsDefeated, blobAmount, foodAmount, day;
    public boolean[][] hasBlob, hasFood;

    public Log(Main main, Environment env) {
        this.main = main;
        this.env = env;

        idFmt = new DecimalFormat("000000");
        timeFmt = new DecimalFormat("0.000");
        doubleFmt = new DecimalFormat("00");
        tripleFmt = new DecimalFormat("000");

        if (env.dimX < 10)
            xFmt = new DecimalFormat("0");
        else if (env.dimX < 100)
            xFmt = new DecimalFormat("00");
        else if (env.dimX < 1000)
            xFmt = new DecimalFormat("000");
        else
            xFmt = new DecimalFormat("0000");


        if (env.dimY < 10)
            yFmt = new DecimalFormat("0");
        else if (env.dimY < 100)
            yFmt = new DecimalFormat("00");
        else if (env.dimY < 1000)
            yFmt = new DecimalFormat("000");
        else
            yFmt = new DecimalFormat("0000");
    }

    public void logEnv() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s\tTime elapsed: %ss\n",
                env.dimX + 1, env.dimY + 1, env.envSize, timeFmt.format(main.timeElapsed));
        System.out.printf("Food count: %s\tFood eaten: %s\tBlobs eaten: %s\tBlobs defeated: %s\tDay %s\n",
                foodAmount, foodEaten, blobsDefeated, blobsEaten, day);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n",
                blobAmount, blobDeaths, blobBirths);
    }

    public void logBlob(Blob blob) {
        if (blob.foodX != null)
            System.out.printf(LOGBLOB,
                idFmt.format(blob.id),
                    xFmt.format(blob.posX),
                    yFmt.format(blob.posY),
                    doubleFmt.format(blob.energy),
                    tripleFmt.format(blob.age),
                    tripleFmt.format(blob.size),
                    tripleFmt.format(blob.agro),
                    doubleFmt.format(blob.sense),
                    doubleFmt.format(blob.speed),
                    tripleFmt.format(blob.defaultDirProb),
                    tripleFmt.format(blob.dirProb),
                    blob.prefDir,
                    xFmt.format(blob.foodX),
                    yFmt.format(blob.foodY),
                    tripleFmt.format(blob.eatCount));
        else
            System.out.printf(LOGBLOB_NA,
                idFmt.format(blob.id),
                    xFmt.format(blob.posX),
                    yFmt.format(blob.posY),
                    doubleFmt.format(blob.energy),
                    tripleFmt.format(blob.age),
                    tripleFmt.format(blob.size),
                    tripleFmt.format(blob.agro),
                    doubleFmt.format(blob.sense),
                    doubleFmt.format(blob.speed),
                    tripleFmt.format(blob.defaultDirProb),
                    tripleFmt.format(blob.dirProb),
                    blob.prefDir,
                    tripleFmt.format(blob.eatCount));
    }

    /**
     * Prints a visual representation of the environment to the console.
     * o is a Blob. x is food. i is both at once.
     */
    public void printEnv() {
        Blob blob;
        Environment.Food food;

        for (int y = env.dimY; y >= 0; y--) {
            for (int x = 0; x <= env.dimX; x++) {
                for (Map.Entry<Integer, Blob> blobEntity : env.blobHash.entrySet()) {
                    blob = blobEntity.getValue();
                    hasBlob[x][y] = blob.posX == x && blob.posY == y;
                    if (hasBlob[x][y]) break;
                }
                for (Map.Entry<Integer, Environment.Food> foodEntity : env.foodHash.entrySet()) {
                    food = foodEntity.getValue();
                    hasFood[x][y] = food.posX == x && food.posY == y;
                    if (hasFood[x][y]) break;
                }
            }
        }

        for (int y = env.dimY; y >= 0; y--) {
            System.out.print(y + "\t\t");
            for (int x = 0; x <= env.dimX; x++) {
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

        if (main.printEnvEnabled) {
            for (int i = 0; i < env.foodQueueSize; i++) env.foodHash.remove(env.removeFoodQueue.poll());
            env.foodQueueSize = 0;
        }
    }
}
