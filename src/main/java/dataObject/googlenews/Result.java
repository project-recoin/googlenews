package dataObject.googlenews;

/**
 * 
 * Taken from https://github.com/emichaux/SimplisticNews
 * Modified by: Saud Aljaloud 
 * sza1g10@ecs.soton.ac.uk
 * Under MIT license
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Result {

	@Expose
	private String GsearchResultClass;
	@Expose
	private String clusterUrl;
	@Expose
	private String content;
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
	@Expose
	private Image image;
	@Expose
	private List<RelatedStory> relatedStories = new ArrayList<RelatedStory>();

	/**
	 * 
	 * @return The GsearchResultClass
	 */
	public String getGsearchResultClass() {
		return GsearchResultClass;
	}

	/**
	 * 
	 * @param GsearchResultClass
	 *            The GsearchResultClass
	 */
	public void setGsearchResultClass(String GsearchResultClass) {
		this.GsearchResultClass = GsearchResultClass;
	}

	/**
	 * 
	 * @return The clusterUrl
	 */
	public String getClusterUrl() {
		return clusterUrl;
	}

	/**
	 * 
	 * @param clusterUrl
	 *            The clusterUrl
	 */
	public void setClusterUrl(String clusterUrl) {
		this.clusterUrl = clusterUrl;
	}

	/**
	 * 
	 * @return The content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            The content
	 */
	public void setContent(String content) {
		this.content = content;
	}

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

	/**
	 * 
	 * @return The image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * 
	 * @param image
	 *            The image
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * 
	 * @return The relatedStories
	 */
	public List<RelatedStory> getRelatedStories() {
		return relatedStories;
	}

	/**
	 * 
	 * @param relatedStories
	 *            The relatedStories
	 */
	public void setRelatedStories(List<RelatedStory> relatedStories) {
		this.relatedStories = relatedStories;
	}

}
