package com.viamhealth.android.model.cards;

import com.viamhealth.android.model.enums.PriorityCardType;

/**
 * Created by kunal on 17/1/14.
 */
public class PickMedicalTestPriorityCard extends PriorityCard {

    private String test1Message;
    private String test1Id;
    private String test1Check;
    private String test2Message;
    private String test2Id;
    private String test2Check;
    private String test3Message;
    private String test3Id;
    private String test3Check;

    public String getTest1Message() {
        return test1Message;
    }

    public void setTest1Message(String test1Message) {
        this.test1Message = test1Message;
    }

    public String getTest1Id() {
        return test1Id;
    }

    public void setTest1Id(String test1Id) {
        this.test1Id = test1Id;
    }

    public String getTest1Check() {
        return test1Check;
    }

    public void setTest1Check(String test1Check) {
        this.test1Check = test1Check;
    }

    public String getTest2Message() {
        return test2Message;
    }

    public void setTest2Message(String test2Message) {
        this.test2Message = test2Message;
    }

    public String getTest2Id() {
        return test2Id;
    }

    public void setTest2Id(String test2Id) {
        this.test2Id = test2Id;
    }

    public String getTest2Check() {
        return test2Check;
    }

    public void setTest2Check(String test2Check) {
        this.test2Check = test2Check;
    }

    public String getTest3Message() {
        return test3Message;
    }

    public void setTest3Message(String test3Message) {
        this.test3Message = test3Message;
    }

    public String getTest3Id() {
        return test3Id;
    }

    public void setTest3Id(String test3Id) {
        this.test3Id = test3Id;
    }

    public String getTest3Check() {
        return test3Check;
    }

    public void setTest3Check(String test3Check) {
        this.test3Check = test3Check;
    }



    @Override
    public void setCustomData(String type, String value){
        if (type == "test1Message") {
            setTest1Message(value);
        } else if (type == "test1Check") {
            setTest1Check(value);
        } else {
            throw new IllegalArgumentException("incorrect custom data type");
        }
    }

    @Override
    public String getCustomData(String v) {
        return v;
    }

    @Override
    public PriorityCardType getType() {
        return null;
    }
}
