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
            satietyDefault();
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(int posX, int posY) {
            satietyDefault();
            this.posX = posX;
            this.posY = posY;
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(String satietyLevel) {
            switch (satietyLevel) {
                case "default" -> satietyDefault();
                case "low" -> satietyLow();
                case "high" -> satietyHigh();
            }
            setFID(Environment.fid);
            foodAmount++;
        }

        public Food(String satietyLevel, int posX, int posY) {
            switch (satietyLevel) {
                case "default" -> satietyDefault();
                case "low" -> satietyLow();
                case "high" -> satietyHigh();
            }
            this.posX = posX;
            this.posY = posY;
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

        public void setPosX(int posX) { this.posX = posX; }
        public void setPosY(int posY) { this.posY = posY; }

        public void satietyDefault() {
            satiety = 15;
        }

        public void satietyLow() {
            satiety = 10;
        }

        public void satietyHigh() {
            satiety = 20;
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
     * @param foodType Type of food
     */
    public void fillFood(int amount, String foodType) {
        int posX, posY;

        while (foodAmount < amount) {
            posX = rng.nextInt(dimX + 1);
            posY = rng.nextInt(dimY + 1);
            if (foodType == null) foodHash.put(fid, new Food(posX, posY));
            else foodHash.put(fid, new Food(foodType, posX, posY));
            fid++;
        }
    }

    /**
     * Spawns a certain amount of food of a specific food type.
     * @param amount Amount of food to be created
     * @param foodType The food type you want to spawn
     * @param clearEntities True to clear the list of all Food entities
     */
    public void spawnFood(int amount, String foodType, boolean clearEntities) {
        if (clearEntities) {
            foodHash.clear();
            fid = 0;
        }
        int posX, posY;

        for (int i = 0; i < amount; i++) {
            posX = rng.nextInt(dimX + 1);
            posY = rng.nextInt(dimY + 1);
            if (foodType == null) foodHash.put(fid, new Food(posX, posY));
            else foodHash.put(fid, new Food(foodType, posX, posY));
            fid++;
        }
    }

    public void spawnFood(int amount, String foodType, boolean clearEntities, int posX, int posY) {
        if (clearEntities) {
            foodHash.clear();
            fid = 0;
        }

        for (int i = 0; i < amount; i++) {
            if (foodType == null) foodHash.put(fid, new Food(posX, posY));
            else foodHash.put(fid, new Food(foodType, posX, posY));
            fid++;
        }
    }

    /**
     * Spawns a certain amount of Blobs of a specific species.
     * @param amount Amount of Blobs to be created
     * @param species The species you want to spawn
     * @param clearEntities True to clear the list of all Blob entities
     */
    public void spawnBlobs(int amount, String species, boolean clearEntities) {
        if (clearEntities) {
            blobHash.clear();
            id = 0;
        }
        int posX, posY;

        for (int i = 0; i < amount; i++) {
            posX = rng.nextInt(dimX + 1);
            posY = rng.nextInt(dimY + 1);
            blobHash.put(id, new Blob(species, posX, posY));
            id++;
        }
    }

    public void spawnBlobs(int amount, String species, boolean clearEntities, int posX, int posY) {
        if (clearEntities) {
            blobHash.clear();
            id = 0;
        }

        for (int i = 0; i < amount; i++) {
            blobHash.put(id, new Blob(species, posX, posY));
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
            blob.setPosX(rng.nextInt(dimX + 1));
            blob.setPosY(rng.nextInt(dimY + 1));
        }
    }

    /**
     * Starts a new day the Blobs must survive.
     * @param moveCount Amount of moves a Blob will do
     * @param foodCount Amount of food entities in the environment in total
     * @param foodType Type of food
     */
    public void startDay(int moveCount, int foodCount, String foodType) {
        day++;
        Blob blob;
        fillFood(foodCount, foodType);
        //spawnBlobs(2, "small", false);

        for (int i = 0; i < moveCount; i++) {
            for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
                blob = blobEntity.getValue();
                if (blob.size > 100) blob.setSize(100);
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

            if(blobAmount < maxBlobs && blob.willReproduce()) {
                id++;
                blobHash.put(id, new Blob(
                        id,
                        60,
                        blob.speed,
                        blob.sense,
                        blob.strength,
                        blob.size + getValidSizeMutation(),
                        blob.agro + getValidAgroMutation(),
                        rng.nextInt(dimX + 1),
                        rng.nextInt(dimX + 1)));
                blobBirths++;
            }
        }
    }
}
