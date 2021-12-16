package evg.learn.tms.payment.api;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PaymentWorker {
    @ActivityMethod
    boolean pay(String userId);
}
