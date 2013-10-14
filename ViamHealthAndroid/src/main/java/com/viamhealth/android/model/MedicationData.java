package com.viamhealth.android.model;

public class MedicationData {
	String id,name,type,details,morning_count,afternoon_count,evening_count,night_count,user,start_timestamp,repeat_hour,repeat_day,repeat_mode,repeat_min,repeat_weekday,repeat_day_interval,start_date,end_date;

	boolean checked=false;
	
	
	public MedicationData() {
		super();
	}

	public MedicationData(String id, String name,String type,String details,
			String morning_count, String afternoon_count, String evening_count,
			String night_count, String user, String start_timestamp,
			String repeat_hour, String repeat_day, String repeat_mode,
			String repeat_min, String repeat_weekday, String repeat_day_interval,String start_date,String end_date) {
		super();
		this.id = id;
		this.name = name;
        this.type=type;
		this.details = details;
		this.morning_count = morning_count;
		this.afternoon_count = afternoon_count;
		this.evening_count = evening_count;
		this.night_count = night_count;
		this.user = user;
		this.start_timestamp = start_timestamp;
		this.repeat_hour = repeat_hour;
		this.repeat_day = repeat_day;
		this.repeat_mode = repeat_mode;
		this.repeat_min = repeat_min;
		this.repeat_weekday = repeat_weekday;
		this.repeat_day_interval = repeat_day_interval;
        this.start_date=start_date;
        this.end_date=end_date;
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

	public String getDetails() {
		return details;
	}

    public void setType(String typ)
    {
        this.type=typ;
    }

    public String getType()
    {
        return type;
    }

	public void setDetails(String details) {
		this.details = details;
	}

	public String getMorning_count() {
		return morning_count;
	}

	public void setMorning_count(String morning_count) {
		this.morning_count = morning_count;
	}

    public void setStart_date(String start_dt)
    {
        this.start_date=start_dt;
    }

    public void setEnd_date()
    {
        this.end_date=end_date;
    }

    public String getEnd_date()
    {
        return end_date;
    }


    public String getStart_date()
    {
        return start_date;
    }

	public String getAfternoon_count() {
		return afternoon_count;
	}

	public void setAfternoon_count(String afternoon_count) {
		this.afternoon_count = afternoon_count;
	}

	public String getEvening_count() {
		return evening_count;
	}

	public void setEvening_count(String evening_count) {
		this.evening_count = evening_count;
	}

	public String getNight_count() {
		return night_count;
	}

	public void setNight_count(String night_count) {
		this.night_count = night_count;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
