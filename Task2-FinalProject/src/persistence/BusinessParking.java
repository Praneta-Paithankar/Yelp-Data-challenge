package persistence;
/*
 * created by Praneta Paithankar(ppaithan)
 */
public class BusinessParking {
	private String street;
    private String garage;
	private String lot;
    private String valet;
    private String validated;
    
   
    @Override
   	public String toString() {
   		return "Parking [street=" + street + ", garage=" + garage + ", lot=" + lot + ", valet=" + valet + ", validated="
   				+ validated + "]";
   	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getGarage() {
		return garage;
	}
	public void setGarage(String garage) {
		this.garage = garage;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getValet() {
		return valet;
	}
	public void setValet(String valet) {
		this.valet = valet;
	}
	public String getValidated() {
		return validated;
	}
	public void setValidated(String validated) {
		this.validated = validated;
	}
}
