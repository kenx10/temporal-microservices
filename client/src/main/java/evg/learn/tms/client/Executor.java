package evg.learn.tms.client;

import evg.learn.tms.shared.QueueNames;
import evg.learn.tms.workflow.api.AcceptanceWorkflow;
import evg.learn.tms.workflow.api.Work;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

public class Executor {
    public static void main(String[] args) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget("127.0.0.1:7233")
                .build();

        // This gRPC stubs wrapper talks to the local docker instance of the Temporal service.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(options);
        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(QueueNames.WORKFLOW_Q)
                .build();


        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        AcceptanceWorkflow acceptanceWorkflow = client.newWorkflowStub(AcceptanceWorkflow.class, workflowOptions);

        acceptanceWorkflow.acceptance(new Work("w123", "{data:'2+2=5'}", "u321"));


        System.exit(0);
    }
}
