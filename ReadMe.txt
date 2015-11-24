OFFICIAL RELEASE 2/16/2012!
Version Installed: 1.1.13 (11/24/2015)

About: GetPerms is a plugin for Minecraft servers running Bukkit. This plugin will gather a list of all permission nodes for every plugin installed on the server and output the nodes to a file.

Note: Plugins must be in the '/plugins' folder in order for GetPerms to grab the permission nodes from it. Also, GetPerms can only retrieve nodes that are in '[plugin_name].jar/plugin.yml' due to Bukkit's current methods.

Installation:
  1. Download the plugin (http://dev.bukkit.org/server-mods/getperms)
  2. Move the jar file to '/plugins' inside the server root directory

Usage:
  1. Start the server
  2. Go into the GetPerms directory (located at plugins/GetPerms/)
  3. Open 'permission_nodes.txt' and/or 'permission_nodes_desc.txt'

Commands:
  All commands can be run with /gp as an alias as well
  /getperms regen           (Re)generate the files containing the permission nodes lists
  /getperms reload          Reload the configuration file for GetPerms

Permission Nodes:
  getperms.regen            Allows player to regenerate permission node files
  getperms.reload           Allows player to reload the GetPerms config file

---------------FILES---------------

permission_nodes.txt:
  Each line is one separate permission node. This file is useful if you want one to just copy and paste the nodes without having to highlight each line separately to avoid copying the description. A permission node's description is available in 'permission_nodes_desc.txt' which is also in GetPerms' plugin folder (located at plugins/GetPerms/).

permission_nodes_desc.txt
  Quite the same as 'permission_nodes.txt', except this one has the description next to each node. Permission nodes are grouped by plugin.

[Plugin name]_permission_nodes.txt
  This is the same thing as permission_nodes.txt, except for a specific plugin. These are usually generated when a plugin doesn't specify all of its permission nodes in its plugin.yml, such as Essentials.

[Plugin name]_permission_nodes_desc.txt
  This is the same thing as permission_nodes_desc.txt, except for a specific plugin. These are usually generated when a plugin doesn't specify all of its permission nodes in its plugin.yml, such as Essentials.