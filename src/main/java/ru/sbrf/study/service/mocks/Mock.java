package ru.sbrf.study.service.mocks;

import java.math.BigDecimal;

public class Mock {
    private static final int clientId = 1;
    private static final boolean tokenValid = true;
    private static final BigDecimal maxSumm = new BigDecimal(500);

    public Mock() {
    }

    public static boolean isTokenValid() {
        return tokenValid;
    }

    public static int getClientId() {
        return clientId;
    }

    public static BigDecimal getMaxSumm() {
        return maxSumm;
    }
}
