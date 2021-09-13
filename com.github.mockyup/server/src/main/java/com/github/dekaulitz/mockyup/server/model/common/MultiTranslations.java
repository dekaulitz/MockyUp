package com.github.dekaulitz.mockyup.server.model.common;

import com.github.dekaulitz.mockyup.server.model.constants.Language;

public abstract class MultiTranslations<T> extends BaseModel {

  public abstract T filterTranslation(Language language);
}
