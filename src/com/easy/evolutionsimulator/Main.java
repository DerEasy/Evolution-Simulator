package com.easy.evolutionsimulator;

import java.util.Map;

import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Log.*;
import static com.easy.evolutionsimulator.Blob.*;

public class Main {
    Environment envLogic;
    static boolean[][] hasBlob;
    static boolean[][] hasFood;
    static long startTime, finishTime;
    static double timeElapsed;
    static boolean printEnvEnabled;

    public static void main(String[] args) {
        startTime = System.nanoTime();
        new Main();
    }

    public Main() {
        envLogic = new Environment(8, 8);
        envLogic.spawnBlobs(3, "default", false);
        envLogic.spawnBlobs(6, "small", false);

        hasBlob = new boolean[dimX + 1][dimY + 1];
        hasFood = new boolean[dimX + 1][dimY + 1];

        startSimulation(0, 0, true, 0, 1, 4, "default");
    }

    public void startSimulation(int days, int foodAmountToEat, boolean printEnv, int maxBlobs, int moveCount, int foodCount, String foodType) {
        if (days == 0) days = Integer.MAX_VALUE;
        if (foodAmountToEat == 0) foodAmountToEat = Integer.MAX_VALUE;
        printEnvEnabled = printEnv && (dimX + 1) <= 80 && (dimY + 1) <= 80;

        if (printEnvEnabled) {
            while (blobAmount > 0 && day <= days && foodEaten < foodAmountToEat) {
                //Logging
                finishTime = System.nanoTime();
                timeElapsed = (double) (finishTime - startTime) / 1000000000;
                logEnv();
                printEnv();

                //Simulation
                envLogic.startDay(moveCount, foodCount, foodType);
                envLogic.endDay(maxBlobs);
            }
        } else {
            while (blobAmount > 0 && day <= days && foodEaten < foodAmountToEat) {
                //Logging
                finishTime = System.nanoTime();
                timeElapsed = (double) (finishTime - startTime) / 1000000000;
                logEnv();

                //Simulation
                envLogic.startDay(moveCount, foodCount, foodType);
                envLogic.endDay(maxBlobs);
            }
        }
        //Output last data that was made available at the end
        logEnv();
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