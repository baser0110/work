package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.InfoCodeGSM;
import bsshelper.externalapi.perfmng.entity.InfoCodeUMTS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoCodesGSMWrapper {
    private List<InfoCodeGSM> dataCodesGSM;
}
