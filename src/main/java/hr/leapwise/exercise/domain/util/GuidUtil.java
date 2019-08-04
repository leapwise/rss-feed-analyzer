package hr.leapwise.exercise.domain.util;


import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

public class GuidUtil {

    public static String getStringGuid(String ... elements) {

        UUID uuid = null;
        if (elements  != null) {
            final String guidBase = StringUtils.join(elements);

            if (StringUtils.isNotBlank(guidBase)) {
                try {
                    uuid = UUID.nameUUIDFromBytes(guidBase.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    uuid = UUID.randomUUID();
                }
            }
        }
        return Optional.ofNullable(uuid).map(UUID::toString).orElse(UUID.randomUUID().toString());
    }
}
