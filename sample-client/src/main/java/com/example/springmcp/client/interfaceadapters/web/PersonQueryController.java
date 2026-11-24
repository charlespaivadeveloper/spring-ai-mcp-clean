package com.example.springmcp.client.interfaceadapters.web;

import com.example.springmcp.client.usecases.AiQueryResponse;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/persons")
public class PersonQueryController {

    private final ChatClient chatClient;
    private final List<McpSyncClient> mcpSyncClients;

    public PersonQueryController(
            final ChatClient.Builder chatClientBuilder,
            final ToolCallbackProvider tools,
            final List<McpSyncClient> mcpSyncClients) {

        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(tools)
                .build();
        this.mcpSyncClients = mcpSyncClients;
    }

    @GetMapping("/nationality/{nationality}")
    public AiQueryResponse findByNationality(@PathVariable final String nationality) {
        log.info("Query: find persons by nationality={}", nationality);
        final PromptTemplate template = new PromptTemplate("Find persons with {nationality} nationality.");
        final Prompt prompt = template.create(Map.of("nationality", nationality));
        final String response = chatClient.prompt(prompt).call().content();
        return new AiQueryResponse("Find persons with nationality: " + nationality, response);
    }

    @GetMapping("/count-by-nationality/{nationality}")
    public AiQueryResponse countByNationality(@PathVariable final String nationality) {
        log.info("Query: count persons by nationality={}", nationality);
        final PromptTemplate template = new PromptTemplate("How many persons come from {nationality}?");
        final Prompt prompt = template.create(Map.of("nationality", nationality));
        final String response = chatClient.prompt(prompt).call().content();
        return new AiQueryResponse("Count persons by nationality: " + nationality, response);
    }

    @GetMapping("/count-by-nationality-from-server/{nationality}")
    public AiQueryResponse countByNationalityFromServer(@PathVariable final String nationality) {
        log.info("Query: count from server prompt for nationality={}", nationality);
        final Prompt prompt = loadMcpPrompt("persons-by-nationality", nationality);
        if (prompt == null) {
            return new AiQueryResponse("count-from-server", "MCP server prompt not available");
        }
        return new AiQueryResponse(
                "Count (server prompt) by nationality: " + nationality,
                chatClient.prompt(prompt).call().content()
        );
    }

    private Prompt loadMcpPrompt(final String promptName, final String nationality) {
        final McpSchema.GetPromptRequest request =
                new McpSchema.GetPromptRequest(promptName, Map.of("nationality", nationality));

        final Optional<McpSyncClient> client = mcpSyncClients.stream()
                .filter(c -> "person-mcp-server".equals(c.getServerInfo().name()))
                .findFirst();

        if (client.isPresent()) {
            final var content = (McpSchema.TextContent) client.get()
                    .getPrompt(request).messages().getFirst().content();
            final PromptTemplate template = new PromptTemplate(content.text());
            return template.create(Map.of("nationality", nationality));
        }
        return null;
    }
}
