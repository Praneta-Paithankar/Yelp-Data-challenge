package structure;

import java.util.Date;
/*
 * created by Praneta Paithankar(ppaithan)
 */
//Pojo class
public class Review {
	private String  review_id;
	private String user_id;
	private String business_id;
	private String stars;
	private Date date;
    private String text;
    private int useful;
    private int funny;
    private int cool;
	public String getReview_id() {
		return review_id;
	}
	public void setReview_id(String review_id) {
		this.review_id = review_id;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUseful() {
		return useful;
	}
	public void setUseful(int useful) {
		this.useful = useful;
	}
	public int getFunny() {
		return funny;
	}
	public void setFunny(int funny) {
		this.funny = funny;
	}
	public int getCool() {
		return cool;
	}
	public void setCool(int cool) {
		this.cool = cool;
	}
}
