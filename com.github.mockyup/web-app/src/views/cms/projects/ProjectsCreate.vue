<template>
  <page-container>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Create new project</h1>
    </div>
    <div class="row mt-3">
      <div class="col-9">
        <form-group>
          <form-container>
            <alert-container v-if="alertAttributes.show" :alert-attributes="alertAttributes"
                             @showAlert:alert="closeAlert"/>
            <form-label for="projectName" class="label-bold">Project name</form-label>
            <form-input id="projectName"
                        v-model="projectNameInputAttribute.value"
                        :input-attributes="projectNameInputAttribute"
                        :event-submitted="projectNameInputAttribute.formSubmitted"
            />
            <form-label for="ProjectDescription" class="label-bold mt-3">Project Description</form-label>
            <text-editor v-model="projectDescriptionInputAttribute.value" :get-content="projectDescriptionInputAttribute.formSubmitted"/>
            <select class="form-select" multiple aria-label="multiple select example" v-model="projectTagsInputAttribute.value">
              <option selected>Open this select menu</option>
              <option value="1">One</option>
              <option value="2">Two</option>
              <option value="3">Three</option>
            </select>

            <div class="d-md-flex justify-content-md-end">
              <form-button class="mt-2 w-md" @click.stop.prevent="createNewProject"
                           :form-button-attribute="formButtonAttributes">Create new project
              </form-button>
            </div>
          </form-container>
        </form-group>
      </div>
      <div class="col-3">
        something
      </div>
    </div>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'
import { ProjectService } from '@/plugins/webclient/service/CmsService'
import BaseComponent from '@/components/base/BaseComponent'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import FormGroup from '@/shared/form/FormGroup.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import AlertContainer from '@/shared/alert/AlertContainer.vue'
import FormLabel from '@/shared/form/FormLabel.vue'
import FormInput from '@/shared/form/FormInput.vue'
import { ButtonAttribute, InputAttribute, InputValidationType } from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import { ProjectCreateRequest } from '@/plugins/webclient/model/Projects'
// import TextInput from '@/shared/form/TextInput.vue'
import AceEditor from '@/shared/editor/CodeEditor.vue'
import TextEditor from '@/shared/editor/TextEditor.vue'

export default defineComponent({
  name: 'Projects',
  mixins: [BaseComponent],
  data () {
    return {
      projectNameInputAttribute: {
        type: 'text',
        placeHolder: 'Project Name',
        validMessage: 'Looks good !',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input your project name'
          },
          {
            validationType: InputValidationType.Length,
            maxLength: 200,
            errMessage: 'Maximum 200 characters!'
          }
        ]
      } as InputAttribute,
      projectDescriptionInputAttribute: {
        type: 'text',
        isValid: false
      } as InputAttribute,
      projectTagsInputAttribute: {
        type: 'text',
        value: []
      } as InputAttribute,
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttribute
    }
  },
  components: {
    TextEditor,
    // AceEditor,
    // TextInput,
    FormButton,
    FormInput,
    FormLabel,
    AlertContainer,
    FormContainer,
    FormGroup,
    PageContainer
  },
  beforeMount () {
    this.service = ProjectService
    this.directionAfterSubmit = {
      name: 'Projects'
    }
  },
  methods: {
    createNewProject (): void {
      this.projectNameInputAttribute.formSubmitted = true
      this.projectDescriptionInputAttribute.formSubmitted = true
      this.payloadRequest = {
        projectName: this.projectNameInputAttribute.value,
        projectDescription: this.projectDescriptionInputAttribute.value,
        projectTags: this.projectTagsInputAttribute.value
      }as ProjectCreateRequest
      console.log(this.payloadRequest)
      if (!this.projectNameInputAttribute.isValid) {
        this.formButtonAttributes.isLoading = false
      } else {
        this.createNewData()
      }
    }
  }
})
</script>

<style scoped>

</style>
