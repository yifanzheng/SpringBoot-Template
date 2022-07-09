package top.yifan.config;


import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yifan.notifier.NeweggEmailNotifier;

/**
 * NotifierConfiguration
 */
@Configuration
public class NotifierConfiguration {

    @Bean
    public NeweggEmailNotifier neweggEmailNotifier(
            InstanceRepository repository,
            @Value("${spring.boot.admin.client.url: }") String clientUrl) {
        return new NeweggEmailNotifier(repository, clientUrl);
    }

}
