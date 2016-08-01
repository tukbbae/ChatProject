package com.hmlee.chatchat.core.constant;

/**
 * Created by hmlee
 */
public final class Constants {
    private Constants() {
    }

    /**
     * 객체를 세션에 바인딩하거나 없을 때 사용할 키
     */
    public static final String SESSION_USER_ATTRIBUTE = "sessionUser";

    public final class RedirectUrl {
        /** 세션이 만료되거나 유효하지 않을 시 Redirect URL */
        public static final String INVALID_SESSION = "/invalidSession";

        /** 접근 거부 Redirect URL */
        public static final String ACCESS_DENIED = "/accessDenied";

        /** 로그인 성공 시 Redirect URL */
        public static final String LOGIN_SUCCESS = "/ctrl_chat_home";

        /** 로그인 실패 시 Redirect URL */
        public static final String LOGIN_FAILURE = "/login";

        /** 로그아웃 성공 시 Redirect URL */
        public static final String LOGOUT_SUCCESS = "/login";
    }

    public static class DeviceType {
        public static final String IOS = "i";
        public static final String ANDROID = "a";
        public static final String EMAIL = "e";
    }

    public static class ResponseCode {
        public static final int SUCCESS = 200;
        public static final int FAILED = 417;
        public static final int BAD_REQUEST = 400;
        public static final int NOT_FOUND = 404;
        public static final int SERVER_ERROR = 500;
    }

    public static class ApnsMessageType {
        public static final int AUTHENTICATION = 1000;
        public static final int GENERAL_MESSAGE = 1001;
    }

    public static class MessageType {
        public static final String PUSH_MESSAGE = "P";
        public static final String MAIL = "M";
    }

    public static class AddressFieldNames {
        public static final String NAME        = "name";
        public static final String COMPANY     = "company";
        public static final String POSITION    = "position";
        public static final String DEPARTMENT  = "department";
        public static final String PHONE       = "phoneNumber";
    }

    public static class MessageClass {
        public static final String NORMAL = "message";
    }

    public final class ModelMapAttribute {
        public static final String PAGINATION = "pagination";
    }
}
