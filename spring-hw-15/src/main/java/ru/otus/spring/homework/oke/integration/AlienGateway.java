package ru.otus.spring.homework.oke.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.homework.oke.model.Egg;
import ru.otus.spring.homework.oke.model.Xenomorph;

@MessagingGateway(defaultReplyChannel = "aliensChannel")
public interface AlienGateway {
    @Gateway(requestChannel = "eggsChannel", replyChannel = "aliensChannel")
    Xenomorph processLifeCycle(Egg queenEggs);
}
