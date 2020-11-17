package model;

public class Product {
	private int id;
	private int category_id;
	private int unit_id;
	private String name;
	private String description;
	private int price_sell;
	private Boolean active;
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(int id, int category_id, int unit_id, String name, String description, int price_sell,
			Boolean active) {
		super();
		this.id = id;
		this.category_id = category_id;
		this.unit_id = unit_id;
		this.name = name;
		this.description = description;
		this.price_sell = price_sell;
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice_sell() {
		return price_sell;
	}
	public void setPrice_sell(int price_sell) {
		this.price_sell = price_sell;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
}
