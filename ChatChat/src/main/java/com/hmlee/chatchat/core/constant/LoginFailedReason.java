package com.hmlee.chatchat.core.constant;

/**
 * LoginFailedReason
 *
 * 로그인 실패 사유 열거형 상수
 *
 * Created by hmlee
 */
public enum LoginFailedReason {

    NOT_EXISTED_USER("USR0102001", "app.login.failed.reason.idOrPassword"), // 존재하지 않는 계정
    ALREADY_LOGIN_USER("USR0102002", "app.login.failed.reason.alreadyLoginUser"), // 이미 로그인된 계정
    NOT_APPROVED_USER("USR0102003", "app.login.failed.reason.notApprovedUser"), // 미승인 계정
    EXCEEDED_MAXIMUM_LOGIN_ATTEMPTS("USR0102004", "app.login.failed.reason.exceededMaximumLoginAttempts"), // 로그인 실패 횟수 초과
    RESTRICTED_USER("USR0102005", "app.login.failed.reason.restrictedUser"), // 차단 계정
    EXCEEDED_MAXIMUM_INACTIVE_DAYS("USR0102006", "app.login.failed.reason.exceededMaximumInactiveDays"), // 장기 미사용 계정
    EXCEEDED_MAXIMUM_PASSWORD_AGE("USR0102007", "app.login.failed.reason.exceededMaximumPasswordAge"), // 패스워드 장기 미변경 계정
    EXCEPTION("USR0102998", "app.login.failed.reason.exception"), // 로그인 중 예외발생
    UNKNOWN_REASON("USR0102999", "app.login.failed.reason.unknownReason"); // 알 수 없는 이유

    /** 사유 코드 */
    private final String reasonCode;

    /** Springframework message 코드 */
    private final String messageCode;

    LoginFailedReason(String reasonCode, String messageCode) {
        this.reasonCode = reasonCode;
        this.messageCode = messageCode;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @return the messageCode
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 사유 코드에 해당하는 LoginFailedReason Instance 반환
     *
     * @param reasonCode
     * @return
     */
    public static LoginFailedReason getInstance(String reasonCode) {
        for (LoginFailedReason failedReason : LoginFailedReason.values()) {
            if (failedReason.getReasonCode().equals(reasonCode)) {
                return failedReason;
            }
        }

        throw new IllegalArgumentException("No such LoginFailedReason!!!");
    }

}
