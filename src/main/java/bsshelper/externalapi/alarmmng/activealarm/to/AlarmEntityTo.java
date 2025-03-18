package bsshelper.externalapi.alarmmng.activealarm.to;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEntityTo {
    List<AlarmEntity> data;
}
