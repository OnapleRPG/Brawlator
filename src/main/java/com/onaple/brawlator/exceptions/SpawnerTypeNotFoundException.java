package com.onaple.brawlator.exceptions;

public class SpawnerTypeNotFoundException extends Throwable {
    public SpawnerTypeNotFoundException(String spawnerTypeName) {
        super("Spawner type with name " + spawnerTypeName + " was not found.");
    }
}
