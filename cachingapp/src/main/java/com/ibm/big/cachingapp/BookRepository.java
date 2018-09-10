package com.ibm.big.cachingapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class BookRepository
{
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

	@CachePut(value="books", key="#book.isbn")
	public Book insert(Book book)
	{
		logger.info("book with isbn {} is being added to cache", book.getIsbn());
		return book;		
	}
	
	@CacheEvict(value="books", key="#isbn")
	public void deleteBook(String isbn)
	{
		logger.info("book with isbn {} is being removed from cache", isbn);
	}
	
	
	@Cacheable(value="books", key ="#isbn")
	public Book getBook(String isbn)
	{
		logger.warn("book with isbn {} not found in cache", isbn);
		return null;
	}
}