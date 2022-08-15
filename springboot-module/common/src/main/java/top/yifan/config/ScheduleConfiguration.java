package top.yifan.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ScheduleConfiguration
 *
 * @author star
 */
@Configuration
@EnableScheduling
public class ScheduleConfiguration implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ScheduleConfiguration.class);

    private static final int DEFAULT_CORE_SIZE = 10;

    private final AtomicInteger scheduledNum = new AtomicInteger(0);

    private final AtomicInteger taskNum = new AtomicInteger(0);

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        log.debug("Create Scheduled Executor");
        final int schedleIndex = scheduledNum.getAndIncrement();
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(DEFAULT_CORE_SIZE,
                r -> new Thread(r, "Scheduled-Task[" + schedleIndex + "]-Task[" + taskNum.getAndIncrement() + "]"));
        registrar.setScheduler(pool);
    }
}
