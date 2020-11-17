package model;

public class ImportDetail {
	private int id;
	private int import_id;
	private int product_id;
	private int quantity;
	private int price;
	private int total;
	private Boolean active;
	public ImportDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImportDetail(int id, int import_id, int product_id, int quantity, int price,
			int total, Boolean active) {
		super();
		this.id = id;
		this.import_id = import_id;
		this.product_id = product_id;
		this.quantity = quantity;
		this.price = price;
		this.total = total;
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getImport_id() {
		return import_id;
	}
	public void setImport_id(int import_id) {
		this.import_id = import_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
