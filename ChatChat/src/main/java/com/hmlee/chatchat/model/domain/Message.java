package com.hmlee.chatchat.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
@Entity
public class Message extends BaseObject {

    private static final long serialVersionUID = -7014476330988523731L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String content;
    // TODO : add message type, read,
    // FIXME : 인증되지 않은 사용자에게는 메시지 발송을 하지 않도록 수정

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
