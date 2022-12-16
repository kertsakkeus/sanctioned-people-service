package com.example.sanctionedpeopleservice.sanctionedperson;

import com.example.sanctionedpeopleservice.common.error.UseCaseResult;
import com.example.sanctionedpeopleservice.common.error.exception.UseCaseErrorResponseException;
import com.example.sanctionedpeopleservice.sanctionedperson.resource.SanctionedPeopleResource;
import com.example.sanctionedpeopleservice.sanctionedperson.resource.SanctionedPersonRequest;
import com.example.sanctionedpeopleservice.sanctionedperson.resource.SanctionedPersonResource;
import com.example.sanctionedpeopleservice.sanctionedperson.resource.SanctionedPersonUpdateRequest;
import com.example.sanctionedpeopleservice.sanctionedperson.usecase.AddSanctionedPersonUseCase;
import com.example.sanctionedpeopleservice.sanctionedperson.usecase.DeleteSanctionedPersonUseCase;
import com.example.sanctionedpeopleservice.sanctionedperson.usecase.FindSanctionedPersonUseCase;
import com.example.sanctionedpeopleservice.sanctionedperson.usecase.UpdateSanctionedPersonUseCase;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class SanctionedPersonController {
  private final AddSanctionedPersonUseCase addSanctionedPersonUseCase;
  private final UpdateSanctionedPersonUseCase updateSanctionedPersonUseCase;
  private final DeleteSanctionedPersonUseCase deleteSanctionedPersonUseCase;
  private final FindSanctionedPersonUseCase findSanctionedPersonUseCase;
  private final SanctionedPersonResourceFactory sanctionedPersonResourceFactory;

  public SanctionedPersonController(AddSanctionedPersonUseCase addSanctionedPersonUseCase,
                                    UpdateSanctionedPersonUseCase updateSanctionedPersonUseCase,
                                    DeleteSanctionedPersonUseCase deleteSanctionedPersonUseCase,
                                    FindSanctionedPersonUseCase findSanctionedPersonUseCase,
                                    SanctionedPersonResourceFactory sanctionedPersonResourceFactory) {
    this.addSanctionedPersonUseCase = addSanctionedPersonUseCase;
    this.updateSanctionedPersonUseCase = updateSanctionedPersonUseCase;
    this.deleteSanctionedPersonUseCase = deleteSanctionedPersonUseCase;
    this.findSanctionedPersonUseCase = findSanctionedPersonUseCase;
    this.sanctionedPersonResourceFactory = sanctionedPersonResourceFactory;
  }

  @Operation(summary = "Get sanctioned person")
  @GetMapping(value = "/sanctions/{personName}")
  public ResponseEntity<SanctionedPersonResource> getSanctionedPerson(@PathVariable String personName) {
    return ResponseEntity.ok(sanctionedPersonResourceFactory.createSanctionedPersonResource(
        createResponse(findSanctionedPersonUseCase.getSanctionedPerson(personName))));
  }

  @Operation(summary = "Get sanctioned people")
  @GetMapping(value = "/sanctions")
  public ResponseEntity<SanctionedPeopleResource> getSanctionedPeople() {
    return ResponseEntity.ok(new SanctionedPeopleResource(
        createResponse(findSanctionedPersonUseCase.getSanctionedPeople())
            .stream()
            .map(sanctionedPersonResourceFactory::createSanctionedPersonResource)
            .toList()));
  }

  @Operation(summary = "Add sanctioned person")
  @PostMapping(value = "/sanctions")
  ResponseEntity<Object> addSanctionedPerson(@RequestBody SanctionedPersonRequest request) {
    return ResponseEntity.created(linkTo(methodOn(getClass())
        .getSanctionedPerson(createResponse(addSanctionedPersonUseCase.addSanctionedPerson(request))
            .getPersonName()))
        .toUri()).build();
  }

  @Operation(summary = "Update sanctioned person")
  @PutMapping(value = "/sanctions/{personName}")
  ResponseEntity<Object> updateSanctionedPerson(@PathVariable String personName,
                                                @RequestBody SanctionedPersonUpdateRequest request) {
    updateSanctionedPersonUseCase.updateSanctionedPerson(personName, request).getError().ifPresent(error -> {
      throw new UseCaseErrorResponseException(error);
    });

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete sanctioned person")
  @DeleteMapping(value = "/sanctions/{personName}")
  ResponseEntity<Object> deleteSanctionedPerson(@PathVariable String personName) {
    deleteSanctionedPersonUseCase.deleteSanctionedPerson(personName).getError().ifPresent(error -> {
      throw new UseCaseErrorResponseException(error);
    });

    return ResponseEntity.noContent().build();
  }

  private <T> T createResponse(UseCaseResult<T> useCaseResult) {
    useCaseResult.getError().ifPresent(error -> {
      throw new UseCaseErrorResponseException(error);
    });

    return useCaseResult.getResult();
  }
}