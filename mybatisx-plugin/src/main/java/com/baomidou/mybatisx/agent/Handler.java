package com.baomidou.mybatisx.agent;

import org.mybatisx.extension.agent.api.AgentException;

public interface Handler {

  /**
   * 是否支持
   *
   * @return 是否支持，如果支持则调用execute方法
   */
  boolean supports(Object obj);

  /**
   * 执行
   *
   * @param obj 参数
   */
  void execute(Object obj) throws AgentException;
}
