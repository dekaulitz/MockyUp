package com.github.dekaulitz.mockyup.repositories.paging;

import com.github.dekaulitz.mockyup.entities.MockEntities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
public class MockEntitiesPage extends AbstractPage<MockEntities> implements Serializable {


}
