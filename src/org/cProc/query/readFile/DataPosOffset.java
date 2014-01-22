package org.cProc.query.readFile;

class DataPosOffset {
	
	private String fileName = "";
	private String offsets = "";


	public String getFileName() {
		return fileName;
	}

	public DataPosOffset(String fileName, String offsets) {
//		System.out.println("@gyy:DataPosOffset: fileName is " + fileName +" offsets is "+ offsets);
		this.fileName = fileName;
		this.offsets = offsets;
		
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOffsets() {
		return offsets;
	}

	public void setOffsets(String offsets) {
		this.offsets = offsets;
	}

}
