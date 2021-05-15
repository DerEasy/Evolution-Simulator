package com.easy.evolutionsimulator;

import static com.easy.evolutionsimulator.Calc.calcProb;
import static com.easy.evolutionsimulator.Environment.*;

public abstract class Animal {
    public int speed;
    public int sense;
    public int size;
    public int energy;
    public int agro;
    public int age;
    public int id;
    public int posX;
    public int posY;
    public int reproductionDate;
    public int dirProb;
    public int eatCount;
    public int lastFoodDay;

    public Animal() {
        age = 0;
    }

    public boolean willReproduce() {
        if (age > 5) return calcProb((int) Math.pow((0.04 * (energy + 20)), 3));
        else return false;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setDirProb(long dirProb) {
        this.dirProb = (int) dirProb;
        if (this.dirProb > 100) setDirProb(100);
        else if (this.dirProb < 0) setDirProb(0);
    }

    public void setAgro(Integer agro) {
        this.agro = agro;
        if (this.agro > 100) setAgro(100);
        else if (this.agro < 0) setAgro(0);
    }

    public void setLastFoodDay(int lastFoodDay) {
        this.lastFoodDay = lastFoodDay;
    }

    public void setEnergy(Integer energy) {
        if (energy <= 100) this.energy = energy;
        else this.energy = 100;
    }

    public void setSense(Integer sense) {
        this.sense = sense;

        boolean dimXGreater = dimX >= dimY;
        if (dimXGreater && this.sense > (dimX / 2)) this.sense = (dimX / 2);
        else if (!dimXGreater && this.sense > (dimY / 2)) this.sense = (dimY / 2);
    }

    public void setSize(Integer size) {
        this.size = size;
        if (this.size > 100) setSize(100);
        else if (this.size < 1) setSize(1);
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;

        boolean dimXGreater = dimX >= dimY;
        if (dimXGreater && this.speed > (dimX / 3)) this.speed = (dimX / 3);
        else if (!dimXGreater && this.speed > (dimY / 3)) this.speed = (dimY / 3);
    }

    public void setReproductionDate(int reproductionDate) {
        this.reproductionDate = reproductionDate;
    }

    public void modPosX(int posX) {
        this.posX += posX;
    }

    public void modPosY(int posY) {
        this.posY += posY;
    }

    public void modPosition(int posX, int posY) {
        this.posX += posX;
        this.posY += posY;
    }

    public void modAge(Integer age) {
        this.age += age;
    }

    public void modDirProb(Integer dirProb) {
        this.dirProb += dirProb;
        if (this.dirProb > 100) setDirProb(100);
        else if (this.dirProb < 0) setDirProb(0);
    }

    public void modAgro(Integer agro) {
        this.agro += agro;
        if (this.agro > 100) setAgro(100);
        else if (this.agro < 0) setAgro(0);
    }

    public void modEnergy(Integer energy) {
        this.energy += energy;
        if (this.energy > 100) setEnergy(100);
    }

    public void modEatCount(int eatCount) {
        this.eatCount += eatCount;
    }

    public void modSense(Integer sense) {
        this.sense += sense;

        boolean dimXGreater = dimX >= dimY;
        if (dimXGreater && this.sense > (dimX / 2)) this.sense = (dimX / 2);
        else if (!dimXGreater && this.sense > (dimY / 2)) this.sense = (dimY / 2);
    }

    public void modSize(Integer size) {
        this.size += size;
        if (this.size > 100) setSize(100);
        else if (this.size < 1) setSize(1);
    }

    public void modSpeed(Integer speed) {
        this.speed += speed;

        boolean dimXGreater = dimX >= dimY;
        if (dimXGreater && this.speed > (dimX / 2)) this.speed = (dimX / 2);
        else if (!dimXGreater && this.speed > (dimY / 2)) this.speed = (dimY / 2);
    }
}
