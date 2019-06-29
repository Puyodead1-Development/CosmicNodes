# CosmicNodes (WIP)
This plugin is a recreation of the Nodes from CosmicSky (with a some extra features that are configurable)<br>
![GitHub repo size](https://img.shields.io/github/repo-size/Puyodead1-Development/CosmicNodes.svg?style=plastic)
![Discord](https://img.shields.io/discord/589200717277954093.svg?style=plastic)
![GitHub All Releases](https://img.shields.io/github/downloads/Puyodead1-Development/CosmicNodes/total.svg?style=plastic)
![GitHub issues](https://img.shields.io/github/issues/Puyodead1-Development/CosmicNodes.svg?style=plastic)
![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/Puyodead1-Development/CosmicNodes.svg?style=plastic)
![GitHub](https://img.shields.io/github/license/Puyodead1-Development/CosmicNodes.svg?style=plastic)
![Spiget Rating](https://img.shields.io/spiget/rating/0.svg?style=plastic)
![GitHub last commit](https://img.shields.io/github/last-commit/Puyodead1/CosmicNodes.svg?style=plastic)


## Requirments
- Spigot 1.13 (for now)
- ~~HolographicDisplays (optional - toggle in config)~~ (Not implemented yet)

## Commands
- /cosmicnodes givenode \<player\> \<type from config\>

## Config info
[Valid Materials](https://gitlab.com/RandomHashTags/RandomSky/blob/master/src/main/java/me/randomhashtags/randomsky/utils/universal/UMaterial.java)
You can add more nodes by copying the existing ``LOG`` part and just changing it. EX:
Original Config ``LOG``:
```
nodes:
  LOG:
    respawn time: 60
    harvest block: OAK_LOG
    node block: OAK_PLANKS
    name: "&fLog Node"
    loot: OAK_BUTTON
    # loot:
    #  - "scrap:oak; &f&l*&r &f1x Wood Scrap"
    lore:
      - "&7Resource Node"
      - " "
      - "&f&lLoot"
      # - "{LOOT}"
      - " &f&l*&r &f1x Wood Scrap"
      - " "
      - "&f&lRespawn Time"
      - " &f{RESPAWN}"
      - " "
      - "&7Place this resource node on your island!"
```
Adding ``GOLD``:
```
nodes:
  LOG:
    respawn time: 60
    harvest block: OAK_LOG
    node block: OAK_PLANKS
    name: "&fLog Node"
    loot: OAK_BUTTON
    # loot:
    #  - "scrap:oak; &f&l*&r &f1x Wood Scrap"
    lore:
      - "&7Resource Node"
      - " "
      - "&f&lLoot"
      # - "{LOOT}"
      - " &f&l*&r &f1x Wood Scrap"
      - " "
      - "&f&lRespawn Time"
      - " &f{RESPAWN}"
      - " "
      - "&7Place this resource node on your island!"
  GOLD:
    respawn time: 1200
    harvest block: GOLD_BLOCK
    node block: GOLD_ORE
    name: "&6Gold Node"
    loot: YELLOW_DYE
    # loot:
    #  - "scrap:oak; &f&l*&r &f1x Wood Scrap"
    lore:
      - "&7Resource Node"
      - " "
      - "&f&lLoot"
      # - "{LOOT}"
      - " &f&l*&r &f1x Gold Scrap"
      - " "
      - "&f&lRespawn Time"
      - " &f{RESPAWN}"
      - " "
      - "&7Place this resource node on your island!"
```
