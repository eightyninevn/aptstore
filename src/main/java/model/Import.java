package model;

public class Import {
	private int id;
	private int supplier_id;
	private int warehouse_id;
	private int employee_id;
	private int amount;
	private String create_at;
	private Boolean active;
	public Import() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Import(int id, int supplier_id, int warehouse_id, int employee_id, int amount, String create_at,
			Boolean active) {
		super();
		this.id = id;
		this.supplier_id = supplier_id;
		this.warehouse_id = warehouse_id;
		this.employee_id = employee_id;
		this.amount = amount;
		this.create_at = create_at;
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}
	public int getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(int warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCreate_at() {
		return create_at;
	}
	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	
}
