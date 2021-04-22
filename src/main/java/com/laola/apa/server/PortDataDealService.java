package com.laola.apa.server;

import java.util.List;

/**
 *
 * @param <R> deal 返回值类型
 * @param <P> deal 参数类型
 */
public interface PortDataDealService<R,P> {
      R deal(P ... data);
}
