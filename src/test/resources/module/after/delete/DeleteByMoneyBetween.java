package template;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface TipMapper {
    int deleteByMoneyBetween(@Param("beginMoney") BigDecimal beginMoney, @Param("endMoney") BigDecimal endMoney);
}
