package model;

public class WareHouse {
	private int id;
	private String name;
	private String location;
	private String style;
	private boolean active;
	public WareHouse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WareHouse(int id, String name, String location, String style, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.style = style;
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
