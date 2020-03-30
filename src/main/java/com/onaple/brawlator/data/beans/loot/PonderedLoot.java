package com.onaple.brawlator.data.beans.loot;

import ninja.leaping.configurate.objectmapping.Setting;

public abstract class PonderedLoot implements Loot {
    public PonderedLoot(double weigth) {
        this.weigth = weigth;
    }
    protected double weigth;

    public double getWeight(){
        return weigth;
    }
}
