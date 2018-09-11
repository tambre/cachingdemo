package com.ibm.big.legacycache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.CacheManager;


@Configuration
public class CacheConfiguration
{
	@Value("${ehcache.configuration}")
	String configFile;

	@Bean
	public CacheManager cacheManager()
	{
		ClassPathResource cpr = new ClassPathResource(configFile);
		try
		{
			return CacheManager.newInstance(cpr.getInputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
}
