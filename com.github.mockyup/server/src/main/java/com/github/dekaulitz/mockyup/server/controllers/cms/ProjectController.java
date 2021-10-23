package com.github.dekaulitz.mockyup.server.controllers.cms;

import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.COUNT;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.PROJECTS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.TAGS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.ProjectsFacade;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectTagsParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class ProjectController extends BaseController {

  @Autowired
  private ProjectsFacade projectsFacade;

  @PreAuthorize("hasAuthority('PROJECTS_READ_WRITE')")
  @PostMapping(value = PROJECTS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> createProject(
      @RequestBody @Valid CreateProjectRequest createProjectRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.createProject(createProjectRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAuthority('PROJECTS_READ_WRITE')")
  @PutMapping(value = PROJECTS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> updateProject(@PathVariable String id,
      @RequestBody @Valid UpdateProjectRequest updateProjectRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.updateProject(id, updateProjectRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAuthority('PROJECTS_READ_WRITE')")
  @DeleteMapping(value = PROJECTS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> deleteProject(@PathVariable String id,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    this.projectsFacade.getProjectService().deleteById(id);
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(null,
            mandatoryModel));
  }


  @PreAuthorize("hasAnyAuthority('PROJECTS_READ_WRITE','PROJECTS_READ')")
  @GetMapping(value = PROJECTS + TAGS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> getProjectTags(
      @ModelAttribute MandatoryModel mandatoryModel,@Valid GetProjectTagsParam getProjectTagsParam)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.getProjectTag(getProjectTagsParam),
            mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('PROJECTS_READ_WRITE','PROJECTS_READ')")
  @GetMapping(value = PROJECTS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseModel> getProjectById(@PathVariable String id,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.getProjectDetail(id),
            mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('PROJECTS_READ_WRITE','PROJECTS_READ')")
  @GetMapping(value = PROJECTS + COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getCount(@ModelAttribute MandatoryModel mandatoryModel,
      @Valid GetProjectParam getProjectParam) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.getCount(getProjectParam), mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('PROJECTS_READ_WRITE','PROJECTS_READ')")
  @GetMapping(value = PROJECTS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAll(@ModelAttribute MandatoryModel mandatoryModel,
      @Valid GetProjectParam getProjectParam) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.projectsFacade.allProjects(getProjectParam),
            mandatoryModel));
  }
}
