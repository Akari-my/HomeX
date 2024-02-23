# HomeX
HomeX is a feature-rich Spigot plugin that introduces a Home in-game system

## Future updates :pencil2:
- Put MySQL Database to better manage the Player Home data
- Make the Database Async

## Commands :spades:
Command | Description | Permession
--- | --- | ---
`/home setHome <name Home>` | Set the home
`/home delHome <name Home>` | Delete Home
`/home home <name Home>` | teleport to home

## Config.yml
<details>
  <summary>Click to open</summary>

```yaml
#
#    _    _                       __   __
#   | |  | |                      \ \ / /
#   | |__| | ___  _ __ ___   ___   \ V /
#   |  __  |/ _ \| '_ ` _ \ / _ \   > <
#   | |  | | (_) | | | | | |  __/  / . \
#   |_|  |_|\___/|_| |_| |_|\___| /_/ \_\
#
#      This Plugin was made with ❤️ by Akari_my
#      GitHub: github.com/Akari-my
#      Discord for support: akari_my
#
#      version: 1.0-BETA


settings:
  cooldownSeconds: 3
  maxHomes: 2

messages:
  success:
    homeSet: "&aHome '%home%' set successfully!"
    homeDeleted: "&aHome '%home%' successfully deleted!"
    teleportedToHome: "&aTeleported to home '%home%'!"
    coolDown: "§aYou will be teleported between 3 seconds"
  error:
    homeAlreadyExists: "&cThe home '%home%' already exists."
    homeNotFound: "&cThe home '%home%' does not exist."
    coolDownWait: "§cYou must wait before you can teleport again"
    coolDownMove: "§cYou moved the teleporter and it was Cancelled"
```
</details>

## Bugs??
Write to me on Discord if you have any bugs or problems with HomeX

## Please leave a ⭐ to help the Project!
