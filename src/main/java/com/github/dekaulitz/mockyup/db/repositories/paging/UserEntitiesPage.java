package com.github.dekaulitz.mockyup.db.repositories.paging;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class UserEntitiesPage extends AbstractPage<UserEntities> implements Serializable {


}
