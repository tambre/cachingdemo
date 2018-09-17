package com.ibm.big.legacycache;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.RMICacheReplicatorFactory;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@Component
public class AuthorRepository
{
	private static final Logger logger = LoggerFactory.getLogger(AuthorRepository.class);

	@Autowired
	CacheManager cacheManager;

	private void createCache()
	{

		logger.debug("********** author method called to instantiate cache ***********");

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

		cacheManager.addCache(authors);
	}

	private synchronized Cache getCache()
	{

		if (cacheManager.getCache("authors") == null)
		{
			createCache();
		}

		return cacheManager.getCache("authors");

	}

	public Author insert(Author author)
	{
		// START: Debug code
		Map<String, CacheManagerPeerProvider> providermap = cacheManager.getCacheManagerPeerProviders();

		Cache cache = getCache();

		logger.debug("providerMap: {}", providermap);

		providermap.entrySet().forEach(e -> {

			logger.debug("provider map entry: {} = {}", e.getKey(),
					e.getValue().listRemoteCachePeers(cacheManager.getCache("authors")));
		});
		// END: Debug code

		cache.put(new Element(author.getId(), author));
		logger.info("author with id {} is being added to cache", author.getId());
		return author;
	}

	public void delete(String id)
	{
		logger.info("author with isbn {} is being removed from cache", id);
	}

	public Author getAuthor(String id)
	{
		Cache cache = getCache(); 
		Element e = cache.get(id);
		if (e != null)
		{
			logger.warn("author with id {} is {}", id, e.getObjectValue());
			return (Author) e.getObjectValue();
		}
		logger.warn("author with id {} not found in cache", id);
		return null;
	}
}