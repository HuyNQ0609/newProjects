package com.vsafe.admin.server.helpers.enums.responseStatus;

public class ErrorCode {

    public enum AuthErrorCode implements IResponseStatus {
        AUTH0001("User not exists."),
        AUTH0002("Wrong password."),
        AUTH0003("Disabled account."),
        AUTH0004("Expired account."),
        AUTH0005("Locked account."),
        AUTH0006("Expired credentials."),
        AUTH0007("Illegal token type."),
        AUTH0201("Illegal token type."),
        AUTH9999("Illegal token type.");
        private final String message;

        AuthErrorCode(String message) {
            this.message = message;
        }

        @Override
        public String message() {
            return message;
        }

        @Override
        public String messageCode() {
            return this.name();
        }
    }

    public enum BusinessErrorCode implements IResponseStatus {
        BUS404("Dữ liệu không tồn tại"),
        BUS400("Dữ liệu không hợp lệ");
        private final String message;

        BusinessErrorCode(String message) {
            this.message = message;
        }

        @Override
        public String message() {
            return message;
        }

        @Override
        public String messageCode() {
            return this.name();
        }
    }
}
