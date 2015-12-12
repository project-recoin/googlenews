package dataObject.googlenews;

/**
 * 
 * Taken from https://github.com/emichaux/SimplisticNews
 * Under MIT license
 */

import com.google.gson.annotations.Expose;

public class RelatedStory {

	@Expose
	private String unescapedUrl;
	@Expose
	private String url;
	@Expose
	private String title;
	@Expose
	private String titleNoFormatting;
	@Expose
	private String location;
	@Expose
	private String publisher;
	@Expose
	private String publishedDate;
	@Expose
	private String signedRedirectUrl;
	@Expose
	private String language;

	/**
	 * 
	 * @return The unescapedUrl
	 */
	public String getUnescapedUrl() {
		return unescapedUrl;
	}

	/**
	 * 
	 * @param unescapedUrl
	 *            The unescapedUrl
	 */
	public void setUnescapedUrl(String unescapedUrl) {
		this.unescapedUrl = unescapedUrl;
	}

	/**
	 * 
	 * @return The url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 *            The url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return The titleNoFormatting
	 */
	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}

	/**
	 * 
	 * @param titleNoFormatting
	 *            The titleNoFormatting
	 */
	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}

	/**
	 * 
	 * @return The location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 *            The location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @return The publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * 
	 * @param publisher
	 *            The publisher
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * 
	 * @return The publishedDate
	 */
	public String getPublishedDate() {
		return publishedDate;
	}

	/**
	 * 
	 * @param publishedDate
	 *            The publishedDate
	 */
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	/**
	 * 
	 * @return The signedRedirectUrl
	 */
	public String getSignedRedirectUrl() {
		return signedRedirectUrl;
	}

	/**
	 * 
	 * @param signedRedirectUrl
	 *            The signedRedirectUrl
	 */
	public void setSignedRedirectUrl(String signedRedirectUrl) {
		this.signedRedirectUrl = signedRedirectUrl;
	}

	/**
	 * 
	 * @return The language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @param language
	 *            The language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
