package com.easy.evolutionsimulator;

import java.util.Scanner;

import static com.easy.evolutionsimulator.S.*;

public class Main {
    private Environment env;
    private Log log;

    public long startTime, finishTime;
    public double timeElapsed;
    public boolean printEnvEnabled;
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Main();
    }

    private void initializeEnvironment(int dimX, int dimY, int maxBlobs) {
        env = new Environment(this, dimX, dimY, maxBlobs);
        log = new Log(this, env);
        env.setLog(log);
        log.hasBlob = new boolean[env.dimX + 1][env.dimY + 1];
        log.hasFood = new boolean[env.dimX + 1][env.dimY + 1];
    }

    private Main() {
        int demomode;
        int dimX, dimY;
        int amount, sense, speed, size, agro, dirProb;
        int days, foodAmountToEat, runTime, pauseTime, maxBlobs, moveCount, foodAmount, foodSatiety;
        int booleanChoice;
        boolean clearEntities = false;
        boolean printEnv = true;

        p(TITLE);
        p(HELP);
        p(CONFIG);
        l();
        demomode = sc.nextInt();

        if (demomode == 1) {
            initializeEnvironment(8, 8, 0);
            env.spawnBlobs(6,3,1,30,40, 30,true);
            startSimulation(0,0,0,0,true,1,7, 15);

        } else if (demomode == 2) {
            initializeEnvironment(6, 6, 1);
            env.spawnBlobs(1,2,1,30,0, 50,true);
            startSimulation(0,0,0,800,true,1,1, 15);

        } else if (demomode == 3) {
            initializeEnvironment(6, 6, 2);
            env.spawnBlobs(1,2,1,30,0, 80,true, 1, 1);
            env.spawnBlobs(1,2,1,80,50, 80,false, 2, 2);
            env.spawnFood(1, 15, true, 1, 3);
            startSimulation(0,0,0,800,true,1,1, 15);

        } else if (demomode == 0) {
            p(DIMX); dimX = sc.nextInt();
            p(DIMY); dimY = sc.nextInt();
            p(POPLIMIT); maxBlobs = sc.nextInt();

            initializeEnvironment(dimX, dimY, maxBlobs);

            //Spawn Blobs dialog
            while (true) {
                p(SPAWNBLOBS); booleanChoice = sc.nextInt();

                if (booleanChoice == 1) {
                    p(CLEARENTITIES); booleanChoice = sc.nextInt();

                    if (booleanChoice == 1)
                        clearEntities = true;
                    else if (booleanChoice == 0)
                        clearEntities = false;

                    p(BLOBAMOUNT); amount = sc.nextInt();
                    p(SENSE); sense = sc.nextInt();
                    p(SPEED); speed = sc.nextInt();
                    p(SIZE); size = sc.nextInt();
                    p(AGRO); agro = sc.nextInt();
                    p(DIRPROB); dirProb = sc.nextInt();

                    env.spawnBlobs(amount, sense, speed, size, agro, dirProb, clearEntities);
                }
                else if (booleanChoice == 0) break;
            }

            p(DAYS);         days = sc.nextInt();
            p(FOODEATEN);    foodAmountToEat = sc.nextInt();
            p(RUNTIME);      runTime = sc.nextInt();
            p(PAUSETIME);    pauseTime = sc.nextInt();
            p(GRAPHICS);     booleanChoice = sc.nextInt(); if (booleanChoice == 0) printEnv = false;
            p(MOVESPERDAY);  moveCount = sc.nextInt();
            p(FOODAMOUNT);   foodAmount = sc.nextInt();
            p(FOODSATIETY);  foodSatiety = sc.nextInt();
            p(START);        booleanChoice = sc.nextInt();

            if (booleanChoice == 1)
                startSimulation(days, foodAmountToEat, runTime, pauseTime, printEnv, moveCount, foodAmount, foodSatiety);
            else if (booleanChoice == 0)
                System.exit(0);
        }
    }

    private void startSimulation(int days, int foodAmountToEat, int runTime, int pauseTime,
                                boolean printEnv, int moveCount, int foodAmount, int foodSatiety) {

        if (days == 0) days = Integer.MAX_VALUE;
        if (foodAmountToEat == 0) foodAmountToEat = Integer.MAX_VALUE;
        if (runTime == 0) runTime = Integer.MAX_VALUE;
        printEnvEnabled = printEnv && (env.dimX + 1) <= 80 && (env.dimY + 1) <= 80;
        startTime = System.nanoTime();

        while (log.blobAmount > 0 && log.day <= days && log.foodEaten < foodAmountToEat && timeElapsed <= runTime) {
            //Logging
            finishTime = System.nanoTime();
            timeElapsed = (double) (finishTime - startTime) / 1000000000;
            log.logEnv();
            if (printEnvEnabled) log.printEnv();

            //Simulation
            env.startDay(moveCount, foodAmount, foodSatiety);
            env.endDay();

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
        log.logEnv();
        p(EXIT);
        sc.next();
    }
}