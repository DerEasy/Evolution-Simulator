package com.easy.evolutionsimulator;

import java.util.Scanner;

import static com.easy.evolutionsimulator.Environment.dimX;
import static com.easy.evolutionsimulator.Environment.dimY;
import static com.easy.evolutionsimulator.Log.*;

public class Main {
    static Environment envLogic;
    static boolean[][] hasBlob;
    static boolean[][] hasFood;
    static long startTime, finishTime;
    static double timeElapsed;
    static boolean printEnvEnabled;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int demomode;
        int dimX, dimY;
        int amount, sense, speed, size, agro;
        int days, foodAmountToEat, runTime, pauseTime, maxBlobs, moveCount, foodAmount, foodSatiety;
        int booleanChoice;
        boolean clearEntities = false;
        boolean printEnv = true;

        System.out.println("\nEvolution Simulator\n");
        System.out.println("Help:\nYou will want to spawn at least one type of Blob in the environment.\n" +
                "The dimensions of the environment should not be extremely large to avoid performance problems.\n" +
                "Optimal dimension values normally have two or three digits. For demonstration purposes, one digit is good.\n\n" +
                "Do not create an overabundance of food entities. Adjust the amount to a reasonable value according\n to the total amount of blocks in the environment.\n" +
                "If you increase the amount of moves that Blobs will do in a day, the amount of food must also be\n increased to level out the increased energy consumption\n" +
                "It is most reasonable to keep size and agro values somewhere at the lower two-digit numbers.\n" +
                "Sense and speed values should be kept very low in accordance to the dimensions.\n\n" +
                "Legend for the visual representation system:\no stands for a/multiple Blob entities\nx stands for a/multiple food entities\n" +
                "i is displayed whenever a/multiple Blob entities stand on the same block as a/multiple food entities.\n" +
                "The environment will not be printed if either the x or y dimension value exceeds 80.\n\n\n\n");

        System.out.println("Do you want to configure the simulator (0) or run demo mode (1) or dev mode (2)? 0/1/2:");
        demomode = sc.nextInt();
        if (demomode == 1) {
            envLogic = new Environment(8,8, 0);
            envLogic.spawnBlobs(6,3,1,30,40,true);
            hasBlob = new boolean[Environment.dimX + 1][Environment.dimY + 1];
            hasFood = new boolean[Environment.dimX + 1][Environment.dimY + 1];
            startSimulation(0,0,0,0,true,1,7, 15);

        } else if (demomode == 2){
            envLogic = new Environment(6,6, 1);
            envLogic.spawnBlobs(1,2,1,30,0,true);
            hasBlob = new boolean[Environment.dimX + 1][Environment.dimY + 1];
            hasFood = new boolean[Environment.dimX + 1][Environment.dimY + 1];
            startSimulation(0,0,0,0,true,1,3, 15);

        } else if (demomode == 0) {
            System.out.println("Set the size of the x dimension of the environment:");
            dimX = sc.nextInt();
            System.out.println("Set the size of the y dimension of the environment:");
            dimY = sc.nextInt();
            System.out.println("How many Blobs may be in the environment at once? 0 to disable population limit:");
            maxBlobs = sc.nextInt();

            envLogic = new Environment(dimX, dimY, maxBlobs);
            hasBlob = new boolean[Environment.dimX + 1][Environment.dimY + 1];
            hasFood = new boolean[Environment.dimX + 1][Environment.dimY + 1];

            while (true) {
                System.out.println("Spawn (additional) Blobs? 0/1:");
                booleanChoice = sc.nextInt();
                if (booleanChoice == 1) {
                    System.out.println("Clear all previously created entities? 0/1:");
                    booleanChoice = sc.nextInt();
                    if (booleanChoice == 1) clearEntities = true;
                    else if (booleanChoice == 0) clearEntities = false;
                    System.out.println("Amount of Blobs to be spawned:");
                    amount = sc.nextInt();
                    System.out.println("Sense value (Keep it very low, but nonzero):");
                    sense = sc.nextInt();
                    System.out.println("Speed value (Keep it very low, but nonzero):");
                    speed = sc.nextInt();
                    System.out.println("Size value (1 - 100):");
                    size = sc.nextInt();
                    System.out.println("Agro value (0 - 100):");
                    agro = sc.nextInt();

                    envLogic.spawnBlobs(amount, sense, speed, size, agro, clearEntities);
                } else if (booleanChoice == 0) break;
            }

            System.out.println("How many days shall be simulated? 0 to continue running indefinitely:");
            days = sc.nextInt();
            System.out.println("At what number of food that has been eaten shall the simulation stop? 0 to disable:");
            foodAmountToEat = sc.nextInt();
            System.out.println("How long in seconds shall the simulation run? 0 to continue running indefinitely:");
            runTime = sc.nextInt();
            System.out.println("For how long in ms do you want to pause execution after a day has been simulated? 0 to disable:");
            pauseTime = sc.nextInt();
            System.out.println("Do you want to print a visual representation of the environment in the console? 0/1:");
            booleanChoice = sc.nextInt();
            if (booleanChoice == 1) printEnv = true;
            else if (booleanChoice == 0) printEnv = false;
            System.out.println("How many moves shall the Blobs do in one day? Default is 1:");
            moveCount = sc.nextInt();
            System.out.println("How many food entities shall be available everyday?");
            foodAmount = sc.nextInt();
            System.out.println("Food satiety (Amount of energy Blobs will get if they eat food)? Default is 15:");
            foodSatiety = sc.nextInt();
            System.out.println("Ready. Start simulation? 0/1:");
            booleanChoice = sc.nextInt();

            if (booleanChoice == 1) {
                //sc.close();
                startSimulation(days, foodAmountToEat, runTime, pauseTime, printEnv, moveCount, foodAmount, foodSatiety);
            } else if (booleanChoice == 0) System.exit(0);
        }
    }

    public static void startSimulation(int days, int foodAmountToEat, int runTime, int pauseTime,
                                boolean printEnv, int moveCount, int foodAmount, int foodSatiety) {

        if (days == 0) days = Integer.MAX_VALUE;
        if (foodAmountToEat == 0) foodAmountToEat = Integer.MAX_VALUE;
        if (runTime == 0) runTime = Integer.MAX_VALUE;
        printEnvEnabled = printEnv && (dimX + 1) <= 80 && (dimY + 1) <= 80;
        startTime = System.nanoTime();

        while (blobAmount > 0 && day <= days && foodEaten < foodAmountToEat && timeElapsed <= runTime) {
            //Logging
            finishTime = System.nanoTime();
            timeElapsed = (double) (finishTime - startTime) / 1000000000;
            logEnv();
            if (printEnvEnabled) printEnv();

            //Simulation
            envLogic.startDay(moveCount, foodAmount, foodSatiety);
            envLogic.endDay();

            if (pauseTime > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(pauseTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //Output last data that was made available at the end
        logEnv();
        System.out.println("\nEnter anything to exit.");
        sc.next();
    }
}