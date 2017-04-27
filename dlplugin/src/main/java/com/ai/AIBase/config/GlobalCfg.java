package com.ai.AIBase.config;

import com.ai.AIBase.util.Parser;
import com.ai.AIBase.util.Utility;
import com.ailk.common.data.IData;

import java.io.InputStream;

public class GlobalCfg extends AbstractCfg {
	public static final String CONFIG_FIELD_ONLINEADDR = "online.addr";
	
	private static GlobalCfg instance;
	
	/**
	 * get instance
	 * @return GlobalCfg
	 */
	public static GlobalCfg getInstance(String configFileName) {
		if (instance == null) {
			synchronized (GlobalCfg.class) {
				instance = new GlobalCfg();
				instance.setFileName(configFileName);
				instance.cache = instance.loadConfig();
			}
		}
		return instance;
	}

	public static GlobalCfg getInstance(InputStream stream) {
		if (instance == null) {
			synchronized (GlobalCfg.class) {
				instance = new GlobalCfg();
				instance.setStream(stream);
				instance.cache = instance.loadConfig();
			}
		}
		return instance;
	}

	/**
	 * load config
	 * @return IData
	 */
	protected IData loadConfig() {
		try {
			if (this.getStream() != null) {
				return Parser.loadProperties(this.getStream());
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