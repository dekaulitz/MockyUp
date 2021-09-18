package com.github.dekaulitz.mockyup.server.controllers.cms;

import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.PROJECT_CONTRACTS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.cms.CmsFacade;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.contract.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.UpdateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(V1)
public class ContractController extends BaseController {

  @Autowired
  private CmsFacade cmsFacade;

  @PreAuthorize("hasAnyAuthority('PROJECT_CONTRACTS_READ','PROJECT_CONTRACTS_READ_WRITE')")
  @GetMapping(value = PROJECT_CONTRACTS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> getAll(@ModelAttribute MandatoryModel mandatoryModel,
      @Valid GetProjectContractParam getProjectContractParam) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.cmsFacade.allContractCards(getProjectContractParam),
            mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('PROJECT_CONTRACTS_READ','PROJECT_CONTRACTS_READ_WRITE')")
  @PostMapping(value = PROJECT_CONTRACTS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> createContract(
      @RequestBody @Valid CreateProjectContractRequest createProjectContractRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(
            this.cmsFacade.createContract(createProjectContractRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAuthority('PROJECT_CONTRACTS_READ_WRITE')")
  @PutMapping(value = PROJECT_CONTRACTS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> updateContract(@PathVariable String id,
      @RequestBody @Valid UpdateProjectContractRequest updateProjectContractRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(
            this.cmsFacade.updateContract(id, updateProjectContractRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('PROJECT_CONTRACTS_READ','PROJECT_CONTRACTS_READ_WRITE')")
  @GetMapping(value = PROJECT_CONTRACTS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> getByContractId(@PathVariable String id,
      @ModelAttribute MandatoryModel mandatoryModel) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.cmsFacade.getByContractId(id),
            mandatoryModel));
  }
}