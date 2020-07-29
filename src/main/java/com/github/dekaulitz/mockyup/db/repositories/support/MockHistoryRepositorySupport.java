package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;

public interface MockHistoryRepositorySupport {
    /**
     * get all mock history
     *
     * @param mockId    id from mock colelction
     * @param historyId id from history collection
     * @return DtoMockupHistoryVmodel
     */
    DtoMockupHistoryVmodel getMockHistoryByIdAndMockId(String mockId, String historyId);
}
