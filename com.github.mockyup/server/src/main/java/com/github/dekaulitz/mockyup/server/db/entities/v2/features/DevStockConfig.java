package com.github.dekaulitz.mockyup.server.db.entities.v2.features;

import com.github.dekaulitz.mockyup.server.db.entities.v2.features.mockup.MockUpRequestEmbedded;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DevStockConfig implements Serializable {

  private MockUpRequestEmbedded mockup;
}
