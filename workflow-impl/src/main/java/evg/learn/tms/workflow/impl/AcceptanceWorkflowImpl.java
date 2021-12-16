package evg.learn.tms.workflow.impl;

import evg.learn.tms.checkwork.api.WorkChecker;
import evg.learn.tms.checkwork.api.WorkStatus;
import evg.learn.tms.payment.api.PaymentWorker;
import evg.learn.tms.shared.QueueNames;
import evg.learn.tms.workflow.api.AcceptanceWorkflow;
import evg.learn.tms.workflow.api.Work;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class AcceptanceWorkflowImpl implements AcceptanceWorkflow {

    // ActivityStubs enable calls to Activities as if they are local methods, but actually perform an RPC.
    private final WorkChecker workChecker = Workflow.newActivityStub(WorkChecker.class, ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .setTaskQueue(QueueNames.CHECK_WORK_Q)
            .build());

    private final PaymentWorker paymentWorker = Workflow.newActivityStub(PaymentWorker.class, ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .setTaskQueue(QueueNames.PAYMENT_Q)
            .build());


    @Override
    public void acceptance(Work work) {
        log.info("Start acceptance: {}", work);
        WorkStatus workStatus = workChecker.check(work.getId());
        if (WorkStatus.OK == workStatus) {
            log.info("Work OK, start payment");
            paymentWorker.pay(work.getUserId());
        } else {
            log.info("Work not OK");
        }
    }

    public static void main(String[] args) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget("127.0.0.1:7233")
                .build();

        WorkerFactory factory = WorkerFactory.newInstance(
                WorkflowClient.newInstance(
                        WorkflowServiceStubs.newInstance(options)));

        Worker worker = factory.newWorker(QueueNames.WORKFLOW_Q);
        worker.registerWorkflowImplementationTypes(AcceptanceWorkflowImpl.class);

        factory.start();
    }
}
