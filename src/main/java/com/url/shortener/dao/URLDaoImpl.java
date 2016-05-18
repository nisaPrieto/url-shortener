package com.url.shortener.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.url.shortener.model.URL;

@Repository
public class URLDaoImpl implements URLDao {

	private static String SHORTURL_PARAM = "surlparam";
	private static String LONGURL_PARAM = "lurlparam";
	private static String SELECT_URLS_BY_SHORTURL = "SELECT * FROM urls WHERE shorturl=:"
			+ SHORTURL_PARAM;
	private static String SELECT_URLS_BY_LONGURL = "SELECT * FROM urls WHERE longurl=:"
			+ LONGURL_PARAM;
	private static String INSERT_URLS = "INSERT INTO urls (shorturl, longurl) VALUES (:"
			+ SHORTURL_PARAM + ", :" + LONGURL_PARAM + ")";

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public URL findByShortURL(String shortURL) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(SHORTURL_PARAM, shortURL);
		String sql = SELECT_URLS_BY_SHORTURL;

		URL result = excecuteQuery(sql, params);

		return result;
	}

	public URL findByLongURL(String longURL) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LONGURL_PARAM, longURL);
		String sql = SELECT_URLS_BY_LONGURL;

		URL result = excecuteQuery(sql, params);

		return result;
	}

	public void save(String shortURL, String longURL) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(SHORTURL_PARAM, shortURL);
		params.put(LONGURL_PARAM, longURL);
		namedParameterJdbcTemplate.update(INSERT_URLS, params);
	}

	private URL excecuteQuery(String sql, Map<String, Object> params) {
		URL result = null;
		try {
			result = namedParameterJdbcTemplate.queryForObject(sql, params,
					new URLMapper());
		} catch (DataAccessException e){
			// no results for the sql
		}
		return result;
	}

	private static final class URLMapper implements RowMapper<URL> {

		public URL mapRow(ResultSet rs, int rowNum) throws SQLException {
			URL url = new URL();
			url.setId(rs.getInt("id"));
			url.setShorturl(rs.getString("shorturl"));
			url.setLongurl(rs.getString("longurl"));
			return url;
		}
	}
}
