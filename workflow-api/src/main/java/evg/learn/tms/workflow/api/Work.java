package evg.learn.tms.workflow.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    private String id;
    private String jsonData;
    private String userId;
}
