package bsshelper.externalapi.configurationmng.nemoactserv.wrapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiberTableWrapper {
    private List<List<String>> dataOpticDev1;
    private List<List<String>> dataOpticDev2;
    private List<List<String>> dataOpticDev3;
    private List<List<String>> dataOpticDev4;
    private List<List<String>> dataOpticDev5;
    private List<List<String>> dataOpticDev6;
    private List<List<String>> dataOpticLink;
    private List<List<String>> dataOpticDevItbbu1_1;
    private List<List<String>> dataOpticDevItbbu1_2;
    private List<List<String>> dataOpticDevItbbu2_1;
    private List<List<String>> dataOpticDevItbbu2_2;
    private List<List<String>> dataOpticLinkItbbu;
    private List<Integer> maxSize;
    private List<Integer> maxSizeItbbu;
}
