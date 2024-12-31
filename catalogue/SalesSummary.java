package catalogue;

public class SalesSummary {
	private final String productNo;
	private final String description;
	private final int totalQuantity;
	private final double totalRevenue;
	
	public SalesSummary(String productNo, String description, int totalQuantity, double totalRevenue) {
		this.productNo = productNo;
		this.description = description;
		this.totalQuantity = totalQuantity;
		this.totalRevenue = totalRevenue;
	}
	
	public String productNo() {
		return productNo;
	}
	
	public String description() {
		return description;
	}
	
	public int totalQuanity() {
		return totalQuantity;
	}
	
	public double totalRevenue() {
		return totalRevenue;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s): %d sold, £%.2f revenue",
				description, productNo, totalQuantity, totalRevenue);
	}
	

}
