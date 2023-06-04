package template;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface TipMapper {
    int deleteByMoneyBeforeAndMoneyAfter(@Param("money") BigDecimal money, @Param("oldMoney") BigDecimal oldMoney);
}
