Introduction
Better messages does it's best to do exactly as the name suggests! It adds better configurable messages. You can also disable, configure, and randomise the messages! Designed as an alternative to essentials custom join messages with even more features!
I am going to do my best to make EVERY message customisable.

Features
- Custom join/leave messages
- Disable any message that you can configure
- Bukkit/Hex colour code and PlaceHolders support!
- Randomised messages
- Reload command
- Have messages show on certain counts (e.g. 1st, 5th, 10th join etc)
- Dimension/World change messages
- Multiple line messages
- Supports prefixes and suffixes!
- YML data saving
- Make the messages send to certain people
- Permission specific messages
- Run commands

Planned Features
- Command for editing the config in-game
- Message chance (Chance for a message to be used)
- Custom death and advancement messages
- Get notified when you're mentioned in the chat!
- Message radius

Built-in PlaceHolders
- {player} player that joined or left/player the event is about (Includes prefix/suffix)
- {from} name of the world a player left from (World Change only)
- {to} name of the world a player entered (World Change only)

```yml
#DO NOT TOUCH THIS SETTING
config-version: 13

disable-outdated-config-warning: false

# Messages for better messages commands
internal-messages:
  prefix: "[&bBetterMessages&r] "
  help-redirect: "&bType \"/bm help\" for help"
  reloaded: "&bConfig has been reloaded"
  help: "&bCommands\n- /bettermessages help: Brings you here!\n- /bettermessages reload: Reloads the config!"

messages:
  join:
   enabled: true

   # List of counts when the message will work, if  you add 5 it will send
   # it on the fifth join (or whatever the event is)
   # Set to -1 to disable
   count:
     - -1

   # The permission required for the message to be run
   # Set to "none" to disable
   permission: none

   # The message to be sent, supports hex and bukkit colour codes
   message: '&eWelcome &f&l{player} &eto the server!'

   # The audience is who the message is sent to
   # Options
   # server - everyone in the server
   # world - everyone in the world the player is currently in. Doesn't work on the leave event
   # world/{world} - specify a world to send the messages to
   # user - the player that activated the event/message
   audience: server

   # The activation is when the message is run, just what event
   # Options
   # join
   # quit
   # world-change/{from}/{to} - replace {to} and {from} with the to and from world names
   activation:
     - join

   # Commands to run when the event runs. Run as the console
   # Disabled by default
   #commands:
     #- give {player} diamond 1
  first-join:
   enabled: true
   count:
     - 1
   permission: none
   message: '&eWelcome &f&l{player} &eto the server for the first time!'
   audience: server
   activation:
     - join
  quit:
   enabled: true
   count:
     - -1
   permission: none
   message: '&f&l{player} &equit :('
   audience: server
   activation:
     - quit
  entered-nether:
   enabled: true
   count:
     - -1
   permission: none
   message: '&f&l{player} &eentered the nether, be careful!'
   audience: server
   activation:
     - world-change/world/world_nether
```
