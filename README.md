# Brawlator ![Build Status](https://travis-ci.org/OnapleRPG/Brawlator.svg?branch=master) ![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=Brawlator&metric=alert_status) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Introduction
This is a Sponge Minecraft plugin designed to help you generate custom spawners into your worlds.  
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
* **damage**: Damage the monster is able to deal  
* **hp**: Monster health points  
* **knockbackResistance**: Monster resistance to knockback  
* **name**: Name to identify the monster (used when issuing commands)  
* **speed**: Monster speed  
* **type**: Monster base type  

### spawners.conf
Used to set spawner configuration. Each spawner you create must have a monster and a type set.  
* **quantityMax**: Max monster count from the spawner
* **name**: Type name (used when issuing commands)  
* **maxSpawnRange**: Max range the monster can spawn around the spawner  
* **maxRoamRange**: Max range the monster can roam around the spawner without despawning (0 for unlimited) 
* **rate**: Minimum seconds before the next spawn is allowed  
