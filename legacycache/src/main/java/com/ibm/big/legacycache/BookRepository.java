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
public class BookRepository
{
	private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

	@Autowired
	CacheManager cacheManager;

	private void createCache()
	{

		logger.debug("********** books method called to instantiate cache ***********");

		Properties props = new Properties();
		props.put("replicateAsynchronously", "true");
		props.put("replicatePuts", "true");
		props.put("replicateUpdates", "true");
		props.put("replicateUpdatesViaCopy", "true");
		props.put("replicateRemovals", "true");
		props.put("asynchronousReplicationIntervalMillis", "true");
		props.put("asynchronousReplicationMaximumBatchSize", "true");

		RMICacheReplicatorFactory factory = new RMICacheReplicatorFactory();
		CacheEventListener cel = factory.createCacheEventListener(props);

		net.sf.ehcache.config.CacheConfiguration cc = new net.sf.ehcache.config.CacheConfiguration("books", 100)
				.eternal(false).timeToIdleSeconds(120).timeToLiveSeconds(120).maxElementsOnDisk(1000)
				.diskExpiryThreadIntervalSeconds(60).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.persistence(new PersistenceConfiguration().strategy(Strategy.NONE));

		Cache books = new Cache(cc);
		books.getCacheEventNotificationService().registerListener(cel);

		cacheManager.addCache(books);
	}

	private synchronized Cache getCache()
	{

		if (cacheManager.getCache("books") == null)
		{
			createCache();
		}

		return cacheManager.getCache("books");

	}

	public Book insert(Book book)
	{
		// START: Debug code
		Map<String, CacheManagerPeerProvider> providermap = cacheManager.getCacheManagerPeerProviders();

		Cache cache = getCache(); // cacheManager.getCache("books");

		logger.debug("providerMap: {}", providermap);

		providermap.entrySet().forEach(e -> {

			logger.debug("provider map entry: {} = {}", e.getKey(),
					e.getValue().listRemoteCachePeers(cacheManager.getCache("books")));
		});
		// END: Debug code

		cache.put(new Element(book.getIsbn(), book));
		logger.info("book with isbn {} is being added to cache", book.getIsbn());
		return book;
	}

	public void deleteBook(String isbn)
	{
		logger.info("book with isbn {} is being removed from cache", isbn);
	}

	public Book getBook(String isbn)
	{
		Cache cache = getCache(); // cacheManager.getCache("books");
		Element e = cache.get(isbn);
		if (e != null)
		{
			logger.warn("book with isbn {} is {}", isbn, e.getObjectValue());
			return (Book) e.getObjectValue();
		}
		logger.warn("book with isbn {} not found in cache", isbn);
		return null;
	}
}