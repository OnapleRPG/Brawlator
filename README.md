# Brawlator ![Github Action](https://github.com/OnapleRPG/Brawlator/actions/workflows/gradle.yml/badge.svg) ![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.onaple%3ABrawlator&metric=reliability_rating) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![sponge version](https://img.shields.io/badge/sponge-7.2.0-blue.svg)](https://www.spongepowered.org/) 

This is a Sponge Minecraft plugin designed to create custom monster spawners in your world, or replace default spawning monsters.  
There are three things you can customize with Brawlator:  
- Custom monsters *(pick their name, equipments and stats)*
- Custom spawners *(choose the spawn frequency, range and maximum amount of monsters)*
- Custom loots *(customize your spawned monsters loot. Can optionally use our itemizer)*

## Installation

This plugin needs a sponge server 1.12. Download our [latest release](https://github.com/OnapleRPG/Brawlator/releases) and copy it into your server's mods/ folder. Then restart your server.  
Check in the /config/brawlator/ folder if default configuration is properly copied, and you should notice server logs mentionning the proper plugin loaded.  

## Commands

* `/brawlator vision`: Activate monstrovision for yourself to see Brawlator spawners particles, otherwise invisible.  
Permission: *brawlator.command.vision*  
* `/brawlator invoke <name> [<position>]`: Spawn a preconfigured monster (or a vanilla one) at your position or at a given position.  
Permission: *brawlator.command.invoke*  
* `/brawlator spawner create <spawnerType> <monsterName>`: Create a spawner at your position with given type and monster.  
Permission: *brawlator.command.spawner*  
* `/brawlator spawner delete`: Delete all spawners in the few blocks around you.  
Permission: *brawlator.command.spawner*  

## Configuration files

Brawlator use different configuration files. When starting your server with the plugin the first time, it will generate default ones with example values.  

### global.conf

Miscellaneous config for brawlator: right now it only contains one setting:  
* **enableNaturalSpawning**: If false, natural spawning will entirely be disabled *(defaults to true)*

### monsters.conf

Used to generate custom monsters with special attributes. You can still use vanilla monsters, but the configured one can have custom data and stats.  
* **damage**: Damage points the monster is able to deal  
* **hp**: Monster health points  
* **knockbackResistance**: Monster resistance to knockback  
* **name**: Name to identify the monster (used when issuing commands. if you include whitespace, don't forget to wrap it with "" when issuing in-game commands)  
* **speed**: Monster speed  
* **type**: Monster base type  
* **pools**: Array of loot pooltables id for additionnal loot; refer to loot configuration  
* **equipments**: Equipments that the monster will held
* **naturalSpawn**: Used to replace a "natural" monster spawn with this custom monster. If the conditions are met, natural spawned monsters of the same type will use this configuration instead. Only if "enableNaturalSpawning" set to true.  
   * **biomeType**: Type of biome when the spawn replacement can happen (like *minecraft:forest*, [see possible biomes](https://minecraft.gamepedia.com/Biome/ID)). If set, the replacement can only occur in the given biome.  
   * **probability**: Chance for a spawn to be replaced. Only applied if conditions are met (and monster type is the same). Between 0 and 1 suffixed by "d" for decimals (ex: 0.2d).
   * **maxHeight**: Max height to prevent the replacement from happening above the specified height

### spawners.conf

Used for custom spawners stats. To create a spawner you will need a monster from *monsters.conf* as well as a spawner from *spawners.conf*.    
* **quantityMax**: Max simultaneous monster count who can come from that spawner
* **name**: Spawner type name (used when issuing commands)  
* **maxSpawnRange**: Max range the monster can spawn around the spawner  
* **maxRoamRange**: Max range the monster can roam around the spawner without despawning (0 for unlimited) 
* **rate**: Minimum amount of seconds before the next spawn is allowed  

### loots.conf

Loot tables to add additionnal loot for custom monsters.  
* **probability** Probability that the loot table will be triggered, between 0 and 1. If not triggered, none of the loots below can happen
* **name** Identifier for the loot table
* **loot** Array with the possible loots
    * **weight** Probability within the loot table that the given item can drop, between 0 and 1.  
    It goes from the first element to the last by increasing the current weight.  
    *Exemple:*  
    *If we roll a 0.7 and the first weight is 0.5, it is skipped. If the second weight is 0.3, then our roll is matched (0.7 < 0.5 + 0.3) and the second item drops. Therefore, items with cumulated weight exceeding a total of 1 will never drop.*  
    * **type** If you want a standard minecraft item to drop. Specify a string like "minecraft:cobblestone"
    * **ref** If Itemizer is present on the server, will give an item represented within Itemizer by that ref id
    * **poolId** If Itemizer is present on the server, will trigger an Itemizer pool and match its result
