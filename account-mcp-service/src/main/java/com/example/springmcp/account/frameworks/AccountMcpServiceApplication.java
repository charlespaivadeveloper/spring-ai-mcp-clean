package com.example.springmcp.account.frameworks;

import com.example.springmcp.account.interfaceadapters.mcp.AccountMcpAdapter;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.springmcp.account")
public class AccountMcpServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AccountMcpServiceApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider accountTools(final AccountMcpAdapter accountMcpAdapter) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(accountMcpAdapter)
                .build();
    }
}
