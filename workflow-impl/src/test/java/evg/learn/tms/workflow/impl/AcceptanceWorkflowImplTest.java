package evg.learn.tms.workflow.impl;

import evg.learn.tms.checkwork.impl.WorkCheckerImpl;
import evg.learn.tms.payment.impl.PaymentWorkerImpl;
import evg.learn.tms.shared.QueueNames;
import evg.learn.tms.workflow.api.AcceptanceWorkflow;
import evg.learn.tms.workflow.api.Work;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcceptanceWorkflowImplTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private Worker checkWorker;
    private Worker paymentWorker;
    private WorkflowClient workflowClient;

    @Before
    public void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();

        checkWorker = testEnv.newWorker(QueueNames.CHECK_WORK_Q);
        checkWorker.registerActivitiesImplementations(new WorkCheckerImpl());

        paymentWorker = testEnv.newWorker(QueueNames.PAYMENT_Q);
        paymentWorker.registerActivitiesImplementations(new PaymentWorkerImpl());

        worker = testEnv.newWorker(QueueNames.WORKFLOW_Q);
        worker.registerWorkflowImplementationTypes(AcceptanceWorkflowImpl.class);

        workflowClient = testEnv.getWorkflowClient();

        testEnv.start();
    }

    @After
    public void tearDown() {
        testEnv.close();
    }

    @Test
    public void testTransfer() {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(QueueNames.WORKFLOW_Q)
                .build();
        AcceptanceWorkflow workflow = workflowClient.newWorkflowStub(AcceptanceWorkflow.class, options);
        workflow.acceptance(new Work("w_1", "{data:'fuck you'}", "u_1"));
    }
}