package business;

public class GoodForMealTO {
	private boolean dinner;
    private boolean latenight;
    private boolean breakfast;
    private boolean brunch;
    private boolean lunch;
    private boolean dessert;
	public boolean isDinner() {
		return dinner;
	}
	public void setDinner(boolean dinner) {
		this.dinner = dinner;
	}
	public boolean isLatenight() {
		return latenight;
	}
	public void setLatenight(boolean latenight) {
		this.latenight = latenight;
	}
	public boolean isBreakfast() {
		return breakfast;
	}
	public void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}
	public boolean isBrunch() {
		return brunch;
	}
	public void setBrunch(boolean brunch) {
		this.brunch = brunch;
	}
	public boolean isLunch() {
		return lunch;
	}
	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}
	public boolean isDessert() {
		return dessert;
	}
	public void setDessert(boolean dessert) {
		this.dessert = dessert;
	}
    
}
