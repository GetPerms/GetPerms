package com.github.GetPerms.GetPerms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteToFile {

	public void Write(String string, File file) throws IOException {
		Write(string, "", file);
	}
	
	public void Write(String name, String desc, File file) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.println((new StringBuilder()).append(name).append(" - ").append(desc).toString());
	}
}
