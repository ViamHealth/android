package com.viamhealth.android.model;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskData {
    String id;
    String message;
    String labelChoice1;
    String labelChoice2;
    Integer setChoice = 0;
    String feedbackMessageChoice1;
    String feedbackMessageChoice2;

    public TaskData() {
    }

    public TaskData(String id, String message, String labelChoice1, String labelChoice2, Integer setChoice, String feedbackMessageChoice1, String feedbackMessageChoice2) {
        this.id = id;
        this.message = message;
        this.labelChoice1 = labelChoice1;
        this.labelChoice2 = labelChoice2;
        this.setChoice = setChoice;
        this.feedbackMessageChoice1 = feedbackMessageChoice1;
        this.feedbackMessageChoice2 = feedbackMessageChoice2;
    }

    public String getLabelChoice1() {
        return labelChoice1;
    }

    public void setLabelChoice1(String labelChoice1) {
        this.labelChoice1 = labelChoice1;
    }

    public String getLabelChoice2() {
        return labelChoice2;
    }

    public void setLabelChoice2(String labelChoice2) {
        this.labelChoice2 = labelChoice2;
    }

    public Integer getSetChoice() {
        return setChoice;
    }

    public void setSetChoice(Integer setChoice) {
        this.setChoice = setChoice;
    }

    public String getFeedbackMessageChoice1() {
        return feedbackMessageChoice1;
    }

    public void setFeedbackMessageChoice1(String feedbackMessageChoice1) {
        this.feedbackMessageChoice1 = feedbackMessageChoice1;
    }

    public String getFeedbackMessageChoice2() {
        return feedbackMessageChoice2;
    }

    public void setFeedbackMessageChoice2(String feedbackMessageChoice2) {
        this.feedbackMessageChoice2 = feedbackMessageChoice2;
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

}
