package com.easy.evolutionsimulator;

import java.util.ArrayList;
import java.util.Map;

import static com.easy.evolutionsimulator.Calc.*;

@SuppressWarnings("UnusedReturnValue")
public class Blob extends Animal {
    private final Main main;
    private final Environment env;
    private final Log log;

    public Integer foodX, foodY;
    public int prefDir, lastDir, defaultDirProb;
    private int dirFood, adjustedSpeed;
    private boolean panicmode;

    public Blob(Main main, Environment env, Log log, int id, int energy, int speed, int sense, int size, int agro, int dirProb) {
        super(env);
        this.main = main;
        this.env = env;
        this.log = log;
        setId(id);
        setSpeed(speed);
        setSense(sense);
        setSize(size);
        setEnergy(energy);
        setAgro(agro);
        setDirProb(dirProb);
        setPosition(rng.nextInt(env.dimX + 1), rng.nextInt(env.dimY + 1));
        defaultDirProb = dirProb;
        log.blobAmount++;
    }

    public Blob(Main main, Environment env, Log log, int id, int energy, int speed, int sense, int size, int agro, int dirProb, int x, int y) {
        super(env);
        this.main = main;
        this.env = env;
        this.log = log;
        setId(id);
        setSpeed(speed);
        setSense(sense);
        setSize(size);
        setEnergy(energy);
        setAgro(agro);
        setDirProb(dirProb);
        setPosX(x);
        setPosY(y);
        defaultDirProb = dirProb;
        log.blobAmount++;
    }




    //The following is the part in which the artificial intelligence of a Blob entity is defined.


    /**
     * Moves the Blob in one of the 9 possible directions with a given speed.
     * @param direction Direction key from 0 to 8 (inclusive)
     * @param speed Default or adjusted speed of the Blob
     */
    private void moveBlob(int direction, int speed) {
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
                default:
                    throw new IllegalArgumentException("Illegal direction value whilst trying to move Blob.");
            }
        }
        correctPos();
        movementEnergyLoss(direction, speed);
        directionLogging(direction);
    }

    private void movementEnergyLoss(int direction, int speed) {
        if (direction != 0 && !(speed < 1)) {
            if (size < 25)
                modEnergy(-3);
            else
                modEnergy((int) Math.round(-0.12 * size));

            modEnergy(Math.round(-agro / 30f));
        }
    }

    private void directionLogging(int direction) {
        lastDir = direction;
        if (foodX == null) prefDir = direction;
        else prefDir = 0;
    }

    /**
     * The Blob will move randomly with maximum speed if it has not found any food nearby.
     * If its exclusion key is greater than 0, movement is restricted per the exclusion procedure.
     * The Blob will generally prefer to walk to the same direction it was walking before.
     * Else if it has spotted a food source, it will move with appropriate speed to that food source.
     */
    private void decideMovement() {
        int exKey = getExKey();
        int freeDir = -1;
        int exDir = -1;
        int reverseDir = setReverseDirectionKey();

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

            if (prefDirAvailable && calcProb(dirProb))
                moveBlob(prefDir, speed);
            else
                moveBlob(exDir, speed);
        }
    }

    private int setReverseDirectionKey() {
        int reverseDir;

        if (prefDir != 0 && prefDir <= 4)
            reverseDir = prefDir + 4;
        else if (prefDir != 0)
            reverseDir = prefDir - 4;
        else if (lastDir <= 4)
            reverseDir = lastDir + 4;
        else
            reverseDir = lastDir - 4;

        return reverseDir;
    }

    /**
     * Scans the area for a food source and marks its position if it has been found.
     * "Area" is defined by the sense property of the Blob. The Blob will try to
     * choose the nearest food source possible.
     * @return True if nearby food has been located.
     */
    private boolean senseFood() {
        int foodX, foodY;
        int fillIndex = -1;
        Environment.Food[] availableFood = new Environment.Food[10];

        for (Map.Entry<Integer, Environment.Food> foodEntity : env.foodHash.entrySet()) {
            foodX = foodEntity.getValue().posX;
            foodY = foodEntity.getValue().posY;

            if (foodX - sense <= posX &&
                    foodX + sense >= posX &&
                foodY - sense <= posY &&
                    foodY + sense >= posY) {

                fillIndex++;
                availableFood[fillIndex] = foodEntity.getValue();
            }
            if (fillIndex == 9) break;
        }

        if (fillIndex > 0) {
            int tempX = Math.abs(availableFood[0].posX - posX);
            int tempY = Math.abs(availableFood[0].posY - posY);
            int compX, compY;
            int index = 0;

            for (int i = 0; i < fillIndex; i++) {
                compX = Math.abs(availableFood[(i + 1)].posX - posX);
                compY = Math.abs(availableFood[(i + 1)].posY - posY);
                if ((compX < tempX &&
                        compY <= tempY) ||
                    (compX <= tempX &&
                        compY < tempY)) {

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
     * @return True if food has been eaten
     */
    private boolean eatFood() {
        if (foodX == null || foodY == null)
            return false;

        boolean ateFood = false;
        for (Map.Entry<Integer, Environment.Food> foodEntity : env.foodHash.entrySet()) {
            if (foodEntity.getValue().available &&
                    foodEntity.getValue().posX == foodX &&
                    foodEntity.getValue().posY == foodY &&
                    posX == foodX &&
                    posY == foodY) {
                
                modEnergy(foodEntity.getValue().satiety);
                log.foodAmount--;
                log.foodEaten++;
                ateFood = true;

                if (main.printEnvEnabled) {
                    env.removeFoodQueue.add(foodEntity.getKey());
                    env.foodQueueSize++;
                    foodEntity.getValue().available = false;
                } else
                    env.foodHash.remove(foodEntity.getKey());

                if (energy >= 100) {
                    setEnergy(100);
                    return true;
                }
            }
        }
        adjustedSpeed = speed;
        return ateFood;
    }

    /**
     * If the Blob meets another Blob on the same block, it will eat it if
     * it's 20% bigger than it. A Blob can eat 1 other Blob at most in one go.
     * @return True if a Blob has been eaten
     */
    private boolean eatBlob() {
        if (size < 8)
            return false;
        Blob blob;
        int blobsEaten = 0;

        for (Map.Entry<Integer, Blob> blobEntity : env.blobHash.entrySet()) {
            blob = blobEntity.getValue();

            if (energy >= 100 && blobsEaten > 0)
                return true;
            if (energy >= 100 && blobsEaten == 0)
                return false;

            if (blob.posX == posX &&
                    blob.posY == posY &&
                    blobsEaten <= 1 &&
                    blob.age >= 10 &&
                    size >= blob.size + (blob.size * 0.2)) {

                int minSizeDiff = (int) (size - (blob.size + (blob.size * 0.2)));
                int agroDiff = agro - blob.agro;
                int eatProb = 50 + agroDiff;

                if (calcProb((eatProb + minSizeDiff))) {
                    modEnergy((blob.size / 8));
                    modSize(1);
                    env.blobHash.remove(blobEntity.getKey());
                    log.blobDeaths++;
                    blobsEaten++;
                    log.blobAmount--;
                    log.blobsEaten++;
                } else {
                    modEnergy(-(blob.size / 6));
                    blob.modEnergy(-minSizeDiff);
                    if (rng.nextBoolean()) blob.modAgro(1);
                    log.blobsDefeated++;
                }
            }
        }
        growOrShrink();
        return blobsEaten > 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class FleeStatus {
        boolean unsafe, noSafeDir;
        int[] safeDir;

        FleeStatus(boolean unsafe, boolean noSafeDir) {
            this.unsafe = unsafe;
            this.noSafeDir = noSafeDir;
        }

        public void setSafeArray(int[] safeDir) {
            this.safeDir = safeDir;
        }
    }

    private FleeStatus determineFleeStatus() {
        Blob b;
        ArrayList<Integer> safeDir = new ArrayList<>(8);
        for (int i = 1; i <= 8; i++)
            safeDir.add(i);

        for (Map.Entry<Integer, Blob> blobEntity : env.blobHash.entrySet()) {
            b = blobEntity.getValue();
            if (b == this)
                continue;

            if (b.posX - sense <= posX &&
                    b.posX + sense >= posX &&
                    b.posY - sense <= posY &&
                    b.posY + sense >= posY &&
                    size <= b.size + (b.size * 0.2) &&
                    agro <= b.agro - (b.agro * 0.1) &&
                    b.sense >= sense &&
                    b.speed >= speed) {

                safeDir = updateSafeDirArray(safeDir, getForeignerDir(b));
            }
        }

        FleeStatus f = new FleeStatus(safeDir.size() < 8, safeDir.size() == 0);
        if (!f.noSafeDir)
            f.setSafeArray(safeDir.stream().mapToInt((Integer::valueOf)).toArray());

        return f;
    }

    private ArrayList<Integer> updateSafeDirArray(ArrayList<Integer> safeDir, int prKey) {
        switch (prKey) {
            case 1:
                for (int i : prTopLeft)
                    safeDir.remove((Integer) i);
                break;
            case 2:
                for (int i : prTop)
                    safeDir.remove((Integer) i);
                break;
            case 3:
                for (int i : prTopRight)
                    safeDir.remove((Integer) i);
                break;
            case 4:
                for (int i : prRight)
                    safeDir.remove((Integer) i);
                break;
            case 5:
                for (int i : prBottomRight)
                    safeDir.remove((Integer) i);
                break;
            case 6:
                for (int i : prBottom)
                    safeDir.remove((Integer) i);
                break;
            case 7:
                for (int i : prBottomLeft)
                    safeDir.remove((Integer) i);
                break;
            case 8:
                for (int i : prLeft)
                    safeDir.remove((Integer) i);
                break;
        }
        return safeDir;
    }

    @SuppressWarnings("ConstantConditions")
    private Integer getForeignerDir(Blob b) {
        if      (b.posX == posX && b.posY == posY)
            return 0;
        else if (b.posX < posX && b.posY > posY)
            return 1;
        else if (b.posX == posX && b.posY > posY)
            return 2;
        else if (b.posX > posX && b.posY > posY)
            return 3;
        else if (b.posX > posX && b.posY == posY)
            return 4;
        else if (b.posX > posX && b.posY < posY)
            return 5;
        else if (b.posX == posX && b.posY < posY)
            return 6;
        else if (b.posX < posX && b.posY < posY)
            return 7;
        else if (b.posX < posX && b.posY == posY)
            return 8;

        throw new IndexOutOfBoundsException("Foreigner direction out of range.");
    }

    private void growOrShrink() {
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
    @SuppressWarnings("ConstantConditions")
    private int calcDirectionKey() {
        /*
        1   2   3
        8   0   4
        7   6   5
        */

        if (foodX == null || foodY == null)
            return 0;

        int diffX, diffY;
        diffX = foodX - posX;
        diffY = foodY - posY;

        if      (diffX == 0 && diffY == 0)
            dirFood = 0;
        else if (diffX < 0 && diffY > 0)
            dirFood = 1;
        else if (diffX == 0 && diffY > 0)
            dirFood = 2;
        else if (diffX > 0 && diffY > 0)
            dirFood = 3;
        else if (diffX > 0 && diffY == 0)
            dirFood = 4;
        else if (diffX > 0 && diffY < 0)
            dirFood = 5;
        else if (diffX == 0 && diffY < 0)
            dirFood = 6;
        else if (diffX < 0 && diffY < 0)
            dirFood = 7;
        else if (diffX < 0 && diffY == 0)
            dirFood = 8;

        calcAdjustedSpeed(Math.abs(diffX), Math.abs(diffY));

        return dirFood;
    }

    /**
     * Calculates the adjusted speed value by checking where food is located.
     * @param tempX The absolute x value determined by calcDirectionKey()
     * @param tempY The absolute y value determined by calcDirectionKey()
     * @return The adjusted speed
     */
    private int calcAdjustedSpeed(int tempX, int tempY) {
        if (speed == 1) {
            adjustedSpeed = 1;
        } else if (tempX >= tempY) {
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
    private boolean correctPos() {
        boolean isOutside = false;
        if (posX > env.dimX) {
            setPosX(env.dimX);
            isOutside = true;
        }
        if (posY > env.dimY) {
            setPosY(env.dimY);
            isOutside = true;
        }
        if (posX < 0) {
            setPosX(0);
            isOutside = true;
        }
        if (posY < 0) {
            setPosY(0);
            isOutside = true;
        }

        return isOutside;
    }

    /**
     * Gets the exclusion key.
     * @return Exclusion key from 0 to 8 (inclusive)
     */
    private int getExKey() {
       /*
        1   2   3
        8   0   4
        7   6   5
       */

        correctPos();

        //free movement
        if (posX < env.dimX && posX > 0 &&
               posY < env.dimY && posY > 0)
            return 0;
        //topLeft
        if (posX == 0 && posY == env.dimY)
            return 1;
        //top
        if (posX < env.dimX && posX > 0 && posY == env.dimY)
            return 2;
        //topRight
        if (posX == env.dimX && posY == env.dimY)
            return 3;
        //right
        if (posX == env.dimX && posY < env.dimY && posY > 0)
            return 4;
        //bottomRight
        if (posX == env.dimX && posY == 0)
            return 5;
        //bottom
        if (posX < env.dimX && posX > 0 && posY == 0)
            return 6;
        //bottomLeft
        if (posX == 0 && posY == 0)
            return 7;
        //left
        if (posX == 0 && posY < env.dimY && posY > 0)
            return 8;

        throw new IndexOutOfBoundsException("Blob: Exclusion key is out of range.");
    }

    public void determineDeath() {
        if (energy <= 0) {
            env.blobHash.remove(id);
            log.blobAmount--;
            log.blobDeaths++;
        } else if (energy > 100)
            setEnergy(100);
    }

    public void tryReproduction() {
        if (log.blobAmount < env.maxBlobs &&
                (log.day - reproductionDate) >= 5 &&
                willReproduce()) {

            env.id++;
            env.blobHash.put(env.id, new Blob(
                    main,
                    env,
                    log,
                    env.id,
                    (int) (energy - (energy * 0.4)),
                    speed + getSpeedMutation(this),
                    sense + getSenseMutation(this),
                    size + getSizeMutation(),
                    agro + getAgroMutation(),
                    defaultDirProb + getDirProbMutation()));
            log.blobBirths++;
            setReproductionDate(log.day);
        }
    }

    private void determinePanicLinearity() {
        if (!panicmode)
            defaultDirProb = dirProb;

        if (!panicmode &&
                foodX == null &&
                (log.day - lastFoodDay) >= 3 &&
                energy <= 35) {

            panicmode = true;
            setDirProb(Math.round(((0.02 * Math.pow((dirProb - 100), 2) + 10) / 100) * dirProb + dirProb));
        } else if (foodX != null) {
            panicmode = false;
            setDirProb(defaultDirProb);
        }
    }

    public void dailyRoutine() {
        log.logBlob(this);

        eatBlob();

        if (energy <= 35) {
            FleeStatus f = determineFleeStatus();
            if (f.unsafe && !f.noSafeDir) {
                moveBlob(f.safeDir[rng.nextInt(f.safeDir.length)], speed);
                senseFood();
            }
        }

        if (foodX != null || senseFood()) {
            determinePanicLinearity();
            calcDirectionKey();
            decideMovement();
            if (calcDirectionKey() == 0) {
                eatFood();
                modEatCount(1);
                setLastFoodDay(log.day);
                foodX = null;
                foodY = null;
            }
        } else {
            determinePanicLinearity();
            decideMovement();
        }

        sizeEnergyLoss();
    }

    private void sizeEnergyLoss() {
        if(size > 33) modEnergy((int) Math.round(-0.03 * size));
        else modEnergy(-1);
    }
}
