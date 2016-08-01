package com.hmlee.chatchat.model;

import java.util.List;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class PushRequestBody extends BaseObject {
    private static final long serialVersionUID = -6498046228449666880L;

    private String type;

    private List<MessageBody> pushList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MessageBody> getPushList() {
        return pushList;
    }

    public void setPushList(List<MessageBody> pushList) {
        this.pushList = pushList;
    }
}
