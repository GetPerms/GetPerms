package com.github.GetPerms.GetPerms;

import java.util.logging.Logger;
import java.lang.StringBuilder;

public final class l {

	Logger mlog = Logger.getLogger("Minecraft");

	public final void log(String s)
	{
		String b = (new StringBuilder()).append("[GetPerms] ").append(s).toString();
		mlog.info(b);
	}
}