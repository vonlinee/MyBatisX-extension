package com.baomidou.mybatisx.feat.jpa.operate.dialect;

import com.baomidou.mybatisx.feat.jpa.operate.manager.StatementBlock;

/**
 * The interface Custom statement.
 */
public interface CustomStatement {
  /**
   * Gets statement block.
   *
   * @return the statement block
   */
  StatementBlock getStatementBlock();

  /**
   * Operator name string.
   *
   * @return the string
   */
  String operatorName();
}
