package evg.learn.tms.payment.impl;

import evg.learn.tms.payment.api.PaymentWorker;
import evg.learn.tms.shared.QueueNames;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class PaymentWorkerImpl implements PaymentWorker {
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public boolean pay(String userId) {
        log.info("Payment for user #{}", userId);
        return RANDOM.nextBoolean();
    }

    public static void main(String[] args) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget("127.0.0.1:7233")
                .build();

        WorkerFactory factory = WorkerFactory.newInstance(
                WorkflowClient.newInstance(
                        WorkflowServiceStubs.newInstance(options)));

        Worker worker = factory.newWorker(QueueNames.PAYMENT_Q);
        worker.registerActivitiesImplementations(new PaymentWorkerImpl());

        factory.start();
    }
}
