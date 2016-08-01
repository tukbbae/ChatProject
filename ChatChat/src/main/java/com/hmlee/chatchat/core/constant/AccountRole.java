package com.hmlee.chatchat.core.constant;

/**
 * 사용자 계정의 권한을 정의하는 열거형 상수
 *
 * Created by hmlee
 */
public enum AccountRole {

    ROLE_ADMIN("SYSADMIN"),
    ROLE_OPERATOR("OPERATOR"),
    ROLE_AGENT("AGENT");

    private final String roleCode;

    AccountRole(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public static AccountRole getInstance(String roleCode) {
        for (AccountRole accountRole : AccountRole.values()) {
            if (accountRole.getRoleCode().equals(roleCode)) {
                return accountRole;
            }
        }

        throw new IllegalArgumentException("No such AccountRole!!");
    }

}
