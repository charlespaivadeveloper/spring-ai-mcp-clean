package com.example.springmcp.person.interfaceadapters.mcp;

import com.example.springmcp.person.usecases.FindPersonByIdUseCase;
import com.example.springmcp.person.usecases.FindPersonsByNationalityUseCase;
import com.example.springmcp.person.usecases.PersonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonMcpAdapter {

    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final FindPersonsByNationalityUseCase findPersonsByNationalityUseCase;

    @Tool(description = "Find a person by their unique identifier. Returns person details including name, age, nationality and gender.")
    public PersonResponse getPersonById(
            @ToolParam(description = "The unique Long identifier of the person") final Long id) {

        log.info("MCP tool invoked: getPersonById with id={}", id);
        return findPersonByIdUseCase.findById(id);
    }

    @Tool(description = "Find all persons with a given nationality. Returns a list of person records.")
    public List<PersonResponse> getPersonsByNationality(
            @ToolParam(description = "The nationality to search for, e.g. 'Brazil', 'Germany', 'Japan'") final String nationality) {

        log.info("MCP tool invoked: getPersonsByNationality with nationality={}", nationality);
        return findPersonsByNationalityUseCase.findByNationality(nationality);
    }
}
