package com.github.dekaulitz.mockyup.server.db.repositories.support;

import com.github.dekaulitz.mockyup.server.domain.mocks.vmodels.DtoMockupHistoryVmodel;

public interface MockHistoryRepositorySupport {

  /**
   * get all mock history
   *
   * @param mockId    {@link String} id from mock colelction
   * @param historyId {@link String} id from history collection
   * @return DtoMockupHistoryVmodel
   */
  DtoMockupHistoryVmodel getMockHistoryByIdAndMockId(String mockId, String historyId);
}
