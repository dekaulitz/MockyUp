package com.github.dekaulitz.mockyup.server.db.tmp.repositories.paging;

import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserEntities;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class UserEntitiesPage extends AbstractPage<UserEntities> implements Serializable {


}
