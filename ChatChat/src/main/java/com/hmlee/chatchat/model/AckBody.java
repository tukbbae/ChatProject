package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class AckBody extends BaseObject {

    private static final long serialVersionUID = 5606519655593002488L;

    private String msg_id;
    private String regi_id;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getRegi_id() {
        return regi_id;
    }

    public void setRegi_id(String regi_id) {
        this.regi_id = regi_id;
    }
}
