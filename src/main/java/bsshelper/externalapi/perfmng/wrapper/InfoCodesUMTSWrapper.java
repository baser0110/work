package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.InfoCodeUMTS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoCodesUMTSWrapper {
    private List<InfoCodeUMTS> dataCodesUMTS;
}
