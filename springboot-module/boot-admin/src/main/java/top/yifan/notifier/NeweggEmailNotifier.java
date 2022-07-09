package top.yifan.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;
import top.yifan.util.DateHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * NeweggEmailNotifier
 */
public class NeweggEmailNotifier extends AbstractEventNotifier {

    private final String clientUrl;

    public NeweggEmailNotifier(InstanceRepository repository, String clientUrl) {
        super(repository);
        this.clientUrl = clientUrl;
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> doEvent(event, instance));
    }

    protected void doEvent(InstanceEvent event, Instance instance) {
        if (event instanceof InstanceStatusChangedEvent) {
            StatusInfo status = instance.getStatusInfo();
            // down表示服务里部分中间件有问题
            // offline表示服务无法http访问了
            if (!status.isUp() && !status.isDown() && !status.isOffline()) {
                return;
            }
            String appName = instance.getRegistration().getName();
            String appStatus = status.getStatus();
            String endpoint = instance.getRegistration().getServiceUrl();
            if (StringUtils.isBlank(endpoint)) {
                endpoint = instance.getRegistration().getHealthUrl();
            }

            Map<String, Object> tmpAttrs = new HashMap<>();
            tmpAttrs.put("appName", appName);
            tmpAttrs.put("appStatus", appStatus);
            tmpAttrs.put("eventTime", DateHelper.format(event.getTimestamp()));
            tmpAttrs.put("endpoint", endpoint);
            tmpAttrs.put("clientUrl", clientUrl);

            String tpl = "app-status-down";
            if (status.isUp()) {
                tpl = "app-status-up";
            }
            // TODO 获取邮件模板，发送通知
        }
    }

}
