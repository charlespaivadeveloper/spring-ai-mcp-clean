package com.example.springmcp.person.frameworks;

import com.example.springmcp.person.interfaceadapters.mcp.PersonMcpAdapter;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.example.springmcp.person")
public class PersonMcpServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PersonMcpServiceApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider personTools(final PersonMcpAdapter personMcpAdapter) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(personMcpAdapter)
                .build();
    }

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> personPrompts() {
        final var prompt = new McpSchema.Prompt(
                "persons-by-nationality",
                "Get persons by nationality",
                List.of(new McpSchema.PromptArgument("nationality", "Person nationality", true))
        );

        final var spec = new McpServerFeatures.SyncPromptSpecification(
                prompt,
                (exchange, request) -> {
                    final String arg = (String) request.arguments().get("nationality");
                    final var message = new McpSchema.PromptMessage(
                            McpSchema.Role.USER,
                            new McpSchema.TextContent("How many persons come from " + arg + " ?")
                    );
                    return new McpSchema.GetPromptResult("Count persons by nationality", List.of(message));
                }
        );

        return List.of(spec);
    }
}
