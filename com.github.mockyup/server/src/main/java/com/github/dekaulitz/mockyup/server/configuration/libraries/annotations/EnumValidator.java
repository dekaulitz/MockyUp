package com.github.dekaulitz.mockyup.server.configuration.libraries.annotations;

import com.github.dekaulitz.mockyup.server.model.constants.Role;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;

public class EnumValidator implements ConstraintValidator<ValidEnum, List<String>> {

  @Override
  public boolean isValid(List<String> list,
      ConstraintValidatorContext constraintValidatorContext) {

    if (CollectionUtils.isNotEmpty(list)) {
      for (String s : list) {
        if (!EnumUtils.isValidEnum(Role.class, s)){
          return false;
        }
      }
    }

    return true;
  }
}
