package com.github.dekaulitz.mockyup.repositories.paging;

import com.github.dekaulitz.mockyup.entities.UserEntities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
public class UserEntitiesPage extends AbstractPage<UserEntities> implements Serializable {


}
