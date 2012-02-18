package com.github.GetPerms.GetPerms;

import java.util.logging.Logger;
import java.lang.StringBuilder;

public class l {

	Logger mlog = Logger.getLogger("Minecraft");
	private StringBuilder a;

	public void log(String s)
	{
		String b = a.append("[GetPerms] ").append(s).toString();
		mlog.info(b);
	}
}