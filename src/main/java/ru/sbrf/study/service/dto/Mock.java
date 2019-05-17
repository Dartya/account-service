package ru.sbrf.study.service.dto;

import java.math.BigDecimal;

public class Mock {
    private static final int clientId = 1;
    private static final BigDecimal maxSumm = new BigDecimal(500);

    public Mock() {
    }

    public static int getClientId() {
        return clientId;
    }

    public static BigDecimal getMaxSumm() {
        return maxSumm;
    }
}
