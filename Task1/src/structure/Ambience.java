package structure;
/*
 * created by Praneta Paithankar(ppaithan)
 */
//Pojo class
public class Ambience {
	private boolean hipster;
	private boolean classy;
    private boolean touristy;
    private boolean divey;
    private boolean casual;
    private boolean intimate;
    private boolean romantic;
    private boolean trendy;
    private boolean upscale;

	
	public boolean isHipster() {
		return hipster;
	}


	public void setHipster(boolean hipster) {
		this.hipster = hipster;
	}


	public boolean isClassy() {
		return classy;
	}


	public void setClassy(boolean classy) {
		this.classy = classy;
	}


	public boolean isTouristy() {
		return touristy;
	}


	public void setTouristy(boolean touristy) {
		this.touristy = touristy;
	}


	public boolean isDivey() {
		return divey;
	}


	public void setDivey(boolean divey) {
		this.divey = divey;
	}


	public boolean isCasual() {
		return casual;
	}


	public void setCasual(boolean casual) {
		this.casual = casual;
	}


	public boolean isIntimate() {
		return intimate;
	}


	public void setIntimate(boolean intimate) {
		this.intimate = intimate;
	}


	public boolean isRomantic() {
		return romantic;
	}


	public void setRomantic(boolean romantic) {
		this.romantic = romantic;
	}


	public boolean isTrendy() {
		return trendy;
	}


	public void setTrendy(boolean trendy) {
		this.trendy = trendy;
	}


	public boolean isUpscale() {
		return upscale;
	}


	public void setUpscale(boolean upscale) {
		this.upscale = upscale;
	}


	@Override
	public String toString() {
		return "Ambience [hipster=" + hipster + ", classy=" + classy + ", touristy=" + touristy + ", divey=" + divey
				+ ", casual=" + casual + ", intimate=" + intimate + ", romantic=" + romantic + ", trendy=" + trendy
				+ ", upscale=" + upscale + "]";
	}

}
