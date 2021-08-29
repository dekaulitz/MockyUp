package com.github.dekaulitz.mockyup.server.db.repositories.paging;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MockEntitiesPage extends AbstractPage<MockEntities> implements Serializable {


}
