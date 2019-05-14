package com.onaple.brawlator.data.beans;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class SpawnerTypeListBean {
    public static final TypeToken<SpawnerTypeListBean> TYPE = TypeToken.of(SpawnerTypeListBean.class);

    @Setting(value="spawners")
    List<SpawnerTypeBean> spawners;

    public List<SpawnerTypeBean> getSpawners() {
        return spawners;
    }
    public void setSpawners(List<SpawnerTypeBean> spawners) {
        this.spawners = spawners;
    }
}
