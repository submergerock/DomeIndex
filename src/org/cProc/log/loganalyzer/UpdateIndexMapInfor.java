package org.cProc.log.loganalyzer;

import java.util.ArrayList;

public interface UpdateIndexMapInfor {
	
	void addNewIndexMap(String message);
	void undoIndexMap(ArrayList<String> indexFileArray);

}
