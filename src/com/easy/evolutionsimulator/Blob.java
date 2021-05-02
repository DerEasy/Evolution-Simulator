package com.easy.evolutionsimulator;

import static com.easy.evolutionsimulator.Environment.*;

public class Blob extends Animal {
    public int posX, posY, dirFood, adjustedSpeed;
    public Integer foodX, foodY;

    public Blob(String species) {
        switch (species) {
            case "default" -> defaultBlob();
        }
    }

    public Blob(Integer id, Integer energy, Integer speed, Integer sense, Integer strength, Integer size, Integer agro) {
        setId(id);
        setSpeed(speed);
        setStrength(strength);
        setSense(sense);
        setSize(size);
        setEnergy(energy);
        setAgro(agro);
    }

    /**
     * Sets default minimal properties of a Blob.
     */
    public void defaultBlob() {
        setId(Environment.id);
        setEnergy(60); //Energy in percent
        setSense(3); //Radius a Blob can scavenge/scan - in blocks (applies for diagonals as well). Amount of blocks: (2x + 1)²
        setSpeed(2); //Amount of blocks a Blob can pass in one go
    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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

        if (direction != 0) modEnergy(-5);
        correctPos();
    }

    /**
     * Scans the area for a food source and marks its position if it has been found.
     * "Area" is defined by the sense property of the Blob.
     * @return True if nearby food has been located.
     */
    public boolean senseFood() {
        int foodX, foodY;

        for (Food foodEntity : foodEntities) {
            foodX = foodEntity.posX;
            foodY = foodEntity.posY;

            if (foodX - sense <= posX && foodX + sense >= posX &&
                foodY - sense <= posY && foodY + sense >= posY) {
                this.foodX = foodX;
                this.foodY = foodY;
                return true;
            }
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
        int foodAmount = foodEntities.size();
        boolean onFoodBlock = false;

        for (int i = 0; i < foodAmount; i++) {
            if (foodEntities.get(i).posX == foodX && foodEntities.get(i).posY == foodY &&
                posX == foodX && posY == foodY) {
                energy += foodEntities.get(i).satiety;
                //noinspection SuspiciousListRemoveInLoop
                foodEntities.remove(i);
                foodAmount--;
                foodEaten++;
                onFoodBlock = true;

                if (energy >= 100) {
                    energy = 100;
                    return true;
                }
            }
        }
        adjustedSpeed = speed;
        return onFoodBlock;
    }

    /**
     * Calculates the direction key of the next food source and sets the adjusted speed accordingly.
     */
    public void calcDirectionKey() {
        /*
        1   2   3
        8   0   4
        7   6   5
        */

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
        if (tempX >= tempY) {
            if (tempX <= speed && tempX > 0) adjustedSpeed = tempX;
            else adjustedSpeed = speed;
        } else {
            if (tempY <= speed && tempY > 0) adjustedSpeed = tempY;
            else adjustedSpeed = speed;
        }
    }

    /**
     * Corrects the Blob's position if it wandered outside of environment boundaries.
     */
    public void correctPos() {
        if (posX > dimX) setPosX(dimX);
        if (posY > dimY) setPosY(dimY);
        if (posX < 0) setPosX(0);
        if (posY < 0) setPosY(0);
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
}
