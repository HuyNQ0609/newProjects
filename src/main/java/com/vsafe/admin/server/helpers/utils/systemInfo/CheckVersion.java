package com.vsafe.admin.server.helpers.utils.systemInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Slf4j
public class CheckVersion {
    public static void getVersionInfo() {
        String str = "\n==========================================\n" +
                OSinfo.getOSname()  + "\n" +
                "==========================================" + "\n";
        log.debug(str);
    }
}
