package com.ibm.big.cachingapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CachingAppController
{
	private static final Logger logger = LoggerFactory.getLogger(CachingAppController.class);

	@Autowired
	BookRepository br;

	@RequestMapping(method = RequestMethod.POST, value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Book> createBook(@RequestBody(required = true) Book book)
	{
		logger.debug("to insert: {}", book);
		ResponseEntity<Book> response;
		book = br.insert(book);
		response = ResponseEntity.ok(book);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Book> getBook(@RequestParam(name = "isbn", required = true) String isbn)
	{
		logger.debug("book with isbn to fetch: {}", isbn);
		ResponseEntity<Book> response;
		Book book = br.getBook(isbn);
		response = ResponseEntity.ok(book);
		return response;
	}
	

}
