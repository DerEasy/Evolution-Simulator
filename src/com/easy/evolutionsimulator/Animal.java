package com.easy.evolutionsimulator;

import static com.easy.evolutionsimulator.Calc.calcProb;

public abstract class Animal {
    public Integer speed, strength, sense, size, energy, agro, age, id;
    public int posX, posY;

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

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAgro(Integer agro) {
        this.agro = agro;
        if (this.agro > 100) setAgro(100);
        else if (this.agro < 0) setAgro(0);
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public void setSense(Integer sense) {
        this.sense = sense;
    }

    public void setSize(Integer size) {
        this.size = size;
        if (this.size > 100) setSize(100);
        else if (this.size < 1) setSize(1);
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
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
    }

    public void modSize(Integer size) {
        this.size += size;
        if (this.size > 100) setSize(100);
        else if (this.size < 1) setSize(1);
    }

    public void modSpeed(Integer speed) {
        this.speed += speed;
    }

    public void modStrength(Integer strength) {
        this.strength += strength;
    }
}
