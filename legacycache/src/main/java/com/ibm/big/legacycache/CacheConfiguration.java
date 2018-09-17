package com.ibm.big.legacycache;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.distribution.RMICacheReplicatorFactory;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@Configuration
public class CacheConfiguration
{

	private static final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

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

	@Bean(name = "books")
	public Cache books(CacheManager cm)
	{
		logger.debug("********** books method called to instantiate cache ***********");

		Properties props = new Properties();
		props.put("replicateAsynchronously", "true");
		props.put("replicatePuts", "true");
		props.put("replicateUpdates", "true");
		props.put("replicateUpdatesViaCopy", "true");
		props.put("replicateRemovals", "true");
		props.put("asynchronousReplicationIntervalMillis", "1000");
		props.put("asynchronousReplicationMaximumBatchSize", "1000");

		RMICacheReplicatorFactory factory = new RMICacheReplicatorFactory();
		CacheEventListener cel = factory.createCacheEventListener(props);

		net.sf.ehcache.config.CacheConfiguration cc = new net.sf.ehcache.config.CacheConfiguration("books", 100)
				.eternal(false).timeToIdleSeconds(120).timeToLiveSeconds(120).maxElementsOnDisk(1000)
				.diskExpiryThreadIntervalSeconds(60).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.persistence(new PersistenceConfiguration().strategy(Strategy.NONE));

		Cache books = new Cache(cc);
		books.getCacheEventNotificationService().registerListener(cel);

		cm.addCache(books);
		return books;
	}
	
	@Bean(name = "authors")
	public Cache authors(CacheManager cm)
	{
		logger.debug("********** authors method called to instantiate cache ***********");

		Properties props = new Properties();
		props.put("replicateAsynchronously", "true");
		props.put("replicatePuts", "true");
		props.put("replicateUpdates", "true");
		props.put("replicateUpdatesViaCopy", "true");
		props.put("replicateRemovals", "true");
		props.put("asynchronousReplicationIntervalMillis", "1000");
		props.put("asynchronousReplicationMaximumBatchSize", "1000");

		RMICacheReplicatorFactory factory = new RMICacheReplicatorFactory();
		CacheEventListener cel = factory.createCacheEventListener(props);

		net.sf.ehcache.config.CacheConfiguration cc = new net.sf.ehcache.config.CacheConfiguration("authors", 100)
				.eternal(false).timeToIdleSeconds(120).timeToLiveSeconds(120).maxElementsOnDisk(1000)
				.diskExpiryThreadIntervalSeconds(60).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.persistence(new PersistenceConfiguration().strategy(Strategy.NONE));

		Cache authors = new Cache(cc);
		authors.getCacheEventNotificationService().registerListener(cel);

		cm.addCache(authors);
		return authors;
		
	}
}
