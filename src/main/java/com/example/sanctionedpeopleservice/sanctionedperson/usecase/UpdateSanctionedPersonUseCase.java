package com.example.sanctionedpeopleservice.sanctionedperson.usecase;

import com.example.sanctionedpeopleservice.common.error.UseCaseError;
import com.example.sanctionedpeopleservice.common.error.UseCaseResult;
import com.example.sanctionedpeopleservice.common.error.response.ErrorDetail;
import com.example.sanctionedpeopleservice.sanctionedperson.model.SanctionedPerson;
import com.example.sanctionedpeopleservice.sanctionedperson.repository.SanctionedPersonRepository;
import com.example.sanctionedpeopleservice.sanctionedperson.resource.SanctionedPersonUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.sanctionedpeopleservice.common.error.model.ErrorStatus.PERSON_NOT_FOUND;

@Service
public class UpdateSanctionedPersonUseCase {
  private final SanctionedPersonRepository sanctionedPersonRepository;

  public UpdateSanctionedPersonUseCase(SanctionedPersonRepository sanctionedPersonRepository) {
    this.sanctionedPersonRepository = sanctionedPersonRepository;
  }

  public UseCaseResult<SanctionedPerson> updateSanctionedPerson(String personName,
                                                                SanctionedPersonUpdateRequest request) {
    return sanctionedPersonRepository.findByPersonName(personName)
        .map(sanctionedPerson -> updatePerson(sanctionedPerson, request.getNewPersonName()))
        .orElseGet(() -> new UseCaseResult<>(createPersonNonExistenceError(personName)));
  }

  private UseCaseResult<SanctionedPerson> updatePerson(SanctionedPerson sanctionedPerson, String newPersonName) {
    sanctionedPerson.setPersonName(newPersonName);
    sanctionedPersonRepository.save(sanctionedPerson);

    return new UseCaseResult<>(sanctionedPerson);
  }

  private UseCaseError createPersonNonExistenceError(String personName) {
    return new UseCaseError("Person does not exist", PERSON_NOT_FOUND,
        List.of(ErrorDetail.builder()
            .message("Person: "
                .concat(personName)
                .concat(" does not exist in sanctioned people list!"))
            .build()));
  }
}