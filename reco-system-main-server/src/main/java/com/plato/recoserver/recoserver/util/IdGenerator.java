package com.plato.recoserver.recoserver.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @author Kevin
 * @date 2022-03-29
 */
public class IdGenerator {
    /**
     * generate the unique recommend event id based on uuid and date
     * @return the unique id
     */
    public static String generateEventId() {
        String today = DateFormatUtils.format(new Date(), "yyMMdd");
        String uuid = UUID.randomUUID().toString();
        return today + StringUtils.join(StringUtils.split(uuid, '-'));
    }
}
