package com.vsafe.admin.server.helpers.enums;

public class RecurringSubscriptionEnum {
    public enum BillCycle implements IntentStateWithDescription{
        MONTHLY("Hàng tháng"),
        MONTH_3("3 tháng"),
        MONTH_6("6 tháng"),
        MONTH_9("9 tháng"),
        YEARLY("Hàng năm"),
        ONE_TIME("Thanh toán 1 lần"),
        ;

        private final String description;

        BillCycle(String description) {
            this.description = description;
        }

        @Override
        public String description() {
            return this.description;
        }

        @Override
        public String getValue() {
            return name();
        }

        public static BillCycle parse(String value) {
            BillCycle[] validFlags = BillCycle.values();
            for (BillCycle validFlag : validFlags) {
                if (validFlag.getValue().equals(value)) {
                    return validFlag;
                }
            }
            return null;
        }
    }
}
