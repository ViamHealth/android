package com.viamhealth.android.model;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskData {
    String id;
    String message;
    String label_choice_1;
    String label_choice_2;

    public TaskData() {
    }

    public TaskData(String id, String message, String label_choice_1, String label_choice_2) {
        this.id = id;
        this.message = message;
        this.label_choice_1 = label_choice_1;
        this.label_choice_2 = label_choice_2;
    }



    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLabel_choice_1() {
        return label_choice_1;
    }

    public void setLabel_choice_1(String label_choice_1) {
        this.label_choice_1 = label_choice_1;
    }

    public String getLabel_choice_2() {
        return label_choice_2;
    }

    public void setLabel_choice_2(String label_choice_2) {
        this.label_choice_2 = label_choice_2;
    }
}
