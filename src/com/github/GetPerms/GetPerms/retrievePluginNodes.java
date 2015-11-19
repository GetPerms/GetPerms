package com.github.GetPerms.GetPerms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RetrievePluginNodes {

	Main gp;
	String dbEntryCheck = "https://raw.github.com/GetPerms/GetPerms/master/db/entries";
	URL dbEntryChecks;

	public RetrievePluginNodes(Main gp) {
		this.gp = gp;
	}

	@SuppressWarnings("unused")
	public final void getDatabasePlugins() {
		String list = "";
		try {
			dbEntryChecks = new URL(dbEntryCheck);
			BufferedReader r = new BufferedReader(new InputStreamReader(dbEntryChecks.openStream()));
			list = r.readLine();
		} catch (MalformedURLException e) {
			gp.PST(e);
		} catch (IOException e) {
			gp.PST(e);
		}
	}

}
