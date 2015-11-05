package dataObject;

/**
 * 
 * Taken from https://github.com/emichaux/SimplisticNews
 * Under MIT license
 */

import com.google.gson.annotations.Expose;

public class Page {

	@Expose
	private String start;
	@Expose
	private Integer label;

	/**
	 * 
	 * @return The start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * 
	 * @param start
	 *            The start
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * 
	 * @return The label
	 */
	public Integer getLabel() {
		return label;
	}

	/**
	 * 
	 * @param label
	 *            The label
	 */
	public void setLabel(Integer label) {
		this.label = label;
	}

}
