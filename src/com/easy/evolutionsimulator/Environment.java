package com.easy.evolutionsimulator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.easy.evolutionsimulator.Calc.rng;
import static com.easy.evolutionsimulator.Log.*;

public class Environment {
    static ConcurrentHashMap<Integer, Blob> blobHash;
    static ConcurrentHashMap<Integer, Food> foodHash;
    static int dimX, dimY;
    static int envSize;
    static Integer id, fid;
    static int maxBlobs;

    static class Food {
        int satiety, posX, posY;
        Integer fid;
        boolean available = true;

        public Food() {
            setSatiety(15);
            setPosition(rng.nextInt(dimX + 1), rng.nextInt(dimY + 1));
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(int posX, int posY) {
            setSatiety(15);
            setPosition(posX, posY);
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(int satietyLevel) {
            setSatiety(satietyLevel);
            setPosition(rng.nextInt(dimX + 1), rng.nextInt(dimY + 1));
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(int satietyLevel, int posX, int posY) {
            setSatiety(satietyLevel);
            setPosition(posX, posY);
            setFID(Environment.fid);
            foodAmount++;
        }

        public void setFID(Integer fid) {
            this.fid = fid;
        }

        public void setPosition(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public void setSatiety(int satiety) {
            this.satiety = satiety;
        }
    }

    public Environment(int pDimX, int pDimY, int maxBlobs) {
        if (maxBlobs == 0) Environment.maxBlobs = Integer.MAX_VALUE;
        else Environment.maxBlobs = maxBlobs;
        dimX = pDimX - 1;
        dimY = pDimY - 1;
        envSize = pDimX * pDimY;
        blobHash = new ConcurrentHashMap<>();
        foodHash = new ConcurrentHashMap<>();
        id = 0;
        fid = 0;
    }

    /**
     * Fills the list of food entities until its size matches with the given amount.
     * @param amount Amount of food that should be in the environment in total
     * @param foodSatiety Type of food
     */
    public void fillFood(int amount, int foodSatiety) {
        while (foodAmount < amount) {
            if (foodSatiety == 0) foodHash.put(fid, new Food());
            else foodHash.put(fid, new Food(foodSatiety));
            fid++;
        }
    }

    /**
     * Spawns a certain amount of food of a specific satiety.
     * @param amount Amount of food to be created
     * @param foodSatiety The food type you want to spawn
     * @param clearEntities True to clear the list of all Food entities
     */
    public void spawnFood(int amount, int foodSatiety, boolean clearEntities) {
        if (clearEntities) {
            foodHash.clear();
            fid = 0;
        }

        for (int i = 0; i < amount; i++) {
            if (foodSatiety == 0) foodHash.put(fid, new Food());
            else foodHash.put(fid, new Food(foodSatiety));
            fid++;
        }
    }

    public void spawnFood(int amount, int foodSatiety, boolean clearEntities, int posX, int posY) {
        if (clearEntities) {
            foodHash.clear();
            fid = 0;
        }

        for (int i = 0; i < amount; i++) {
            if (foodSatiety == 0) foodHash.put(fid, new Food(posX, posY));
            else foodHash.put(fid, new Food(foodSatiety, posX, posY));
            fid++;
        }
    }

    /**
     * Spawns a certain amount of Blobs of a specific species.
     * @param amount Amount of Blobs to be created
     * @param clearEntities True to clear the list of all Blob entities
     */
    public void spawnBlobs(int amount, int sense, int speed, int size, int agro, boolean clearEntities) {
        if (clearEntities) {
            blobHash.clear();
            id = 0;
        }

        for (int i = 0; i < amount; i++) {
            blobHash.put(id, new Blob(id, 60, speed, sense, size, agro));
            id++;
        }
    }

    public void spawnBlobs(int amount, int sense, int speed, int size, int agro, boolean clearEntities, int posX, int posY) {
        if (clearEntities) {
            blobHash.clear();
            id = 0;
        }

        for (int i = 0; i < amount; i++) {
            blobHash.put(id, new Blob(id, 60, speed, sense, size, agro, posX, posY));
            id++;
        }
    }

    /**
     * Starts a new day the Blobs must survive.
     * @param moveCount Amount of moves a Blob will do
     * @param foodCount Amount of food entities in the environment in total
     * @param foodSatiety Satiety level of food
     */
    public void startDay(int moveCount, int foodCount, int foodSatiety) {
        day++;
        fillFood(foodCount, foodSatiety);

        for (int i = 0; i < moveCount; i++) {
            for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
                blobEntity.getValue().dailyRoutine();
            }
        }
    }

    /**
     * Ends the day. Decides whether a Blob will reproduce or die.
     */
    public void endDay() {
        Blob blob;

        for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
            blob = blobEntity.getValue();
            blob.modAge(1);
            blob.decideUponDeath();
            blob.tryReproduction();
        }
    }
}
