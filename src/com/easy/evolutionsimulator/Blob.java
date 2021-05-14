package com.easy.evolutionsimulator;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.easy.evolutionsimulator.Environment.*;
import static com.easy.evolutionsimulator.Log.*;
import static com.easy.evolutionsimulator.Calc.*;
import static com.easy.evolutionsimulator.Main.printEnvEnabled;

public class Blob extends Animal {
    public int dirFood, adjustedSpeed;
    public Integer foodX, foodY;
    public static ConcurrentLinkedQueue<Integer> removeFoodQueue = new ConcurrentLinkedQueue<>();
    public static int foodQueueSize;
    public int prefDir = 0;
    public int reverseDir, tempDir;


    public Blob(int id, int energy, int speed, int sense, int size, int agro) {
        setId(id);
        setSpeed(speed);
        setSense(sense);
        setSize(size);
        setEnergy(energy);
        setAgro(agro);
        setPosition(rng.nextInt(dimX + 1), rng.nextInt(dimY + 1));
        blobAmount++;
    }

    public Blob(int id, int energy, int speed, int sense, int size, int agro, int x, int y) {
        setId(id);
        setSpeed(speed);
        setSense(sense);
        setSize(size);
        setEnergy(energy);
        setAgro(agro);
        posX = x;
        posY = y;
        blobAmount++;
    }




    //The following is the part in which the artificial intelligence of a Blob entity is defined.


    /**
     * Moves the Blob in one of the 9 possible directions with a given speed.
     * @param direction Direction key from 0 to 8 (inclusive)
     * @param speed Default or adjusted speed of the Blob
     */
    public void moveBlob(int direction, int speed) {
        /*
        1   2   3
        8   0   4
        7   6   5
        */

        if (direction < 0 || direction > 8 || speed < 1)
            throw new IllegalArgumentException("Illegal direction or speed values whilst trying to move Blob.");

        for (int i = 0; i < speed; i++) {
            switch (direction) {
                case 0:
                    break;
                case 1:
                    posX--; posY++;
                    break;
                case 2:
                            posY++;
                    break;
                case 3:
                    posX++; posY++;
                    break;
                case 4:
                    posX++;
                    break;
                case 5:
                    posX++; posY--;
                    break;
                case 6:
                            posY--;
                    break;
                case 7:
                    posX--; posY--;
                    break;
                case 8:
                    posX--;
                    break;
            }
        }

        if (direction != 0) {
            if (size < 25) modEnergy(-3);
            else modEnergy((int) Math.round(-0.12 * size));

            modEnergy(Math.round(-agro / 30));
        }

        correctPos();
        if (foodX == null) prefDir = direction;
        else prefDir = 0;
        tempDir = direction;
    }

    /**
     * The Blob will move randomly with maximum speed if it has not found any food nearby.
     * If its exclusion key is greater than 0, movement is restricted per the exclusion procedure.
     * The Blob will generally prefer to walk to the same direction it was walking before.
     * Else if it has spotted a food source, it will move with appropriate speed to that food source.
     */
    public void decideMovement() {
        int exKey = getExKey();
        int freeDir = -1;
        int exDir = -1;

        setReverseDirectionKey();

        if (exKey == 0) {
            do {
                freeDir = rng.nextInt(25);
            } while (freeDir == reverseDir);
        } else {
            do {
                exDir = getValidDirKey(exKey);
            } while (exDir == reverseDir);
        }

        if (foodX != null) {
            moveBlob(dirFood, adjustedSpeed);
        }
        else if (exKey == 0 && prefDir == 0) {
            moveBlob(freeMovement[freeDir], speed);
        }
        else if (exKey == 0 && prefDir > 0) {
            if (calcProb(dirProb)) moveBlob(prefDir, speed);
            else moveBlob(freeMovement[freeDir], speed);
        }
        else if (exKey > 0 && prefDir == 0) {
            moveBlob(exDir, speed);
        }
        else if (exKey > 0 && prefDir > 0) {
            boolean prefDirAvailable = false;
            int[] dirCheck = getValidDirArray(exKey);

            for (int j : dirCheck) {
                if (j == prefDir) {
                    prefDirAvailable = true;
                    break;
                }
            }

            if (prefDirAvailable && calcProb(dirProb)) moveBlob(prefDir, speed);
            else moveBlob(exDir, speed);
        }
        setReverseDirectionKey();
    }

    public void setReverseDirectionKey() {
        if (prefDir != 0 && prefDir <= 4) reverseDir = prefDir + 4;
        else if (prefDir != 0) reverseDir = prefDir - 4;
        else if (tempDir <= 4) reverseDir = tempDir + 4;
        else reverseDir = tempDir - 4;
    }

    /**
     * Scans the area for a food source and marks its position if it has been found.
     * "Area" is defined by the sense property of the Blob. The Blob will try to
     * choose the nearest food source possible.
     * @return True if nearby food has been located.
     */
    public boolean senseFood() {
        int foodX, foodY;
        int fillIndex = -1;
        Food[] availableFood = new Food[10];

        for (Map.Entry<Integer, Food> foodEntity : foodHash.entrySet()) {
            foodX = foodEntity.getValue().posX;
            foodY = foodEntity.getValue().posY;

            if (foodX - sense <= posX && foodX + sense >= posX &&
                foodY - sense <= posY && foodY + sense >= posY) {
                fillIndex++;
                availableFood[fillIndex] = foodEntity.getValue();
            } if (fillIndex == 9) break;
        }

        if (fillIndex > 0) {
            int tempX = Math.abs(availableFood[0].posX - posX);
            int tempY = Math.abs(availableFood[0].posY - posY);
            int compX, compY;
            int index = 0;

            for (int i = 0; i < fillIndex; i++) {
                compX = Math.abs(availableFood[(i + 1)].posX - posX);
                compY = Math.abs(availableFood[(i + 1)].posY - posY);
                if ((compX < tempX && compY <= tempY) ||
                    (compX <= tempX && compY < tempY)) {
                    tempX = compX;
                    tempY = compY;
                    index = i + 1;
                }
            }

            this.foodX = availableFood[index].posX;
            this.foodY = availableFood[index].posY;
            return true;
        } else if (fillIndex == 0) {
            this.foodX = availableFood[0].posX;
            this.foodY = availableFood[0].posY;
            return true;
        }

        this.foodX = null;
        this.foodY = null;
        return false;
    }

    /**
     * Makes the Blob eat all food entities found in a block until it is full (Energy >= 100).
     * If Blob is on a block with food on it, the method returns true.
     * @return True if on a block with food on it
     */
    public boolean eatFood() {
        boolean onFoodBlock = false;
        if (foodX == null || foodY == null) return false;

        for (Map.Entry<Integer, Food> foodEntity : foodHash.entrySet()) {
            if (foodEntity.getValue().available &&
                    foodEntity.getValue().posX == foodX &&
                    foodEntity.getValue().posY == foodY &&
                    posX == foodX &&
                    posY == foodY) {
                
                modEnergy(foodEntity.getValue().satiety);
                foodAmount--;
                foodEaten++;
                onFoodBlock = true;

                if (printEnvEnabled) {
                    //removeFoodList.add(foodEntity.getKey());
                    removeFoodQueue.add(foodEntity.getKey());
                    foodQueueSize++;
                    foodEntity.getValue().available = false;
                } else foodHash.remove(foodEntity.getKey());

                if (energy >= 100) {
                    setEnergy(100);
                    return true;
                }
            }
        }
        adjustedSpeed = speed;
        return onFoodBlock;
    }

    /**
     * If the Blob meets another Blob on the same block, it will eat it if
     * it's 20% bigger than it. A Blob can eat 1 other Blob at most in one go.
     * @return True if a Blob has been eaten
     */
    public boolean eatBlob() {
        if (size < 8) return false;
        Blob blob;
        int blobsEaten = 0;

        for (Map.Entry<Integer, Blob> blobEntity : blobHash.entrySet()) {
            blob = blobEntity.getValue();

            if (energy >= 100 && blobsEaten > 0) return true;
            if (energy >= 100 && blobsEaten == 0) return false;

            if (blob.posX == posX && blob.posY == posY && blobsEaten <= 1 &&
                    blob.age >= 10 && size >= blob.size + (blob.size * 0.2)) {

                int minSizeDiff = (int) (size - (blob.size + (blob.size * 0.2)));
                int agroDiff = agro - blob.agro;
                int eatProb = 50 + agroDiff;

                if (calcProb((eatProb + minSizeDiff))) {
                    modEnergy((blob.size / 8));
                    modSize(1);
                    blobHash.remove(blobEntity.getKey());
                    blobDeaths++;
                    blobsEaten++;
                    blobAmount--;
                    Log.blobsEaten++;
                } else {
                    modEnergy(-(blob.size / 6));
                    blob.modEnergy(-minSizeDiff);
                    if (rng.nextBoolean()) blob.modAgro(1);
                    blobsDefeated++;
                }
            }
        }
        growOrShrink();
        return blobsEaten > 0;
    }

    public void growOrShrink() {
        if (energy < (size * 0.8)) {
            modSize((int) -(size * 0.1));
        } else if (energy > (size * 0.2)) {
            modSize(1);
        }
        if (age >= 35) {
            modSize((int) -(size * 0.1));
            modEnergy(-3);
        }
    }

    /**
     * Calculates the direction key of the next food source and sets the adjusted speed accordingly.
     * @return The direction key
     */
    public int calcDirectionKey() {
        /*
        1   2   3
        8   0   4
        7   6   5
        */

        if (foodX == null || foodY == null) return 0;

        int tempX, tempY;
        tempX = foodX - posX;
        tempY = foodY - posY;

        if      (tempX == 0 && tempY == 0)  dirFood = 0;
        else if (tempX < 0 && tempY > 0)    dirFood = 1;
        else if (tempX == 0 && tempY > 0)   dirFood = 2;
        else if (tempX > 0 && tempY > 0)    dirFood = 3;
        else if (tempX > 0 && tempY == 0)   dirFood = 4;
        else if (tempX > 0 && tempY < 0)    dirFood = 5;
        else if (tempX == 0 && tempY < 0)   dirFood = 6;
        else if (tempX < 0 && tempY < 0)    dirFood = 7;
        else if (tempX < 0 && tempY == 0)   dirFood = 8;

        tempX = Math.abs(tempX);
        tempY = Math.abs(tempY);
        if (speed == 1) adjustedSpeed = 1;
        else calcAdjustedSpeed(tempX, tempY);
        return dirFood;
    }

    /**
     * Calculates the adjusted speed value by checking where food is located.
     * @param tempX The absolute x value determined by calcDirectionKey()
     * @param tempY The absolute y value determined by calcDirectionKey()
     * @return The adjusted speed
     */
    public int calcAdjustedSpeed(int tempX, int tempY) {
        if (tempX >= tempY) {
            if (tempX <= speed && tempX > 0) adjustedSpeed = tempX;
            else adjustedSpeed = speed;
        } else {
            if (tempY <= speed && tempY > 0) adjustedSpeed = tempY;
            else adjustedSpeed = speed;
        }
        return adjustedSpeed;
    }

    /**
     * Corrects the Blob's position if it wandered outside of environment boundaries.
     * @return True if Blob was outside of boundaries
     */
    public boolean correctPos() {
        boolean isOutside = false;
        if (posX > dimX) {
            setPosX(dimX);
            isOutside = true;
        } if (posY > dimY) {
            setPosY(dimY);
            isOutside = true;
        } if (posX < 0) {
            setPosX(0);
            isOutside = true;
        } if (posY < 0) {
            setPosY(0);
            isOutside = true;
        }
        return isOutside;
    }

    /**
     * Gets the exclusion key.
     * @return Exclusion key from 0 to 8 (inclusive)
     */
    public int getExKey() {
       /*
        1   2   3
        8   0   4
        7   6   5
       */

        correctPos();

        //free movement
        if (posX < dimX && posX > 0 &&
               posY < dimY && posY > 0)                 return 0;
        //topLeft
        if (posX == 0 && posY == dimY)                  return 1;
        //top
        if (posX < dimX && posX > 0 && posY == dimY)    return 2;
        //topRight
        if (posX == dimX && posY == dimY)               return 3;
        //right
        if (posX == dimX && posY < dimY && posY > 0)    return 4;
        //bottomRight
        if (posX == dimX && posY == 0)                  return 5;
        //bottom
        if (posX < dimX && posX > 0 && posY == 0)       return 6;
        //bottomLeft
        if (posX == 0 && posY == 0)                     return 7;
        //left
        if (posX == 0 && posY < dimY && posY > 0)       return 8;

        throw new IndexOutOfBoundsException("Blob: Exclusion key is out of range.");
    }

    public void decideUponDeath() {
        if (energy <= 0) {
            blobHash.remove(id);
            blobAmount--;
            blobDeaths++;
        } else if (energy > 100) setEnergy(100);
    }

    public void tryReproduction() {
        if (blobAmount < maxBlobs && (day - reproductionDate) >= 5 && willReproduce()) {
            Environment.id++;
            blobHash.put(Environment.id, new Blob(
                    Environment.id,
                    (int) (energy - (energy * 0.4)),
                    speed + getValidSpeedMutation(this),
                    sense + getValidSenseMutation(this),
                    size + getValidSizeMutation(),
                    agro + getValidAgroMutation()));
            blobBirths++;
            setReproductionDate(day);
        }
    }

    public void dailyRoutine() {
        logBlob(this);

        eatBlob();
        if (foodX != null || senseFood()) {
            calcDirectionKey();
            decideMovement();
            if (calcDirectionKey() == 0) {
                eatFood();
                foodX = null;
                foodY = null;
            }
        } else {
            decideMovement();
        }
        if(size > 33) modEnergy((int) Math.round(-0.03 * size));
        else modEnergy(-1);
    }
}
