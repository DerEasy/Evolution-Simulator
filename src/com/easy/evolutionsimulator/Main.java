package com.easy.evolutionsimulator;

import static com.easy.evolutionsimulator.Environment.*;

public class Main {
    Environment envLogic;
    boolean[][] hasBlob;
    boolean[][] hasFood;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        envLogic = new Environment(10, 10);
        envLogic.spawnBlobs(2);

        hasBlob = new boolean[dimX + 1][dimY + 1];
        hasFood = new boolean[dimX + 1][dimY + 1];

        printEnv();
        startDay();
    }

    public void startDay() {
        while (Environment.foodEaten < 5000 && blobEntities.size() > 0) {
            envLogic.startDay(1, 4, "default");
            printEnv();
            envLogic.endDay(5);
            log();
        }
    }

    /**
     * Prints a visual representation of the environment to the console.
     * o is a Blob. x is food. z is both at once.
     */
    public void printEnv() {
        if ((dimX + 1) <= 50 && (dimY + 1) <= 50) {
            for (int y = dimY; y >= 0; y--) {
                for (int x = 0; x <= dimX; x++) {
                    for (Blob blob : blobEntities) {
                        hasBlob[x][y] = blob.posX == x && blob.posY == y;
                        if (hasBlob[x][y]) break;
                    }
                    for (Food food : foodEntities) {
                        hasFood[x][y] = food.posX == x && food.posY == y;
                        if (hasFood[x][y]) break;
                    }
                }
            }

            for (int y = dimY; y >= 0; y--) {
                for (int x = 0; x <= dimX; x++) {
                    if (hasBlob[x][y] && ! hasFood[x][y]) {
                        System.out.print("[o]");
                    } else if (hasBlob[x][y] && hasFood[x][y]) {
                        System.out.print("[z]");
                    } else if (! hasBlob[x][y] && hasFood[x][y]) {
                        System.out.print("[x]");
                    } else {
                        System.out.print("[ ]");
                    }
                }
                System.out.println();
            }
        }
    }
}
