package com.laola.apa.server;

import java.util.List;

/**
 *
 * @param <R>
 * @param <P>
 */
public interface PortDataDealService<R,P> {
      R deal(P ... data);
}
