package com.ai.AIBase.config;

import com.ailk.common.data.IData;

import java.io.InputStream;

public abstract class AbstractCfg {
	
	protected String fileName;
	protected InputStream stream;
	protected IData cache;

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * get file name
	 * @return IData
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 *
	 * @param fileName
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * get names
	 * @return String[]
	 */
	public String[] getNames() {
		return cache.getNames();
	}
	
	/**
	 * get all
	 * @return IData
	 */
	public IData getAll() {
		return cache;
	}
	
}