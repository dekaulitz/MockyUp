package com.github.dekaulitz.mockyup.db.repositories.paging;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MockEntitiesPage extends AbstractPage<MockEntities> implements Serializable {


}
