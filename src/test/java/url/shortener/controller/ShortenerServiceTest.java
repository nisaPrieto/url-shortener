package url.shortener.controller;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.url.shortener.controller.ShortenerService;
import com.url.shortener.controller.exception.NotAShortURLException;
import com.url.shortener.controller.exception.ShortURLException;
import com.url.shortener.controller.exception.URLNotFoundException;
import com.url.shortener.dao.URLDao;
import com.url.shortener.model.URL;

public class ShortenerServiceTest {

	private static String PREFIX = "http://cl.ip/";
	private static String SHORT_URL_EXAMPLE = "http://cl.ip/dF53B";
	private static String ORIGINAL_URL_EXAMPLE = "http://www.google.com";
	private static String WRONG_URL_EXAMPLE = "not an URL";

	ShortenerService shortener;
	private URLDao mockDao;

	@Before
	public void setUp() {

		shortener = new ShortenerService();
		mockDao = createStrictMock(URLDao.class);
		shortener.setUrlDao(mockDao);
	}

	@Test
	public void testGenerateShortURL() {
		String url = shortener.generateShortURL();

		System.out.println(url);

		assertTrue(url.startsWith(PREFIX));

		assertTrue(url.length() >= 17); // http://cl.ip/ + 4 chars
		assertTrue(url.length() <= 23); // http://cl.ip/ + 10 chars

	}

	@Test
	public void testGetShortURL_happyPath() {
		String shorturl = "";
		expect(mockDao.findByLongURL(EasyMock.anyString())).andReturn(null);
		mockDao.save(EasyMock.anyString(), EasyMock.anyString());
		expectLastCall();
		replay(mockDao);

		try {
			shorturl = shortener.getShortURL(ORIGINAL_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail();
		} catch (ShortURLException e) {
			fail();
		}

		assertTrue(shorturl.startsWith("http://cl.ip/"));

		assertTrue(shorturl.length() >= 17); // http://cl.ip/ + 4 chars
		assertTrue(shorturl.length() <= 23); // http://cl.ip/ + 10 chars

	}

	@Test
	public void testGetShortURL_shortURLExist() {
		String shorturl = "";
		expect(mockDao.findByLongURL(EasyMock.anyString())).andReturn(
				getURLExample());

		replay(mockDao);

		try {
			shorturl = shortener.getShortURL(ORIGINAL_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail();
		} catch (ShortURLException e) {
			fail();
		}

		assertEquals(SHORT_URL_EXAMPLE, shorturl);

	}

	@Test
	public void testGetShortURL_malformedURL() {
		try {
			shortener.getShortURL(WRONG_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			return;
		} catch (ShortURLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testGetShortURL_shortURL() {
		try {
			shortener.getShortURL(SHORT_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail();
		} catch (ShortURLException e) {
			return;
		}
		fail();
	}

	@Test
	public void testGetOriginalURL_happyPath() {
		String longurl = "";
		
		expect(mockDao.findByShortURL(SHORT_URL_EXAMPLE)).andReturn(
				getURLExample());
		replay(mockDao);

		try {
			longurl = shortener.getOriginalURL(SHORT_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(ORIGINAL_URL_EXAMPLE,longurl);
	}

	@Test
	public void testGetOriginalURL_malformedURL() {
		try {
			shortener.getOriginalURL(WRONG_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			return;
		}catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}
		fail();
	}
	
	@Test
	public void testGetOriginalURL_notAShortURL() {
		try {
			shortener.getOriginalURL(ORIGINAL_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}catch (NotAShortURLException e) {
			return;
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}
		fail();
	}
	
	@Test
	public void testGetOriginalURL_urlNotFound() {
		expect(mockDao.findByShortURL(SHORT_URL_EXAMPLE)).andReturn(
				null);
		replay(mockDao);
		
		try {
			shortener.getOriginalURL(SHORT_URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			return;
		}
		fail();
	}
	
	private URL getURLExample() {
		URL url = new URL();
		url.setLongurl(ORIGINAL_URL_EXAMPLE);
		url.setShorturl(SHORT_URL_EXAMPLE);
		return url;
	}

}
