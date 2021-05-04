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

    public static void main(String[] args) {
        startTime = System.nanoTime();
        new Main();
    }

    public Main() {
        envLogic = new Environment(6, 6);
        envLogic.spawnBlobs(1, "default", false);
        //envLogic.spawnBlobs(1, "default", false, 4, 3);
        //envLogic.spawnFood(1, "default", true, 0, 0);
        //envLogic.spawnFood(1, "default", false ,3, 0);
        //envLogic.spawnFood(1, "default", false ,0, 2);

        hasBlob = new boolean[dimX + 1][dimY + 1];
        hasFood = new boolean[dimX + 1][dimY + 1];

        startDay();
    }

    public void startDay() {
        while (day <= 1000 && blobAmount > 0) {
            finishTime = System.nanoTime();
            timeElapsed = (double) (finishTime - startTime) / 1000000000;
            logEnv();
            printEnv();
            envLogic.startDay(1, 4, "default");
            envLogic.endDay(1);
        }
        logEnv();
    }

    /**
     * Prints a visual representation of the environment to the console.
     * o is a Blob. x is food. i is both at once.
     */
    public static void printEnv() {
        Blob blob;
        Food food;
        if ((dimX + 1) <= 80 && (dimY + 1) <= 80) {
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

            for (int i = 0; i < removeFoodList.size(); i++) {
                foodHash.remove(removeFoodList.get(i));
            }
            removeFoodList.clear();
        }
    }
}