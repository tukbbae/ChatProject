package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class JsonResult extends BaseObject {

    private static final long serialVersionUID = -8014849606659869095L;
    
    /** The result code */
    private int resultCode;

    /** The result message */
    private String resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
