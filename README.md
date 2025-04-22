# Unified

Annoying for a lot of same items from different mods cannot use mutually? This mod can help you.

This mod add a command `/unify`, which you can open a unify screen. You can exchange items with same tag.

## How to use

By default, this mod don't provide any exchange in the screen. You need to configure first.

Open file `.minecraft/config/unified.json`. If file don't exist, create it.

```json
[
  "Write item tag here",
  "More tag",
  "You can write as much as you want to add"
]
```

Then, when you put items which has one of these tags into the unify screen, you can select other items which have the same tag to exchange.

I recommend to use EMI for tag checking.

### Example

You want to let all logs can exchange.

First, find the tag id.

<div align=center><img src="https://raw.githubusercontent.com/IAFEnvoy/Unified/refs/heads/master/img/1.webp" style="width:400px;text-align:center;" alt=""></img></div>

Then write following contents into config file.

```json
[
  "minecraft:logs"
]
```

Open game (or `/reload`), and you can exchange now.

<div align=center><img src="https://raw.githubusercontent.com/IAFEnvoy/Unified/refs/heads/master/img/2.webp" style="width:400px;text-align:center;" alt=""></img></div>