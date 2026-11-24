package com.example.springmcp.account.interfaceadapters.mcp;

import com.example.springmcp.account.usecases.AccountResponse;
import com.example.springmcp.account.usecases.FindAccountsByPersonIdUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountMcpAdapter {

    private final FindAccountsByPersonIdUseCase findAccountsByPersonIdUseCase;

    @Tool(description = "Find all bank accounts owned by a person. Returns account number, balance and owner information.")
    public List<AccountResponse> getAccountsByPersonId(
            @ToolParam(description = "The unique Long identifier of the person") final Long personId) {

        log.info("MCP tool invoked: getAccountsByPersonId with personId={}", personId);
        return findAccountsByPersonIdUseCase.findByPersonId(personId);
    }
}
