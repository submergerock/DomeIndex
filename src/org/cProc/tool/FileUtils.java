package org.cProc.tool;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class FileUtils {
	public void newFolder(String folderPath) {
		try {

			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		}
	}

	public static void write2HfsFile(String msg, Path path, FileSystem fs) {

		FSDataOutputStream os = null;
		try {
			os = fs.create(path);
			os.write(msg.getBytes());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != os)
				IOUtils.closeStream(os);
		}

	}
}
