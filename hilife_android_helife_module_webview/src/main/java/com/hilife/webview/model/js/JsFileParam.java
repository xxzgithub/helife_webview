package com.hilife.webview.model.js;

public class JsFileParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5674743935072422668L;

	private String fileID;
	private String filePath;
	private String fileName;
	private String fileSize;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
}
