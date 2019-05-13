package com.onaple.brawlator.exceptions;

public class EntityTypeNotFoundException extends Exception {
    public EntityTypeNotFoundException(String type){
        super("The entity type : " + type + " does not exist." );
    }
}
