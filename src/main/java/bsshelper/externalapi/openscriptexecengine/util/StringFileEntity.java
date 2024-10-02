package bsshelper.externalapi.openscriptexecengine.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StringFileEntity {
    private final String file;
    private final String boundary;
}
