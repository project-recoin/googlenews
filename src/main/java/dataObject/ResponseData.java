package dataObject;

/**
 * 
 * Taken from https://github.com/emichaux/SimplisticNews
 * Under MIT license
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class ResponseData {

	@Expose
	private List<Result> results = new ArrayList<Result>();
	@Expose
	private Cursor cursor;

	/**
	 * 
	 * @return The results
	 */
	public List<Result> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 *            The results
	 */
	public void setResults(List<Result> results) {
		this.results = results;
	}

	/**
	 * 
	 * @return The cursor
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * 
	 * @param cursor
	 *            The cursor
	 */
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

}
