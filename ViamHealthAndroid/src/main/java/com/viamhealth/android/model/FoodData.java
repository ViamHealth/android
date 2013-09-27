package com.viamhealth.android.model;

public class FoodData {
	String id,name,quantity,quantity_unit,calories, total_fat, saturated_fat,polyunsaturated_fat,monounsaturated_fat,
		   trans_fat,cholesterol,sodium,potassium,total_carbohydrates, dietary_fiber, sugars, protein, vitamin_a, vitamin_c,
		   calcium,iron,calories_unit,total_fat_unit, saturated_fat_unit, polyunsaturated_fat_unit, monounsaturated_fat_unit,
		   trans_fat_unit,cholesterol_unit,sodium_unit, potassium_unit, total_carbohydrates_unit, dietary_fiber_unit, sugars_unit,
		   protein_unit, vitamin_a_unit, vitamin_c_unit, calcium_unit, iron_unit;

	
	public FoodData(String id, String name, String quantity,
			String quantity_unit, String calories, String total_fat,
			String saturated_fat, String polyunsaturated_fat,
			String monounsaturated_fat, String trans_fat, String cholesterol,
			String sodium, String potassium, String total_carbohydrates,
			String dietary_fiber, String sugars, String protein,
			String vitamin_a, String vitamin_c, String calcium, String iron,
			String calories_unit, String total_fat_unit,
			String saturated_fat_unit, String polyunsaturated_fat_unit,
			String monounsaturated_fat_unit, String trans_fat_unit,
			String cholesterol_unit, String sodium_unit, String potassium_unit,
			String total_carbohydrates_unit, String dietary_fiber_unit,
			String sugars_unit, String protein_unit, String vitamin_a_unit,
			String vitamin_c_unit, String calcium_unit, String iron_unit) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.quantity_unit = quantity_unit;
		this.calories = calories;
		this.total_fat = total_fat;
		this.saturated_fat = saturated_fat;
		this.polyunsaturated_fat = polyunsaturated_fat;
		this.monounsaturated_fat = monounsaturated_fat;
		this.trans_fat = trans_fat;
		this.cholesterol = cholesterol;
		this.sodium = sodium;
		this.potassium = potassium;
		this.total_carbohydrates = total_carbohydrates;
		this.dietary_fiber = dietary_fiber;
		this.sugars = sugars;
		this.protein = protein;
		this.vitamin_a = vitamin_a;
		this.vitamin_c = vitamin_c;
		this.calcium = calcium;
		this.iron = iron;
		this.calories_unit = calories_unit;
		this.total_fat_unit = total_fat_unit;
		this.saturated_fat_unit = saturated_fat_unit;
		this.polyunsaturated_fat_unit = polyunsaturated_fat_unit;
		this.monounsaturated_fat_unit = monounsaturated_fat_unit;
		this.trans_fat_unit = trans_fat_unit;
		this.cholesterol_unit = cholesterol_unit;
		this.sodium_unit = sodium_unit;
		this.potassium_unit = potassium_unit;
		this.total_carbohydrates_unit = total_carbohydrates_unit;
		this.dietary_fiber_unit = dietary_fiber_unit;
		this.sugars_unit = sugars_unit;
		this.protein_unit = protein_unit;
		this.vitamin_a_unit = vitamin_a_unit;
		this.vitamin_c_unit = vitamin_c_unit;
		this.calcium_unit = calcium_unit;
		this.iron_unit = iron_unit;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantity_unit() {
		return quantity_unit;
	}

	public void setQuantity_unit(String quantity_unit) {
		this.quantity_unit = quantity_unit;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getTotal_fat() {
		return total_fat;
	}

	public void setTotal_fat(String total_fat) {
		this.total_fat = total_fat;
	}

	public String getSaturated_fat() {
		return saturated_fat;
	}

	public void setSaturated_fat(String saturated_fat) {
		this.saturated_fat = saturated_fat;
	}

	public String getPolyunsaturated_fat() {
		return polyunsaturated_fat;
	}

	public void setPolyunsaturated_fat(String polyunsaturated_fat) {
		this.polyunsaturated_fat = polyunsaturated_fat;
	}

	public String getMonounsaturated_fat() {
		return monounsaturated_fat;
	}

	public void setMonounsaturated_fat(String monounsaturated_fat) {
		this.monounsaturated_fat = monounsaturated_fat;
	}

	public String getTrans_fat() {
		return trans_fat;
	}

	public void setTrans_fat(String trans_fat) {
		this.trans_fat = trans_fat;
	}

	public String getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(String cholesterol) {
		this.cholesterol = cholesterol;
	}

	public String getSodium() {
		return sodium;
	}

	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	public String getPotassium() {
		return potassium;
	}

	public void setPotassium(String potassium) {
		this.potassium = potassium;
	}

	public String getTotal_carbohydrates() {
		return total_carbohydrates;
	}

	public void setTotal_carbohydrates(String total_carbohydrates) {
		this.total_carbohydrates = total_carbohydrates;
	}

	public String getDietary_fiber() {
		return dietary_fiber;
	}

	public void setDietary_fiber(String dietary_fiber) {
		this.dietary_fiber = dietary_fiber;
	}

	public String getSugars() {
		return sugars;
	}

	public void setSugars(String sugars) {
		this.sugars = sugars;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	public String getVitamin_a() {
		return vitamin_a;
	}

	public void setVitamin_a(String vitamin_a) {
		this.vitamin_a = vitamin_a;
	}

	public String getVitamin_c() {
		return vitamin_c;
	}

	public void setVitamin_c(String vitamin_c) {
		this.vitamin_c = vitamin_c;
	}

	public String getCalcium() {
		return calcium;
	}

	public void setCalcium(String calcium) {
		this.calcium = calcium;
	}

	public String getIron() {
		return iron;
	}

	public void setIron(String iron) {
		this.iron = iron;
	}

	public String getCalories_unit() {
		return calories_unit;
	}

	public void setCalories_unit(String calories_unit) {
		this.calories_unit = calories_unit;
	}

	public String getTotal_fat_unit() {
		return total_fat_unit;
	}

	public void setTotal_fat_unit(String total_fat_unit) {
		this.total_fat_unit = total_fat_unit;
	}

	public String getSaturated_fat_unit() {
		return saturated_fat_unit;
	}

	public void setSaturated_fat_unit(String saturated_fat_unit) {
		this.saturated_fat_unit = saturated_fat_unit;
	}

	public String getPolyunsaturated_fat_unit() {
		return polyunsaturated_fat_unit;
	}

	public void setPolyunsaturated_fat_unit(String polyunsaturated_fat_unit) {
		this.polyunsaturated_fat_unit = polyunsaturated_fat_unit;
	}

	public String getMonounsaturated_fat_unit() {
		return monounsaturated_fat_unit;
	}

	public void setMonounsaturated_fat_unit(String monounsaturated_fat_unit) {
		this.monounsaturated_fat_unit = monounsaturated_fat_unit;
	}

	public String getTrans_fat_unit() {
		return trans_fat_unit;
	}

	public void setTrans_fat_unit(String trans_fat_unit) {
		this.trans_fat_unit = trans_fat_unit;
	}

	public String getCholesterol_unit() {
		return cholesterol_unit;
	}

	public void setCholesterol_unit(String cholesterol_unit) {
		this.cholesterol_unit = cholesterol_unit;
	}

	public String getSodium_unit() {
		return sodium_unit;
	}

	public void setSodium_unit(String sodium_unit) {
		this.sodium_unit = sodium_unit;
	}

	public String getPotassium_unit() {
		return potassium_unit;
	}

	public void setPotassium_unit(String potassium_unit) {
		this.potassium_unit = potassium_unit;
	}

	public String getTotal_carbohydrates_unit() {
		return total_carbohydrates_unit;
	}

	public void setTotal_carbohydrates_unit(String total_carbohydrates_unit) {
		this.total_carbohydrates_unit = total_carbohydrates_unit;
	}

	public String getDietary_fiber_unit() {
		return dietary_fiber_unit;
	}

	public void setDietary_fiber_unit(String dietary_fiber_unit) {
		this.dietary_fiber_unit = dietary_fiber_unit;
	}

	public String getSugars_unit() {
		return sugars_unit;
	}

	public void setSugars_unit(String sugars_unit) {
		this.sugars_unit = sugars_unit;
	}

	public String getProtein_unit() {
		return protein_unit;
	}

	public void setProtein_unit(String protein_unit) {
		this.protein_unit = protein_unit;
	}

	public String getVitamin_a_unit() {
		return vitamin_a_unit;
	}

	public void setVitamin_a_unit(String vitamin_a_unit) {
		this.vitamin_a_unit = vitamin_a_unit;
	}

	public String getVitamin_c_unit() {
		return vitamin_c_unit;
	}

	public void setVitamin_c_unit(String vitamin_c_unit) {
		this.vitamin_c_unit = vitamin_c_unit;
	}

	public String getCalcium_unit() {
		return calcium_unit;
	}

	public void setCalcium_unit(String calcium_unit) {
		this.calcium_unit = calcium_unit;
	}

	public String getIron_unit() {
		return iron_unit;
	}

	public void setIron_unit(String iron_unit) {
		this.iron_unit = iron_unit;
	}
	
	
}
