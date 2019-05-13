package com.onaple.brawlator.data.beans;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MonsterListBean {
    public static final TypeToken<MonsterListBean> TYPE = TypeToken.of(MonsterListBean.class);

    @Setting(value="monsters")
    List<MonsterBean> monsters;

    public List<MonsterBean> getMonsters() {
        return monsters;
    }
    public void setMonsters(List<MonsterBean> monsters) {
        this.monsters = monsters;
    }
}
