package evg.learn.tms.workflow.api;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AcceptanceWorkflow {
    @WorkflowMethod
    void acceptance(Work work);
}
