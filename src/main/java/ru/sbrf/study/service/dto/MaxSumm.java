package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO параметра максимальной суммы
 */
public class MaxSumm {
    private BigDecimal maxSumm;

    public MaxSumm() {
    }

    public MaxSumm(BigDecimal maxSumm) {
        this.maxSumm = maxSumm;
    }

    public BigDecimal getMaxSumm() {
        return maxSumm;
    }

    public void setMaxSumm(BigDecimal maxSumm) {
        this.maxSumm = maxSumm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxSumm maxSumm1 = (MaxSumm) o;
        return Objects.equals(maxSumm, maxSumm1.maxSumm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxSumm);
    }
}
