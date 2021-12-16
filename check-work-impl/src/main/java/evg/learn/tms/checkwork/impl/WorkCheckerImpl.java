package evg.learn.tms.checkwork.impl;

import evg.learn.tms.checkwork.api.WorkChecker;
import evg.learn.tms.checkwork.api.WorkStatus;
import evg.learn.tms.shared.QueueNames;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class WorkCheckerImpl implements WorkChecker {
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public WorkStatus check(String workId) {
        log.info("Check work #{}", workId);
        return WorkStatus.values()[RANDOM.nextInt(WorkStatus.values().length)];
    }


    public static void main(String[] args) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget("127.0.0.1:7233")
                .build();

        WorkerFactory factory = WorkerFactory.newInstance(
                WorkflowClient.newInstance(
                        WorkflowServiceStubs.newInstance(options)));

        Worker worker = factory.newWorker(QueueNames.CHECK_WORK_Q);
        worker.registerActivitiesImplementations(new WorkCheckerImpl());

        factory.start();
    }
}
