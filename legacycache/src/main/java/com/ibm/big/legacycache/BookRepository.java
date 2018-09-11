package com.ibm.big.legacycache;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;

@Component
public class BookRepository
{
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

    @Autowired
    CacheManager cacheManager;
    
	public Book insert(Book book)
	{
		//START: Debug code
		Map<String, CacheManagerPeerProvider> providermap = cacheManager.getCacheManagerPeerProviders();
		
		logger.debug("providerMap: {}", providermap);
		
		
		providermap.entrySet().forEach(e -> {
			
			logger.debug("provider map entry: {} = {}", e.getKey(), e.getValue().listRemoteCachePeers(cacheManager.getCache("books")));
		});
		//END: Debug code
		
		Cache cache = cacheManager.getCache("books");
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
		Cache cache = cacheManager.getCache("books");
		Element e = cache.get(isbn);
		if (e != null)
		{
			logger.warn("book with isbn {} is {}", isbn, e.getObjectValue());
			return (Book)e.getObjectValue();
		}
		logger.warn("book with isbn {} not found in cache", isbn);
		return null;
	}
}