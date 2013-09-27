package com.viamhealth.android.model;

public class MedicalData {
	String id,name,detail,start_timestamp,repeat_hour,repeat_day,repeat_mode,repeat_min,repeat_weekday,repeat_day_interval;

	boolean checked=false;
	
	
	public MedicalData() {
		super();
	}
	public MedicalData(String id, String name, String detail,
			String start_timestamp, String repeat_hour, String repeat_day,
			String repeat_mode, String repeat_min, String repeat_weekday,
			String repeat_day_interval) {
		super();
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.start_timestamp = start_timestamp;
		this.repeat_hour = repeat_hour;
		this.repeat_day = repeat_day;
		this.repeat_mode = repeat_mode;
		this.repeat_min = repeat_min;
		this.repeat_weekday = repeat_weekday;
		this.repeat_day_interval = repeat_day_interval;
	}
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStart_timestamp() {
		return start_timestamp;
	}

	public void setStart_timestamp(String start_timestamp) {
		this.start_timestamp = start_timestamp;
	}

	public String getRepeat_hour() {
		return repeat_hour;
	}

	public void setRepeat_hour(String repeat_hour) {
		this.repeat_hour = repeat_hour;
	}

	public String getRepeat_day() {
		return repeat_day;
	}

	public void setRepeat_day(String repeat_day) {
		this.repeat_day = repeat_day;
	}

	public String getRepeat_mode() {
		return repeat_mode;
	}

	public void setRepeat_mode(String repeat_mode) {
		this.repeat_mode = repeat_mode;
	}

	public String getRepeat_min() {
		return repeat_min;
	}

	public void setRepeat_min(String repeat_min) {
		this.repeat_min = repeat_min;
	}

	public String getRepeat_weekday() {
		return repeat_weekday;
	}

	public void setRepeat_weekday(String repeat_weekday) {
		this.repeat_weekday = repeat_weekday;
	}

	public String getRepeat_day_interval() {
		return repeat_day_interval;
	}

	public void setRepeat_day_interval(String repeat_day_interval) {
		this.repeat_day_interval = repeat_day_interval;
	}
	
	
}
