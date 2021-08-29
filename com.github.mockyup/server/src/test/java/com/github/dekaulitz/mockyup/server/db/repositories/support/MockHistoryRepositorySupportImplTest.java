package com.github.dekaulitz.mockyup.server.db.repositories.support;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v1.MockHistoryEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.server.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.server.domain.mocks.vmodels.DtoMockupHistoryVmodel;
import com.github.dekaulitz.mockyup.server.helperTest.Helper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class MockHistoryRepositorySupportImplTest {

  @Autowired
  MockHistoryRepository mockHistoryRepository;

  @Autowired
  MockRepository mockRepository;

  @Test
  void getMockHistoryByIdAndMockId() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    mockEntities.setId(null);
    mockRepository.save(mockEntities);

    MockHistoryEntities mockHistoryEntities = Helper.getMockHistoryEntities();
    mockHistoryEntities.setId(null);
    mockHistoryEntities.setMockId(mockEntities.getId());

    mockHistoryRepository.save(mockHistoryEntities);
    DtoMockupHistoryVmodel dtoMockupHistoryVmodel = mockHistoryRepository
        .getMockHistoryByIdAndMockId(mockHistoryEntities.getMockId(),
            mockHistoryEntities.getId());
    Assert.isTrue(dtoMockupHistoryVmodel.getId().equals(mockHistoryEntities.getId()),
        "history id is not expected");
    Assert.isTrue(dtoMockupHistoryVmodel.getMockId().equals(mockEntities.getId()),
        "mock id is not expected");
  }
}
