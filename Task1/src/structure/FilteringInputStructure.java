package structure;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilteringInputStructure {
	private String user_id;
	private String business_id;
	private String stars;
	
	public FilteringInputStructure(String user_id, String business_id, String stars) {
		super();
		this.user_id = user_id;
		this.business_id = business_id;
		this.stars = stars;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String toCsvRow() {
	    return Stream.of(user_id, business_id, stars)
	            .map(value -> value.replaceAll("\"", "\"\""))
	            .map(value -> Stream.of("\"", ",").anyMatch(value::contains) ? "\"" + value + "\"" : value)
	            .collect(Collectors.joining(","));
	}

}
