package testReadFile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.IndexColumnInfor;
import org.cProc.GernralRead.ReadData.ReadData;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;

public class TestConfigFile {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://192.168.1.12:8000");
		FileSystem fs = FileSystem.get(conf);
		TableAndIndexTool.setPrePath("/smp");

		ReadData readData = ReadData.getBinaryReadData("bssap", "cdr", fs);

		ArrayList<String> indexList = TableAndIndexTool.getAllIndexsInTable(
				"bssap", "cdr", fs);
		// DebugPrint.DebugPrint("indexList---->"+indexList.size(), this);
		for (int indexNum = (indexList.size() - 1); indexNum >= 0; indexNum--) {
			// LocalTempIndexFileInfor indexFileInfor = new
			// LocalTempIndexFileInfor();
			ArrayList<IndexColumnInfor> indexColumnIndexPos = ReadData
					.getIndexColumnPos(indexList.get(indexNum), "bssap", "cdr",
							readData);
			
			System.out.println(indexColumnIndexPos.size());
		}

	}
}
