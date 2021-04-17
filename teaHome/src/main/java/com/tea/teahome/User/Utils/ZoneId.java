package com.tea.teahome.User.Utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiang yuhang
 * @version 1.0
 * @className ZoneId
 * @program teaHome
 * @date 2021-04-17 18:08
 */
public class ZoneId {
    public static List<String> getAllZoneIdsAndItsOffSet() {
        Map<String, String> allZoneIdsAndItsOffSet = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();

        for (String zoneId : java.time.ZoneId.getAvailableZoneIds()) {
            java.time.ZoneId id = java.time.ZoneId.of(zoneId);

            // LocalDateTime -> ZonedDateTime
            ZonedDateTime zonedDateTime = localDateTime.atZone(id);

            // ZonedDateTime -> ZoneOffset
            ZoneOffset zoneOffset = zonedDateTime.getOffset();

            //replace Z to +00:00
            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");

            allZoneIdsAndItsOffSet.put(id.toString(), offset);
        }
        Map<String, String> sortedMap = new LinkedHashMap<>();

        // sort by value, descending order
        allZoneIdsAndItsOffSet.entrySet().stream()
                .sorted(Map.Entry.<String, String>comparingByValue().reversed())
                .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

        List<String> result = new ArrayList<>();

        sortedMap.forEach((k, v) ->
        {
            String out = String.format("%s (UTC%s) %n", k, v);
            result.add(out);
        });

        return result;
    }
}