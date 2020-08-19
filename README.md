# Brawlator [ ![Download](https://api.bintray.com/packages/onaplerpg/onaple/Brawlator/images/download.svg) ](https://bintray.com/onaplerpg/onaple/Brawlator/_latestVersion)[![Build Status](https://travis-ci.org/OnapleRPG/Brawlator.svg?branch=master)](https://travis-ci.org/OnapleRPG/Brawlator) ![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Brawlator&metric=reliability_rating) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Introduction
This is a Sponge Minecraft plugin designed to help you to create epic monster and manage theirs spawn into your worlds.
Override natural spawning to spawn specific custom monster on specific biome and height.
We use two configuration files (we provide default ones); one for spawner types and one for monsters. 
When invoking monsters or creating spawners, you will need to use both informations (although monsters can be 
replaced with vanilla names).

## Commands
* `/brawlator vision`: Activate monstrovision to see Brawlator spawner's particles.  
Permission: *brawlator.command.vision*  
* `/brawlator invoke <name> [<position>]`: Spawn a preconfigured monster (or a vanilla one) at your position or at a given position.  
Permission: *brawlator.command.invoke*  
* `/brawlator spawner create <spawnerType> <monsterName>`: Create a spawner at your position with given type and monster.  
Permission: *brawlator.command.spawner*  
* `/brawlator spawner delete`: Delete all spawners in the few blocks around you.  
Permission: *brawlator.command.spawner*  

## Configuration
By default, you will have two configuration files that you can change to match your needs.  

### monsters.conf
Used to generate custom monsters with special attributes. You can still use vanilla monsters, but the configured one can have improved stats.  
* **damage**: Damage points the monster is able to deal  
* **hp**: Monster health points  
* **knockbackResistance**: Monster resistance to knockback  
* **name**: Name to identify the monster (used when issuing commands. if you include whitespace, don't forget to wrap it with "" when issuing in-game commands)  
* **speed**: Monster speed  
* **type**: Monster base type  
* **pools**: Array of loot pooltables id, from which additionnal loot can come
* **equipments**: Equipments that the monster will held
* **naturalSpawn**: Used to replace a "natural" monster spawn with that custom monster
   * **biomeType**: Type of biome when the spawn replacement can happen (like *minecraft:forest*, [see possible biomes](https://minecraft.gamepedia.com/Biome/ID))
   * **probability**: Chance for every spawn that concerns the same monster **type** in the current **biomeType** to be this custom monster instead. Between 0 and 1 suffixed by "d" (ex: 0.2d).
   * **maxHeight**: Used to prevent that spawn behaviour to happen above the mentioned height

### spawners.conf
Used to set spawner configuration. Each spawner you create must have a monster and a type set.  
* **quantityMax**: Max simultaneous monster count who can come from that spawner
* **name**: Spawner type name (used when issuing commands)  
* **maxSpawnRange**: Max range the monster can spawn around the spawner  
* **maxRoamRange**: Max range the monster can roam around the spawner without despawning (0 for unlimited) 
* **rate**: Minimum seconds before the next spawn is allowed  

### loots.conf
Loot tables to add additionnal loot for custom monsters.  
* **probability** Probability that the loot table will be triggered, between 0 and 1. If not triggered, none of the loots below can happen
* **name** Identifier for the loot table
* **loot** Array with the possible loots
    * **weight** Probability within the loot table that the given item can drop.  
    It goes from the first element to the last by increasing the current weight. Exemple:  
    If we roll a 0.7 and the first weight is 0.5, it is skipped. If the second weight is 0.3, then our roll is matched (0.7 < 0.5 + 0.3) and the second item drops. Therefore items exceeding a total of 1 will never drop.
    * **type** If you want a standard minecraft item to drop. Specify a string like "minecraft:cobblestone"
    * **ref** If Itemizer is present on the server, will give an item represented within Itemizer by that ref id
    * **poolId** If Itemizer is present on the server, will trigger an Itemizer pool and match its result
