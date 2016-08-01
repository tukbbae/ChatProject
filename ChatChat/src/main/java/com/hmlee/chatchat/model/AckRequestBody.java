package com.hmlee.chatchat.model;

import java.util.List;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class AckRequestBody extends BaseObject {
    private static final long serialVersionUID = -2140484619612837482L;

    private List<AckBody> ackList;

    public List<AckBody> getAckList() {
        return ackList;
    }

    public void setAckList(List<AckBody> ackList) {
        this.ackList = ackList;
    }
}
