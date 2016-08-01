package com.hmlee.chatchat.model.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.hmlee.chatchat.core.base.BaseObject;

import java.util.List;

/**
 * 각종 코드 관리를 위한 Model 객체
 *
 * Created by hmlee
 */
@Entity
public class Code extends BaseObject {

    private static final long serialVersionUID = 8812135261389076781L;

    @Id
    private String code;
    private String name;
    private String parentCode;
    private String description;
    private int displayOrder;
    @Transient
    private int beforeOrder;
    @Transient
    private int afterOrder;
    @Transient
    private int subCodeCount;
    @Transient
    private List<Code> subCodeList;

    public Code() {
    }

    public Code(String code) {
        this.code = code;
    }

    public String getCode() {
        return code == null ? null : code.toUpperCase();
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getBeforeOrder() {
        return beforeOrder;
    }

    public void setBeforeOrder(int beforeOrder) {
        this.beforeOrder = beforeOrder;
    }

    public int getAfterOrder() {
        return afterOrder;
    }

    public void setAfterOrder(int afterOrder) {
        this.afterOrder = afterOrder;
    }

    public int getSubCodeCount() {
        return subCodeCount;
    }

    public void setSubCodeCount(int subCodeCount) {
        this.subCodeCount = subCodeCount;
    }

    public List<Code> getSubCodeList() {
        return subCodeList;
    }

    public void setSubCodeList(List<Code> subCodeList) {
        this.subCodeList = subCodeList;
    }

}
