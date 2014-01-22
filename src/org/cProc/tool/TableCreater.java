package org.cProc.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TableCreater {

	private static final String SAVE_PATH = "d:/create";

	private static final String XML_PATH = "d:/versionconfig.xml";

	static void create() throws DocumentException, IOException {

		File outFile = new File(SAVE_PATH);
		if (!outFile.exists())
			outFile.mkdirs();

		File inputXml = new File(XML_PATH);
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(inputXml);
		Element root = document.getRootElement();

		List<Element> list = root.elements();

		for (Element ele : list)// table
		{
			createTable(ele);
		}

	}

	static void createTable(Element table) throws IOException {

		Element version = (Element) table.elements().get(0);

		List<Element> list = version.elements();

		File nameFile = new File(SAVE_PATH, table.getName());
		if (!nameFile.exists())
			nameFile.mkdirs();

		FileOutputStream fos = new FileOutputStream(new File(nameFile,
				"table.frm"));
		// System.out.println(ele.attributeValue("name"));
		fos.write(("tableName:" + table.getName() + "\n").getBytes());
		//fos.write(("tableVersion:1\n").getBytes());

		for (Element ele : list)// table
		{
			// name="start_time_s" len="4"
			// column : start_time_s , INT , 4
			String colname = ele.attributeValue("name");
			String sizeStr = ele.attributeValue("len");
			int size = Integer.parseInt(sizeStr);
			//column :	acm_time , INT ,                    4
			fos.write(("column:"+colname.trim()+","+getType(size)+","+size+"\n").getBytes());
		}

		fos.flush();
		fos.close();

	}

	static String getType(int size) {
		if (size == 1) {
			return "BYTE";
		} else if (size == 2) {
			return "SHORT";
		} else if (size == 4) {
			return "INT";
		} else if (size == 8) {
			return "LONG";
		} else if (size>8) {
			return "STRING";
		}
		
		return "---------------------------------------------------------------------------------------------------------------------------------------------";
	}

	/**
	 * @param args
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		// TODO Auto-generated method stub
		create();
	}

}
