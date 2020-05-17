package com.onaple.brawlator.data.beans;

import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public class EquipmentBean {

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack mainHand;

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack offHand;

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack head;

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack chest;

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack legs;

    @Setting
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack foot;


    public EquipmentBean() {
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public ItemStack getHead() {
        return head;
    }

    public ItemStack getChest() {
        return chest;
    }

    public ItemStack getLegs() {
        return legs;
    }

    public ItemStack getFoot() {
        return foot;
    }

    @Override
    public String toString() {
        return "EquipmentBean{" +
                "mainHand=" + mainHand +
                ", offHand=" + offHand +
                ", head=" + head +
                ", chest=" + chest +
                ", legs=" + legs +
                ", foot=" + foot +
                '}';
    }
}
