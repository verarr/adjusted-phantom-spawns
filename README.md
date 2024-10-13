# Adjusted Phantom Spawns

A mod for Minecraft for modifying the conditions of Phantoms spawning.

## Technical details

Phantom spawning has a global **cooldown** of 1-2 minutes (randomly reset after it runs out). This mod provides the gamerule `phantomSpawningCooldownPercentage` to modify this cooldown as a percentage of the vanilla value. Smaller cooldown values mean phantoms may **spawn more often**, while larger cooldown values mean phantoms may only **spawn less frequently**.

For each player, phantoms can only spawn if their **"Time Since Last Rest"** statistic exceeds 1 hour (72000 ticks or 3 in-game days). Then, the chance of phantoms spawning is based on the ratio of the actual value of the statistic, and the threshold. This mod provides the gamerule `phantomSpawningThreshold` to adjust the statistic according to a desired threshold value in ticks. Smaller threshold values mean phantoms may spawn after a shorter than regular amount of time has passed since a player has slept, making them have to **sleep more often**. Larger threshold values mean phantoms may only spawn after an extended amount of time, making players have to **sleep less often**.

The chance of phantoms spawning is also based on how much a player has exceeded the threshold. This chance is now also scalable with the `phantomSpawningChancePercentage` gamerule. Lower values mean less chance, and higher means more.

## Usage

To change the phantom spawning cooldown:

```
/gamerule phantomSpawningCooldownPercentage <percentage - default is 100>
```

>[!WARNING]
>
>The cooldown can't be manually reset. Setting it to a high value might mean you will have to wait it out even if you set it to something lower afterwards.

To change the sleep threshold:

```
/gamerule phantomSpawningThreshold <ticks - default is 72000 - one day equals 24000>
```

To change the chance of phantoms spawning:

```
/gamerule phantomSpawningChancePercentage <percentage - default is 100>
```
