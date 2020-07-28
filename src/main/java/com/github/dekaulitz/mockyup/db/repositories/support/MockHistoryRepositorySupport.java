package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;

public interface MockHistoryRepositorySupport {
    DtoMockupHistoryVmodel getMockHistoryByIdAndMockId(String mockId, String historyId);
}
