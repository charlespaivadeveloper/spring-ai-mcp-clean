package com.example.springmcp.person.usecases;

import java.util.List;

public interface FindPersonsByNationalityUseCase {

    List<PersonResponse> findByNationality(String nationality);
}
