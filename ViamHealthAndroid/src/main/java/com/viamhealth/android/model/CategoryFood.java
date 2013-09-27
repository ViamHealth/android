package com.viamhealth.android.model;

public class CategoryFood {
	String id,name,calories,multiplier,quantity,count;

	public CategoryFood(String id, String name, String calories,
			String multiplier,String quantity,String count) {
		super();
		this.id = id;
		this.name = name;
		this.calories = calories;
		this.multiplier = multiplier;
		this.quantity=quantity;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
}
