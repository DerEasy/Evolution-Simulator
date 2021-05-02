package com.easy.evolutionsimulator;

public class Animal {
    public Integer speed, strength, sense, size, energy, agro, age, id;

    public Animal() {
        age = 0;
    }

    public boolean willReproduce() {
        if (age > 10) return Calc.calcProb((int) Math.pow((0.040 * (energy + 20)), 3));
        else return false;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAgro(Integer agro) {
        this.agro = agro;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public void setSense(Integer sense) {
        this.sense = sense;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public void modAge(Integer age) {
        this.age += age;
    }

    public void modAgro(Integer agro) {
        this.agro += agro;
    }

    public void modEnergy(Integer energy) {
        this.energy += energy;
    }

    public void modSense(Integer sense) {
        this.sense += sense;
    }

    public void modSize(Integer size) {
        this.size += size;
    }

    public void modSpeed(Integer speed) {
        this.speed += speed;
    }

    public void modStrength(Integer strength) {
        this.strength += strength;
    }
}
