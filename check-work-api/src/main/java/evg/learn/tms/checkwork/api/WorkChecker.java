package evg.learn.tms.checkwork.api;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface WorkChecker {
    @ActivityMethod
    WorkStatus check(String workId);
}
