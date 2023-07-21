package hsja.ibas.rabbit.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author gutao
 * @date 2023-07-13 14:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModel implements Serializable {

    private String name;

    private Long id;

    private Integer type;

    private List<Long> subIds;
}
