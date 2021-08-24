![Mod Logo](src/main/resources/playerbackup_250.png)

# Player Backup

## TL;DR

Create backup of own player
```bash
/playerbackup backup
```

Create backup of an other player
```bash
/playerbackup backup <name>
```

Restore last backup of own player
```bash
/playerbackup restore
```

Restore last backup of another player
```bash
/playerbackup restore <name>
```

Restore earlier backup of another player
```bash
/playerbackup restore <name> <timestamp of backup>
```

## Commands
### Backup
The backup commands are set up to be as convenient as possible. The base command ``/playerbackup backup`` will create a backup of the current player. If the amout of current backups of that player exeed the ``deleteOnRestore``-config the oldest backup is deleted.
To backup another player you may extend the command with the players name: ``/playerbackup backup Absolem90``.

### Restore
The restoration commands are setup analogously to the backup. By starting with ``/playerbackup restore`` you will restore your own latest backup.
By extending the command with a player name you are able to restore the latest backup of that player onto that player: ``/playerbackup restore Absolem90``. You cannot apply the backup to someone other than the original player.
By extending the command even further you may select the backup to restore. You will get suggestions of available backups:
``/playerbackup restore Absolem90 21_08_23_190324``. The backup is name after the timestamp it was created on. The format is `yy_MM_dd_kkmmss`. [for reference](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)

## Configuration
The configuration file will be created at ``config/playerbackup.json``.
The following Properties are configurable:

| name | type | default value | description |
| --- | --- | --- | --- |
| backupCountPerPlayer | integer | 5 | This count sets the allowed amount of backups per player. If the amount is reached the oldest backup is removed. If the value is <= 0 the limitation is disabled. |
| deleteOnRestore | booelean | true | If the flag is set to true a backup is deleted, once it is restored. So the restoration can only be done once.|


### Permissions
The ``config/playerbackup.json`` also configures the access level for the four commands.
Each Permission consists of the name, its id, a description and permission level.
By setting the permission level you may choose the enable the commands to other players. The valid options are:

| Level | Player | OP |
| --- | --- | --- |
| ALL | true | true |
| OP | false | true |
| NONE | false | false |

| permission | command | default |
| --- | --- | --- |
| selfRestore | ``/playerbackup restore`` | OP |
| restore | ``/playerbackup restore <name>`` | OP | 
| selfBackup | ``/playerbackup backup`` | ALL |
| backup | ``/playerbackup backup <name>`` | OP |

[Wiki Source](https://nekoyue.github.io/ForgeJavaDocs-NG/javadoc/1.16.5/net/minecraftforge/server/permission/DefaultPermissionLevel.html)

## Data Storage
The backup data is store within th worlds directory: `<world folder>/playerbackup`. Each backup is stored in an own file. The file names follow this pattern:
`<player uuid>-<timestamp>.dat`. The content is binary data.

## QA
This Mod was testest with the following versions:

| Minecraft | Forge |
| --- | --- |
| 1.16.5 | 36.1.31 |
| 1.16.5 | 36.2.2 |

## Disclaimer
Even though I tested the mod eagerly I cannot guarantee for a bugless behavior. I will not take responsibiliy if any data is lost or destroyed.

## Questions and Inspiration
Feel free to open up Github Tickets. Once in a while I will take a look at them. 