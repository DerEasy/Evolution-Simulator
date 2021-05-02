package com.easy.evolutionsimulator;

import java.util.LinkedList;

public class Environment {
    static LinkedList<Blob> blobEntities;
    static LinkedList<Food> foodEntities;
    static int dimX, dimY;
    static int envSize, id;
    static int foodEaten, blobDeaths, blobBirths;

    public static void log() {
        System.out.printf("\n\n\nEnvDimensions: %s x %s\tEnvSize: %s%n", dimX + 1, dimY + 1, envSize);
        System.out.printf("Food count: %s\tFood eaten: %s%n", foodEntities.size(), foodEaten);
        System.out.printf("Blob count: %s\tBlob deaths: %s\tBlob births: %s\n%n", blobEntities.size(), blobDeaths, blobBirths);

        /*Blob blob;
        for (int i = 0; i < blobEntities.size(); i++) {
            blob = blobEntities.get(i);
            System.out.println(String.format("Blob %s\tx: %s\ty: %s\tEnergy: %s\tFoodDir: %s\tx: %s\ty: %s",
                    i, blob.posX, blob.posY, blob.energy, blob.dirFood, blob.foodX, blob.foodY));
        }*/
    }

    static class Food {
        int satiety, posX, posY;

        public Food() {
            satietyDefault();
        }

        public Food(String satietyLevel) {
            switch (satietyLevel) {
                case "default" -> satietyDefault();
                case "low" -> satietyLow();
                case "high" -> satietyHigh();
            }
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
        blobEntities = new LinkedList<>();
        foodEntities = new LinkedList<>();
    }

    /**
     * Spawns food. Fills the list of food entities until its size matches with the given amount.
     * @param amount Amount of food that should be in the environment in total
     * @param foodType Type of food
     */
    public void spawnFood(int amount, String foodType) {
        while (foodEntities.size() < amount) {
            int posX = Calc.rng.nextInt(dimX + 1);
            int posY = Calc.rng.nextInt(dimY + 1);
            if (foodType == null) foodEntities.add(new Food());
            else foodEntities.add(new Food(foodType));
            foodEntities.getLast().setPosition(posX, posY);
        }
    }

    /**
     * Clears the list of all Blob entities and fills it with new ones.
     * @param amount Amount of Blobs to be created
     */
    public void spawnBlobs(int amount) {
        blobEntities.clear();
        int posX, posY;
        id = 0;

        for (int i = 0; i < amount; i++) {
            posX = Calc.rng.nextInt(dimX + 1);
            posY = Calc.rng.nextInt(dimY + 1);
            blobEntities.add(new Blob("default"));
            blobEntities.getLast().setPosition(posX, posY);
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
                blob.moveBlob(Calc.rng.nextInt(9), blob.speed);
            } else if (blob.foodX == null && exKey > 0) {
                blob.moveBlob(Calc.getValidDirKey(exKey), blob.speed);
            } else {
                blob.moveBlob(blob.dirFood, blob.adjustedSpeed);
            }
    }

    /**
     * Repositions all Blobs to random positions in the environment.
     */
    public void repositionBlobs() {
        int posX, posY;

        for (Blob blobEntity : blobEntities) {
            posX = Calc.rng.nextInt(dimX + 1);
            posY = Calc.rng.nextInt(dimY + 1);
            blobEntity.setPosX(posX);
            blobEntity.setPosY(posY);
        }
    }

    /**
     * Starts a new day the Blobs must survive.
     * @param moveCount Amount of moves a Blob will do
     * @param foodCount Amount of food entities in the environment in total
     * @param foodType Type of food
     */
    public void startDay(int moveCount, int foodCount, String foodType) {
        Blob blob;

        spawnFood(foodCount, foodType);
        for (int i = 0; i < moveCount; i++) {
            for (Blob blobEntity : blobEntities) {
                blob = blobEntity;

                if (blob.senseFood()) {
                    blob.calcDirectionKey();
                    moveBlob(blob);
                    blob.eatFood();
                } else {
                    moveBlob(blob);
                }

                System.out.printf("BlobID(%s)\tx: %s\ty: %s\tEnergy: %s\tFoodDir: %s\tx: %s\ty: %s\tAdjSpeed: %s\tExKey: %s\n",
                        blob.id, blob.posX, blob.posY, blob.energy, blob.dirFood, blob.foodX, blob.foodY, blob.adjustedSpeed, blob.getExKey());

                blob.modEnergy(- 1);
            }
        }
    }

    /**
     * Ends the day. Decides whether a Blob will reproduce or die.
     * @param maxBlobs Maximum amount of Blobs that will be allowed. 0 to disable.
     */
    public void endDay(int maxBlobs) {
        if (maxBlobs == 0) maxBlobs = Integer.MAX_VALUE;
        int blobAmount = blobEntities.size();
        Blob blob;

        for (int i = 0; i < blobAmount; i++) {
            blob = blobEntities.get(i);
            blob.modAge(1);

            if (blob.energy <= 0) {
                //noinspection SuspiciousListRemoveInLoop
                blobEntities.remove(i);
                blobAmount--;
                blobDeaths++;
                continue;
            } else if (blob.energy > 100) blob.energy = 100;

            while(blobAmount > maxBlobs) {
                blobEntities.removeLast();
                blobAmount--;
            }

            if(blobAmount < maxBlobs && blob.willReproduce()) {
                id++;
                blobEntities.add(new Blob(
                        id,
                        60,
                        blob.speed,
                        blob.sense,
                        blob.strength,
                        blob.size,
                        blob.agro));
                blobEntities.getLast().setPosX(Calc.rng.nextInt(dimX + 1));
                blobEntities.getLast().setPosY(Calc.rng.nextInt(dimY + 1));
                blobBirths++;
                blobAmount++;
            }
        }
    }
}
