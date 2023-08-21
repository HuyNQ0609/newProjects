package com.vsafe.admin.server.helpers.enums;

/**
 * Intent state interface.
 */

public interface IntentStateWithDescription extends CommonEnum<String, String>{
    // Enum translation.
    String description();

    default String state() {
        return null;
    }

    @Override
    default String getName() { return getValue();};
}
