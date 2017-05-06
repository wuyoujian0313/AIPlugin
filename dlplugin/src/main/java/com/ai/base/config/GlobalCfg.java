package com.ai.base.config;

import com.ai.base.util.Parser;
import com.ai.base.util.Utility;
import com.ailk.common.data.IData;

public class GlobalCfg extends AbstractCfg {
	public static final String CONFIG_FIELD_ONLINEADDR = "online.addr";
	public static final String CONFIG_FIELD_ENCRYPTKEY = "encryptKey";
	public static final String CONFIG_FIELD_ZHSTRING = "ZH_String";
	
	private static GlobalCfg instance;
	
	/**
	 * get instance
	 * @return GlobalCfg
	 */
	public static GlobalCfg getInstance() {
		if (instance == null) {
			synchronized (GlobalCfg.class) {
				instance = new GlobalCfg();
			}
		}
		return instance;
	}

	/**
	 * load config
	 * @return IData
	 */
	@Override
	protected IData loadConfig() {
		super.loadConfig();
		try {
			if (this.stream != null) {
				return Parser.loadProperties(this.stream);
			}
			return Parser.loadProperties(fileName);
		} catch (Exception e) {
			Utility.error(e);
		}
		return null;
	}

	/**
	 * attr
	 * @param name
	 * @return String
	 */
	public String attr(String name) {
		return cache.getString(name);
	}
	
}