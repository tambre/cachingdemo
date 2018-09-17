package com.ibm.big.legacycache;

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
public class LegacyCacheController
{
	private static final Logger logger = LoggerFactory.getLogger(LegacyCacheController.class);

	@Autowired
	BookRepository br;

	@Autowired
	AuthorRepository ar;

	@RequestMapping(method = RequestMethod.POST, value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Book> createBook(@RequestBody(required = true) Book book)
	{
		logger.debug("book to insert: {}", book);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/author", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Author> createAuthor(@RequestBody(required = true) Author author)
	{
		logger.debug("author to insert: {}", author);
		ResponseEntity<Author> response;
		author = ar.insert(author);
		response = ResponseEntity.ok(author);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/author", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Author> getAuthor(@RequestParam(name = "id", required = true) String id)
	{
		logger.debug("author with isbn to fetch: {}", id);
		ResponseEntity<Author> response;
		Author author = ar.getAuthor(id);
		response = ResponseEntity.ok(author);
		return response;
	}	
}
