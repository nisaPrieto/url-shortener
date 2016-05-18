package url.shortener.controller;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import com.url.shortener.controller.ShortenerService;
import com.url.shortener.controller.URLShortenerControllerImpl;
import com.url.shortener.controller.exception.NotAShortURLException;
import com.url.shortener.controller.exception.ShortURLException;
import com.url.shortener.controller.exception.URLNotFoundException;
import com.url.shortener.view.URLForm;

public class URLShortenerControllerImplTest {
	private static String INITIAL_VIEW = "urlform";
	private static String SUCCESSFUL_VIEW = "result";
	private static String ERROR_VIEW = "error";
	private static String URL_EXAMPLE = "http://www.google.com";

	URLShortenerControllerImpl controller;
	ShortenerService mockShortener;
	Model mockModel;

	@Before
	public void setUp() {

		controller = new URLShortenerControllerImpl();
		mockShortener = createStrictMock(ShortenerService.class);
		controller.setShortener(mockShortener);
		mockModel = createNiceMock(Model.class);
	}

	@Test
	public void testShortenerForm() {
		replay(mockModel);

		String view = controller.shortenerForm(mockModel);

		assertEquals(INITIAL_VIEW, view);
	}

	@Test
	public void shortenerSubmit_happypath() {
		replay(mockModel);

		try {
			expect(mockShortener.getShortURL(URL_EXAMPLE)).andReturn(
					URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (ShortURLException e) {
			fail(e.getMessage());
		}
		replay(mockShortener);

		String view = controller.shortenerSubmit(getURLForm(), mockModel);

		assertEquals(SUCCESSFUL_VIEW, view);
	}

	@Test
	public void shortenerSubmit_malformedURL() {
		replay(mockModel);

		try {
			expect(mockShortener.getShortURL(URL_EXAMPLE)).andThrow(
					new MalformedURLException());
		} catch (MalformedURLException e) {
			// expected
		} catch (ShortURLException e) {
			fail(e.getMessage());
		}
		replay(mockShortener);

		String view = controller.shortenerSubmit(getURLForm(), mockModel);

		assertEquals(ERROR_VIEW, view);
	}

	@Test
	public void shortenerSubmit_shortURL() {
		replay(mockModel);

		try {
			expect(mockShortener.getShortURL(URL_EXAMPLE)).andThrow(
					new ShortURLException());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (ShortURLException e) {
			// expected
		}
		replay(mockShortener);

		String view = controller.shortenerSubmit(getURLForm(), mockModel);

		assertEquals(ERROR_VIEW, view);
	}

	@Test
	public void revertSubmit_happypath() {
		replay(mockModel);

		try {
			expect(mockShortener.getOriginalURL(URL_EXAMPLE)).andReturn(
					URL_EXAMPLE);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}
		replay(mockShortener);

		String view = controller.revertSubmit(getURLForm(), mockModel);

		assertEquals(SUCCESSFUL_VIEW, view);
	}
	
	@Test
	public void revertSubmit_malformedURL() {
		replay(mockModel);

		try {
			expect(mockShortener.getOriginalURL(URL_EXAMPLE)).andThrow(
					new MalformedURLException());
		} catch (MalformedURLException e) {
			//expected
		} catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}
		replay(mockShortener);

		String view = controller.revertSubmit(getURLForm(), mockModel);

		assertEquals(ERROR_VIEW, view);
	}
	
	@Test
	public void revertSubmit_notAShortURL() {
		replay(mockModel);

		try {
			expect(mockShortener.getOriginalURL(URL_EXAMPLE)).andThrow(
					new NotAShortURLException());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (NotAShortURLException e) {
			//expected
		} catch (URLNotFoundException e) {
			fail(e.getMessage());
		}
		replay(mockShortener);

		String view = controller.revertSubmit(getURLForm(), mockModel);

		assertEquals(ERROR_VIEW, view);
	}
	
	@Test
	public void revertSubmit_urlNotFound() {
		replay(mockModel);

		try {
			expect(mockShortener.getOriginalURL(URL_EXAMPLE)).andThrow(
					new URLNotFoundException());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (NotAShortURLException e) {
			fail(e.getMessage());
		} catch (URLNotFoundException e) {
			//expected
		}
		replay(mockShortener);

		String view = controller.revertSubmit(getURLForm(), mockModel);

		assertEquals(ERROR_VIEW, view);
	}
	
	private URLForm getURLForm() {
		URLForm urlform = new URLForm();
		urlform.setUrl(URL_EXAMPLE);
		return urlform;
	}

}
