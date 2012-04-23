OFFICIAL RELEASE 2/16/12!
Version Installed: 1.1.1

About: GetPerms is a plugin for Minecraft servers running Bukkit. This plugin will gather a list of all permission nodes for every plugin installed on the server and output the nodes to a file.

Note: Plugins must be in the '/plugins' folder in order for GetPerms to grab the permission nodes from it. Also, GetPerms can only retrieve nodes that are in '[plugin_name].jar/plugin.yml' due to Bukkit's current methods.

Installation:
  1. Download the plugin (http://dev.bukkit.org/server-mods/getperms)
  2. Move the jar file to '/plugins' inside the server root directory

Usage:
  1. Start the server
  2. Go into the server root directory
  3. Open 'pnodes.txt'

Commands:
  /getperms regen
  /getpermsregen
  /gp regen
  /gpregen

Permission Nodes:
  getperms.regen

pnodes.txt:
  Each line is one separate permission node. This file is useful if you want one to just copy and paste the nodes without having to highlight each line separately to avoid copying the description. A permission node's description is available in 'pnodesfull.txt' which is also in the server root directory.

pnodesfull.txt
  Quite the same as pnodes.txt, except this one has the description next to each node. Permission nodes are listed in  separate groups (a group for each plugin). 