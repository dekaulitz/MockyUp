<template>
  <page-container>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Create new project</h1>
      <div class="page-controller ms-auto">
        <form-button class="btn btn-primary btn-md" @click.stop.prevent="updateProject"
                     :form-button-attribute="formButtonAttributes">Update Project
        </form-button>
      </div>
    </div>
    <div class="row mt-3">
      <div v-if="placeHolderActive">
        <place-holder-container/>
      </div>
        <div class="col-9" v-if="!placeHolderActive">
          <form-group>
            <form-container>
              <form-input id="projectName"
                          v-model="projectNameInputAttribute.value"
                          :input-attributes="projectNameInputAttribute"
                          :event-submitted="projectNameInputAttribute.formSubmitted"
              />
              <text-editor class="mt-3" v-model="projectDescriptionInputAttribute.value"
                           :get-content="projectDescriptionInputAttribute.formSubmitted"
                           :content-value="projectDescriptionInputAttribute.value"
              />
            </form-container>
          </form-group>
        </div>
        <div class="col-3">
          <card-container>
            <card-body>
              <h5 class="page-title holder">Additional information</h5>
              <label class="text-break label-bold ">Project tags</label>
              <div class="text-start">
                <div class="d-inline-flex" v-for="(tag, index ) in Array.from(projectTags)" :key="index">
                  <delete-on-hover :value="tag" @update:deleteOnHover="removeTag">
                    <template #content>
                      <a class="me-2 project-tag"
                         :href="tag">{{ projectTag(tag, index) }}</a>
                    </template>
                  </delete-on-hover>
                </div>
              </div>
              <input-searching-tags @update:projectTags="getProjectTag"/>
            </card-body>
          </card-container>
        </div>
    </div>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'
import { ProjectService } from '@/service/webclient/service/ProjectService'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import FormGroup from '@/shared/form/FormGroup.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import FormInput from '@/shared/form/FormInput.vue'
import { ButtonAttribute, InputAttribute, InputValidationType } from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import { ProjectCreateRequest, ProjectResponse } from '@/service/webclient/model/Projects'
import TextEditor from '@/shared/editor/TextEditor.vue'
import InputSearchingTags from '@/components/projects/InputSearchingTags.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import DeleteOnHover from '@/components/form/DeleteOnHover.vue'

export default defineComponent({
  name: 'ProjectsEdit',
  mixins: [BaseViewComponent],
  data () {
    return {
      data: {} as ProjectResponse,
      service: ProjectService,
      placeHolderActive: true,
      directionAfterSubmit: {
        name: 'Projects'
      },
      projectTags: new Set<string>(),
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
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttribute
    }
  },
  components: {
    DeleteOnHover,
    PlaceHolderContainer,
    InputSearchingTags,
    CardBody,
    CardContainer,
    TextEditor,
    // AceEditor,
    // TextInput,
    FormButton,
    FormInput,
    FormContainer,
    FormGroup,
    PageContainer
  },
  async mounted () {
    await this.getDetail(this.$route.params.id)
    this.projectNameInputAttribute.value = this.data.projectName
    this.projectDescriptionInputAttribute.value = this.data.projectDescription
    this.projectTags = new Set(this.data.projectTags)
    this.placeHolderActive = false
  },
  methods: {
    async updateProject () {
      this.projectNameInputAttribute.formSubmitted = true
      this.projectDescriptionInputAttribute.formSubmitted = true
      this.request = {
        projectName: this.projectNameInputAttribute.value,
        projectDescription: this.projectDescriptionInputAttribute.value,
        projectTags: Array.from(this.projectTags)
      } as ProjectCreateRequest
      if (!this.projectNameInputAttribute.isValid) {
        this.formButtonAttributes.isLoading = false
      }
      if (this.projectNameInputAttribute.isValid) {
        await this.doUpdate()
        this.formButtonAttributes.isLoading = false
      }
    },
    getProjectTag (tag: string): void {
      this.projectTags.add(tag)
    },
    projectTag (tag: string, index: number): string {
      if (index !== this.projectTags.size - 1) {
        return `${tag},`
      } else {
        return tag
      }
    },
    removeTag (tag:string) {
      this.projectTags.delete(tag)
    }
  }
})
</script>

<style scoped lang="scss">
</style>
