package com.github.dekaulitz.mockyup.server.model.common;

import com.github.dekaulitz.mockyup.server.model.constants.Language;

public abstract class MultiTranslations<T> {

  public abstract T filterTranslation(Language language);
}
