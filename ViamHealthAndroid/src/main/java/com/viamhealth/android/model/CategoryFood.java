package com.viamhealth.android.model;

public class CategoryFood {
	String id,name,calories,multiplier,quantity,count,food_item,fat,cholesterol,sugar;

	public CategoryFood(String id,String food_item, String name, String calories,
			String multiplier,String quantity,String count,String fat,String cholesterol,String sugar) {
		super();
		this.id = id;
		this.name = name;
        this.food_item=food_item;
		this.calories = calories;
		this.multiplier = multiplier;
		this.quantity=quantity;
		this.count = count;
        this.fat=fat;
        this.cholesterol=cholesterol;
        this.sugar=sugar;
	}

    public String getFat()
    {
      return fat;
    }

    public void setFat(String fat)
    {
        this.fat=fat;
    }

    public String getCholesterol()
    {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol)
    {
         this.cholesterol=cholesterol;
    }

    public String getSugar()
    {
        return sugar;
    }

    public void setSugar(String sugar)
    {
        this.sugar=sugar;
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

    public String getFoodItem()
    {
        return food_item;
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
