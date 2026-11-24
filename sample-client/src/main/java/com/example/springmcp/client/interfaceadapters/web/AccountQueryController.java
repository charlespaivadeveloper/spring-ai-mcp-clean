package com.example.springmcp.client.interfaceadapters.web;

import com.example.springmcp.client.usecases.AiQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountQueryController {

    private final ChatClient chatClient;

    public AccountQueryController(
            final ChatClient.Builder chatClientBuilder,
            final ToolCallbackProvider tools) {

        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(tools)
                .build();
    }

    @GetMapping("/count-by-person/{personId}")
    public AiQueryResponse countByPerson(@PathVariable final Long personId) {
        log.info("Query: count accounts for personId={}", personId);
        final PromptTemplate template = new PromptTemplate(
                "How many accounts does the person with id {personId} have?");
        final Prompt prompt = template.create(Map.of("personId", personId));
        final String response = chatClient.prompt(prompt).call().content();
        return new AiQueryResponse("Count accounts for personId: " + personId, response);
    }

    @GetMapping("/balance-by-person/{personId}")
    public AiQueryResponse totalBalanceByPerson(@PathVariable final Long personId) {
        log.info("Query: total balance for personId={}", personId);
        final PromptTemplate template = new PromptTemplate("""
                What is the total balance across all accounts for the person with id {personId}?
                Also return the account numbers and individual balances.
                """);
        final Prompt prompt = template.create(Map.of("personId", personId));
        final String response = chatClient.prompt(prompt).call().content();
        return new AiQueryResponse("Total balance for personId: " + personId, response);
    }
}
