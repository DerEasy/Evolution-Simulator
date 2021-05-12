package com.easy.evolutionsimulator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.easy.evolutionsimulator.Calc.*;
import static com.easy.evolutionsimulator.Log.*;

public class Environment {
    static ConcurrentHashMap<Integer, Blob> blobHash;
    static ConcurrentHashMap<Integer, Food> foodHash;
    static int dimX, dimY;
    static int envSize;
    static Integer id, fid;

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

    public Environment(int pDimX, int pDimY) {
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
     * Moves a single Blob entity.
     * The Blob will move randomly with maximum speed if it has not found any food nearby.
     * If its exclusion key is greater than 0, movement is restricted per the exclusion procedure.
     * Else if it has spotted a food source, it will move with appropriate speed to that food source.
     * @param blob The Blob entity to be moved
     */
    public void moveBlob(Blob blob) {
        int exKey = blob.getExKey();

        if (blob.foodX == null && exKey == 0) {
            blob.moveBlob(rng.nextInt(9), blob.speed);
        } else if (blob.foodX == null && exKey > 0) {
            blob.moveBlob(getValidDirKey(exKey), blob.speed);
        } else {
            blob.moveBlob(blob.dirFood, blob.adjustedSpeed);
        }
    }

    /**
     * Repositions all Blobs to random positions in the environment.
     */
    public void repositionBlobs() {
        Blob blob;

        for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
            blob = blobEntity.getValue();
            blob.setPosition(rng.nextInt(dimX + 1), rng.nextInt(dimY + 1));
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
        Blob blob;
        fillFood(foodCount, foodSatiety);

        for (int i = 0; i < moveCount; i++) {
            for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
                blob = blobEntity.getValue();
                logBlob(blob);

                blob.eatBlob();
                if (blob.foodX != null || blob.senseFood()) {
                    blob.calcDirectionKey();
                    moveBlob(blob);
                    if (blob.calcDirectionKey() == 0) {
                        blob.eatFood();
                        blob.foodX = null;
                        blob.foodY = null;
                    }
                } else {
                    moveBlob(blob);
                }
                if(blob.size > 33) blob.modEnergy((int) Math.round(-0.03 * blob.size));
                else blob.modEnergy(-1);
            }
        }
    }

    /**
     * Ends the day. Decides whether a Blob will reproduce or die.
     * @param maxBlobs Maximum amount of Blobs that will be allowed. 0 to disable.
     */
    public void endDay(int maxBlobs) {
        if (maxBlobs == 0) maxBlobs = Integer.MAX_VALUE;
        Blob blob;

        for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
            blob = blobEntity.getValue();
            blob.modAge(1);

            if (blob.energy <= 0) {
                blobHash.remove(blobEntity.getKey());
                blobAmount--;
                blobDeaths++;
                continue;
            } else if (blob.energy > 100) blob.energy = 100;

            if(blobAmount < maxBlobs && (day - blob.reproductionDate) >= 5 && blob.willReproduce()) {
                id++;
                blobHash.put(id, new Blob(
                        id,
                        (int) (blob.energy - (blob.energy * 0.4)),
                        blob.speed + getValidSpeedMutation(blob),
                        blob.sense + getValidSenseMutation(blob),
                        blob.size + getValidSizeMutation(),
                        blob.agro + getValidAgroMutation()));
                blobBirths++;
                blob.setReproductionDate(day);
            }
        }
    }
}
