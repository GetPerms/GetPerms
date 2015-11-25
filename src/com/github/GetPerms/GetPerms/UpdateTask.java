package com.github.GetPerms.GetPerms;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateTask implements Runnable {

	private static final String DEVELOPMENT_VERSION = "https://raw.github.com/GetPerms/GetPerms/master/checks/dev";
	private static final String IGNORE_VERSION = "https://raw.github.com/GetPerms/GetPerms/master/checks/force";
	private static final String DEVELOPMENT_FILE = "https://raw.github.com/GetPerms/GetPerms/master/GetPerms.jar";
	private static final String CURSE_API = "https://api.curseforge.com/servermods/files?projectIds=";
	private static final String PROJECT_ID = "36667";
	private static final String VERSION_REGEX = " v([0-9]+(?:\\.[0-9]+)*)";

	private Main plugin;
	private Configuration configuration;
	private CommandSender sender;
	private String pluginVersion;
	private final File dataFolder;
	private final File updateFolder;
	private final File temporaryDownloadFile;
	private final File updateDownloadFile;

	public UpdateTask(Main plugin) {
		this(plugin, null);
	}

	public UpdateTask(Main plugin, CommandSender sender) {
		this.plugin = plugin;
		configuration = plugin.getConfig();
		this.sender = sender;
		pluginVersion = plugin.getDescription().getVersion();
		dataFolder = plugin.getDataFolder();
		updateFolder = new File(dataFolder, "update");
		temporaryDownloadFile = new File(dataFolder, "temporaryDownload");
		updateDownloadFile = new File(updateFolder, "GetPerms.jar");
	}

	@Override
	public void run() {
		try {
			boolean useDevelopmentBuild = plugin.getConfig().getBoolean("devBuilds");
			String latestVersion;
			boolean skipVersionComparison = false;
			String downloadUrl;
			String md5Checksum = null;

			BufferedReader ignoreVersionCheck = new BufferedReader(
					new InputStreamReader(new URL(IGNORE_VERSION).openStream()));
			boolean ignoreVersion = ignoreVersionCheck.readLine().equalsIgnoreCase("true");
			ignoreVersionCheck.close();

			if (!useDevelopmentBuild) {
				URLConnection connection = new URL(CURSE_API + PROJECT_ID).openConnection();
				PluginDescriptionFile descriptionFile = plugin.getDescription();

				connection.addRequestProperty("User-Agent",
						descriptionFile.getName() + "/v" + descriptionFile.getVersion() + " (by " + getAuthors() + ")");

				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String response = reader.readLine();
				reader.close();

				JSONArray fileList = (JSONArray) JSONValue.parse(response);
				JSONObject newestFile = null;

				for (int i = fileList.size() - 1; i >= 0; i--) {
					JSONObject file = (JSONObject) fileList.get(i);
					if (((String) file.get("releaseType")).equals("release")) {
						newestFile = file;
						break;
					}
				}

				if (newestFile == null) {
					sendWarning("No release files could be found!");
					return;
				}

				latestVersion = (String) newestFile.get("name");
				Pattern pattern = Pattern.compile(plugin.getDescription().getName() + VERSION_REGEX);
				Matcher matcher = pattern.matcher(latestVersion);

				if (matcher.find()) {
					latestVersion = matcher.group(1);
				} else {
					sendWarning("Error parsing update details!");
					return;
				}
				downloadUrl = (String) newestFile.get("downloadUrl");
				md5Checksum = (String) newestFile.get("md5");
			} else {
				downloadUrl = DEVELOPMENT_FILE;

				BufferedReader versionReader = new BufferedReader(
						new InputStreamReader(new URL(DEVELOPMENT_VERSION).openStream()));
				latestVersion = versionReader.readLine();
				versionReader.close();

				skipVersionComparison = true;
			}

			if (ignoreVersion) {
				skipVersionComparison = true;
			}

			if (skipVersionComparison || newer(pluginVersion, latestVersion)) {
				if (configuration.getBoolean("autoDownload", true)) {
					sendInfo("GetPerms v" + latestVersion + " is available.");

					if (useDevelopmentBuild) {
						sendInfo("Downloading latest development build...");
					} else {
						sendInfo("Downloading latest release build...");
					}

					updateFolder.mkdirs();

					String downloadChecksum;
					try {
						downloadChecksum = downloadUpdate(downloadUrl, updateDownloadFile);
					} catch (MalformedURLException e) {
						plugin.printStackTrace(e);
						sendWarning("Unable to download the latest update!");
						return;
					} catch (IOException e) {
						plugin.printStackTrace(e);
						sendWarning("Unable to download the latest update!");
						return;
					} catch (NoSuchAlgorithmException e) {
						plugin.printStackTrace(e);
						sendWarning("Unable to download the latest update!");
						return;
					} catch (Exception e) {
						plugin.printStackTrace(e);
						sendWarning("Unable to download the latest update!");
						return;
					}

					if (useDevelopmentBuild) {
						if (verifyUpdate(temporaryDownloadFile)) {
							temporaryDownloadFile.renameTo(updateDownloadFile);
							sendInfo("Newest version of GetPerms is located at");
							sendInfo("'plugins/" + plugin.getDescription().getName() + "/update/GetPerms.jar'.");
						} else {
							temporaryDownloadFile.delete();

							sendWarning("Update file is corrupt! (Contains HTML elements)");
							sendWarning("The update will most likely be available later.");
						}
					} else {
						if (downloadChecksum.equals(md5Checksum)) {
							temporaryDownloadFile.renameTo(updateDownloadFile);
							sendInfo("Newest version of GetPerms is located at");
							sendInfo("'plugins/" + plugin.getDescription().getName() + "/update/GetPerms.jar'.");
						} else {
							temporaryDownloadFile.delete();

							sendWarning("Update file is corrupt! (Checksum mismatch)");
							sendWarning("The update will most likely be available later.");
						}
					}

				} else {
					sendInfo("Newest GetPerms version" + latestVersion + " is available for download. You can");
					sendInfo("get it at " + downloadUrl);

					if (ignoreVersion && !useDevelopmentBuild) {
						sendWarning("This build is marked as important. We strongly recommend that");
						sendWarning("you update to this version as soon as possible!");
					}
				}
			} else {
				sendInfo("You have the latest version!");
			}
		} catch (MalformedURLException e) {
			plugin.printStackTrace(e);
			sendWarning("Unable to check for updates.");
		} catch (IOException e) {
			plugin.printStackTrace(e);
			sendWarning("Unable to check for updates.");
		} catch (AccessControlException e) {
			plugin.printStackTrace(e);
			sendWarning("Unable to check for updates.");
		}
	}

	private String downloadUpdate(String url, File file) throws Exception {
		if (file.isDirectory()) {
			if (!file.delete()) {
				throw new Exception("Unable to delete plugins/" + dataFolder.getName() + "/GetPerms.jar/");
			}
		}

		if (file.exists()) {
			file.delete();
		}

		return plugin.downloadFile(url, temporaryDownloadFile);
	}

	private void sendInfo(String message) {
		if (sender != null) {
			sender.sendMessage(message);
		}

		plugin.info(message);
	}

	private void sendWarning(String message) {
		if (sender != null) {
			sender.sendMessage(message);
		}

		plugin.warn(message);
	}

	private boolean verifyUpdate(File file) {
		BufferedReader reader = null;
		boolean valid = true;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("content=\"text/html;charset=utf-8\"")) {
					valid = false;
				} else if (line.contains("html lang=\"en\"")) {
					valid = false;
				} else if (line.contains("<html>")) {
					valid = false;
				}
			}
		} catch (EOFException ignored) {
		} catch (FileNotFoundException e) {
			plugin.printStackTrace(e);
		} catch (IOException e) {
			plugin.printStackTrace(e);
		}

		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				plugin.printStackTrace(e);
			}
		}
		return valid;
	}

	private final boolean newer(String current, String check) {
		boolean result = false;
		String[] currentVersion = current.split("\\.");
		String[] checkVersion = check.split("\\.");
		int i = Integer.parseInt(currentVersion[0]);
		int j = Integer.parseInt(checkVersion[0]);
		if (i > j) {
			result = false;
		} else if (i == j) {
			i = Integer.parseInt(currentVersion[1]);
			j = Integer.parseInt(checkVersion[1]);
			if (i > j) {
				result = false;
			} else if (i == j) {
				i = Integer.parseInt(currentVersion[2]);
				j = Integer.parseInt(checkVersion[2]);
				if (i >= j) {
					result = false;
				} else {
					result = true;
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	private String getAuthors() {
		StringBuilder stringBuilder = new StringBuilder();
		String[] authors = new String[0];
		authors = plugin.getDescription().getAuthors().toArray(authors);
		for (int i = 0; i < authors.length; i++) {
			stringBuilder.append(authors[i] + (i + 1 < authors.length ? ", " : ""));
		}
		return stringBuilder.toString();
	}

}
