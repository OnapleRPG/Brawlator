package com.onaple.brawlator.exceptions;

public class MonsterNotFoundException extends Throwable {
    public MonsterNotFoundException(String monsterName) {
        super("Monster with name " + monsterName + " was not found.");
    }
}
