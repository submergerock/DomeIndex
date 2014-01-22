package org.cProc.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

public class ParseTools {

	public static final int appPos = 0;
	public static final int tablePos = 1;
	public static final int columStartPos = 2;

	public static void main(String[] args) throws Exception {

		String createLang = "create table BtreeTest ( A INT , B INT  , C  STRING 3)  on gentest";
		String loadLang = "load  data  local inpath  'D:\\08\\1300752000-1300752083-bssap-1-node27.cdr' into table BtreeTest on gentest";
		//String createTimeIndexLang = "create time index testIndex on test of TelecomData ( time A INT , B INT )";
		String createTimeIndexLang = "create time index calledIndex on bssap of gentest ( time start_time_s INT, start_time_ns INT)";
		String selectLang = "select A,B,C from test where A == A and   (A == A  or A == B) and  (A == A  and  A == B) ";

		File f = new File("D:\\09");
		String[] fileNumber = f.list();
		for (int i = 1; i <= fileNumber.length; i++) {
			//String loadLang = "load  data  local inpath  'D:\\test\\"+i+".dat' into table bssap on gentest";
			loadLang = "load  data  local inpath  '"+ "D:\\09\\"+fileNumber[i-1]+ "' into table BtreeTest on gentest";
			CreateTestLexer lexer = new CreateTestLexer(new ANTLRStringStream(
					loadLang));// 4^2*2/4-(6+2)+4

			CommonTokenStream tokens = new CommonTokenStream(lexer);

			CreateTestParser parser = new CreateTestParser(tokens);
			CommonTree tree = parser.statement().tree;
			System.out.println(tree.getChildCount());
			switch (tree.getToken().getType()) {
			case CreateTestParser.TOK_LOAD:
				System.out.println(tree.toStringTree());
				loadDataStatement(tree);
				break;
			case CreateTestParser.TOK_CREATE:
				System.out.println(tree.toStringTree());
				createTableStatement(tree);
				break;
			case CreateTestParser.TOK_CREATE_TIMEINDEX:
				System.out.println(tree.toStringTree());
				createIndex(tree, CreateTestParser.TOK_CREATE_TIMEINDEX);
				break;
			case CreateTestParser.TOK_SELECT:
				System.out.println(tree.toStringTree());
				selectStatement(tree);
			}

		}

	}

	// tableName:testTable
	// column : A , INT,4
	// column : B , INT,4
	// column : C , STRING,10
	// column : D , LONG,8
	public static void createTableStatement(CommonTree tree) {
		String appName = "";
		String tableName = "";
		String time = System.currentTimeMillis() + "";
		String filePath = "D:\\test\\" + time;
		File output = new File(filePath);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(output);
			System.out.println("type  is " + tree.getToken().getText());
			appName = tree.getChild(0).getChild(0).getText();
			tableName = tree.getChild(1).getChild(0).getText();
			pw.println("tableName:" + tree.getChild(1).getText());
			for (int i = 2; i < tree.getChildCount(); i++) {
				if (tree.getChild(i).getChildCount() == 2) {
					pw.println("column : "
							+ tree.getChild(i).getChild(0).getText() + " ,  "
							+ tree.getChild(i).getChild(1).getText() + " ,  4");
				} else if (tree.getChild(i).getChildCount() == 3) {
					pw.println("column : "
							+ tree.getChild(i).getChild(0).getText() + " ,  "
							+ tree.getChild(i).getChild(1).getText() + " ,  "
							+ tree.getChild(i).getChild(2).getText());
				}
			}
			System.out.println(tree.toStringTree());
			pw.close();
			
//			int reslut = TableAndIndexTool.createTable(
//					"hdfs://192.168.1.8:9000", appName, tableName, filePath);
//			System.out.println("reslut is " + reslut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			output.delete();
		}
	}

	public static void loadDataStatement(CommonTree tree) {
		String appName = "";
		String tableName = "";
		String fileName = "";

		System.out.println("type  is " + tree.getToken().getText());
		for (int i = 0; i < tree.getChildCount(); i++) {
			System.out.println(i + "  is " + tree.getChild(i).getText());
		}
		appName = tree.getChild(0).getChild(0).getText();
		tableName = tree.getChild(1).getChild(0).getText();
		fileName = tree.getChild(2).getText();
		fileName = fileName.substring(1, fileName.length() - 1);
		System.out.println("appName is " + appName);
		System.out.println("tableName is " + tableName);
		System.out.println("fileName is " + fileName);

//		int reslut = TableAndIndexTool.loadDataIntoHDFS(
//				"hdfs://192.168.1.8:9000", appName, tableName, fileName,
//				"/gyy");
//		System.out.println("reslut is " + reslut);
	}

	// tableName:testTable
	// indexName:testIndex
	// time:A
	// other:B
	// other:C
	public static void createIndex(CommonTree tree, int indexType) {
		String appName = "";
		String tableName = "";
		String indexName = "";

		String time = System.currentTimeMillis() + "";
		String filePath = "D:\\test\\" + time;
		System.out.println("filePath is "+filePath);
		File output = new File(filePath);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(output);
			System.out.println("type  is " + tree.getToken().getText());
			for (int i = 0; i < tree.getChildCount(); i++) {
				System.out.println(i + "  is " + tree.getChild(i).getText());
			}
			appName = tree.getChild(0).getChild(0).getText();
			tableName = tree.getChild(1).getChild(0).getText();
			indexName = tree.getChild(2).getChild(0).getText();
			System.out.println("appName: " + appName);
			System.out.println("tableName: " + tableName);
			System.out.println("indexName: " + indexName);
			pw.println("tableName: " + tableName);
			pw.println("indexName: " + indexName);
			for (int i = 3; i < tree.getChildCount(); i++) {
				if (tree.getChild(i).getChildCount() == 3) {
					pw.print(tree.getChild(i).getChild(2).getText());
				} else {
					pw.print("other");
				}
				pw.print(" : " + tree.getChild(i).getChild(0).getText());
				pw.println(" : " + tree.getChild(i).getChild(1).getText());
			}
			pw.close();
			int reslut = 1;//TableAndIndexTool.createIndex(
//					"hdfs://192.168.1.8:9000", appName, tableName, indexName,
//					filePath);
			System.out.println("reslut is " + reslut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			output.delete();
		}
	}

	public static void selectStatement(CommonTree tree) {
		System.out.println(tree.getChildCount());

		System.out.println("tableName " + " is " + tree.getChild(0).getText());
		for (int i = 1; i < tree.getChildCount(); i++) {
			System.out.println("child " + i + " is "
					+ tree.getChild(i).toStringTree());
			switch (tree.getChild(i).getType()) {
			case CreateTestParser.TOK_SELECTLIST:
				selectListStatement((CommonTree) tree.getChild(i));
				break;
			case CreateTestParser.TOK_ORCONDITION:
				System.out.println(orStatement((CommonTree) tree.getChild(i)));
				break;
			case CreateTestParser.TOK_GROUP:
				selectListStatement((CommonTree) tree.getChild(i).getChild(0));
				break;
			}
		}
	}

	private static void selectListStatement(CommonTree tree) {
		System.out.print("list is   ");
		for (int m = 0; m < tree.getChildCount(); m++) {
			System.out.print(tree.getChild(m) + " ");
		}
		System.out.println();
	}

	private static boolean orStatement(CommonTree tree) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			boolean reslut = false;
			switch (tree.getChild(i).getType()) {
			case CreateTestParser.TOK_ANDCONDITION:
				reslut = andStatement((CommonTree) tree.getChild(i));
				break;
			case CreateTestParser.TOK_ORCONDITION:
				reslut = orStatement((CommonTree) tree.getChild(i));
				break;
			}
			if (reslut) {
				return reslut;
			}
			return reslut;
		}
		return true;
	}

	private static boolean andStatement(CommonTree tree) {
		boolean reslut = true;
		for (int i = 0; i < tree.getChildCount(); i++) {
			switch (tree.getChild(i).getType()) {
			case CreateTestParser.TOK_ANDCONDITION:
				reslut = andStatement((CommonTree) tree.getChild(i));
				break;
			case CreateTestParser.TOK_ORCONDITION:
				reslut = orStatement((CommonTree) tree.getChild(i));
				break;
			case CreateTestParser.TOK_ATOMCONDITION:
				for (int m = 0; m < tree.getChild(i).getChildCount(); m++) {
					System.out.print(tree.getChild(i).getChild(m)
							.toStringTree()
							+ " ");
				}
				System.out.println();
				if (!tree.getChild(i).getChild(0).getText().equals(
						tree.getChild(i).getChild(2).getText())) {
					reslut = false;
				}
				break;
			}
			if (reslut == false) {
				return false;
			}
		}
		return true;
	}

}
