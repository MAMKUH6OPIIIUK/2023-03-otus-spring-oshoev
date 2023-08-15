package ru.otus.spring.homework.oke.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.homework.oke.model.Chestbuster;
import ru.otus.spring.homework.oke.model.Human;
import ru.otus.spring.homework.oke.model.Xenomorph;
import ru.otus.spring.homework.oke.service.HumanService;

@SuppressWarnings("unused")
@Configuration
public class IntegrationConfig {
    @Bean
    public MessageChannelSpec<?, ?> eggsChannel() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> aliensChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow alienLifeCycleFlow(HumanService humanService) {
        return IntegrationFlow.from(eggsChannel())
                .handle(humanService, "examineEgg")
                .<Human, Boolean>route(
                        human -> human.getFacehugger() == null,
                        mapping -> mapping
                                .subFlowMapping(true, longHumanLifeFlow(humanService))
                                .subFlowMapping(false, sf -> sf
                                        .transform(h -> new Chestbuster())
                                        .transform(c -> new Xenomorph())
                                        .channel(aliensChannel())))
                .get();
    }

    @Bean
    public IntegrationFlow longHumanLifeFlow(HumanService humanService) {
        return f -> f
                .handle(humanService, "longLife");
    }
}
