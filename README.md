# Better-Messages
This is the source code for my spigot plugin [Better Messages](https://www.spigotmc.org/resources/82830)

Better messages does it's best to do exactly as the name suggests! It adds better configurable messages. You can also disable, configure, and randomise the messages! Designed as an alternative to essentials custom join messages with even more features! I am going to do my best to make EVERY message customisable.

## Features
- Custom join/leave messages
- Disable any message that you can configure
- Bukkit colour code and PlaceHolders support!
- Randomised messages
- Reload command
- First join message
- Dimension/World change messages
- Multiple line messages
- Supports prefixes and suffixes!
- First join message only for joining player
- SQLite data saving

## Planned Features (Sorted by priority)
- Command for editing the config in-game
- Message chance (Chance for a message to be used)
- Per world messages
- Custom death and advancement messages
- Get notified when you're mentioned in the chat!
- Certain message if you have specific permissions!
- Message radius

## Built-in PlaceHolders
- {player} player that joined or left/player the event is about (Includes prefix/suffix)
- {from} name of the world a player left from (World Change only)
- {to} name of the world a player entered (World Change only)

## Config
```
#DO NOT TOUCH THIS SETTING
config-version: 9

disable-update-checker: false
disable-outdated-config-warning: false

join:
  enabled: true
  message: '&eWelcome &f&l{player} &eto the server!'
  first-join:
    enabled: true
    message: '&eWelcome &f&l{player} &eto the server for the first time!'
    only-to-player: false

quit:
  enabled: true
  message: '&f&l{player} &eleft the server :('

world-change:
  1:
    enabled: true
    replace-underscore: true
    from: 'world'
    to: 'world_nether'
    message: '&f&l{player} &eleft the &c{from} &eand entered the &c{to}'
```

![BStats](https://bstats.org/signatures/bukkit/Better%20Messages.svg)

If you need help we will help you [here](https://discord.gg/wVCSqV7ptB)!
