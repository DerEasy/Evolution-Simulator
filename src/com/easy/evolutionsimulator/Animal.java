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

    public void setAgro(Integer agro) {
        this.agro = agro;
        if (this.agro > 100) setAgro(100);
        else if (this.agro < 0) setAgro(0);
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
        if (dimXGreater && this.speed > (dimX / 2)) this.speed = (dimX / 2);
        else if (!dimXGreater && this.speed > (dimY / 2)) this.speed = (dimY / 2);
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

    public void modAgro(Integer agro) {
        this.agro += agro;
        if (this.agro > 100) setAgro(100);
        else if (this.agro < 0) setAgro(0);
    }

    public void modEnergy(Integer energy) {
        this.energy += energy;
        if (this.energy > 100) setEnergy(100);
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

    public String getEnergy() {
        if (energy >= 10) return String.valueOf(energy);
        else return "0" + energy;
    }

    public String getAge() {
        if (age >= 10) return String.valueOf(age);
        else return "0" + age;
    }

    public String getSize() {
        if (size >= 10) return String.valueOf(size);
        else return "0" + size;
    }

    public String getAgro() {
        if (agro >= 10) return String.valueOf(agro);
        else return "0" + agro;
    }

    public String getSense() {
        if (sense >= 10) return String.valueOf(sense);
        else return "0" + sense;
    }

    public String getSpeed() {
        if (speed >= 10) return String.valueOf(speed);
        else return "0" + speed;
    }

    public String getID() {
        if (id >= 10) return String.valueOf(id);
        else return "0" + id;
    }
}
