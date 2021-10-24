<template>
  <contract-page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Create New Contract</h1>
      <div class="page-controller ms-auto">
        <div class="d-inline-flex align-items-center  justify-content-center">
          <form-container class="form-check d-inline-flex me-3">
            <form-input-checkbox class="" v-model="isPrivateFormAttribute.value"
                                 :input-attributes="isPrivateFormAttribute"
                                 :event-submitted="isPrivateFormAttribute.formSubmitted"
                                 value=true
            />
            <form-label class="ms-2 mb-0"><span class="fas fa-lock"/>Private Project</form-label>
          </form-container>
          <form-button class="btn btn-primary btn-md " @click.stop.prevent="createNewContracts"
                       :form-button-attribute="formButtonAttributes"><span class="fas fa-save"/> Submit New Contract
          </form-button>
        </div>
      </div>
    </div>
    <div class="mt-3">
      <div class="row">
        <div class="col-md-6">
          <div class="d-flex holder">
            <h5 class="page-title ">OpenApi Spec (json)</h5>
            <form-button class="btn btn-primary btn-sm ms-auto" @click="initTemplateContract"
                         :form-button-attribute="formButtonAttributes"><span class="fas fa-copy"/>
              Init Template Contract
            </form-button>
          </div>
          <v-ace-editor v-model:value="contract" id="code-editor" lang="json" theme="github"/>
        </div>
        <div class="col-md-6">
          <h5 class="page-title holder">Swagger Spec</h5>
          <swagger-component :spec="contract"></swagger-component>
        </div>
      </div>
    </div>
  </contract-page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ButtonAttribute, InputAttribute } from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import ContractPageContainer from '@/pages/ContractPageContainer.vue'
import { VAceEditor } from 'vue3-ace-editor'
import 'ace-builds/src-noconflict/mode-json'
import 'ace-builds/src-noconflict/theme-github'
import SwaggerComponent from '@/components/swagger/SwaggerComponent.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import FormInputCheckbox from '@/shared/form/FormInputCheckbox.vue'
import FormLabel from '@/shared/form/FormLabel.vue'
import { defaultContract } from '@/service/helper/ContractHelper'
import { ContractService } from '@/service/webclient/service/ContractService'
import { ContractCreateRequest } from '@/service/webclient/model/Contracts'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'ContractsCreate',
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      service: ContractService,
      contract: '{\n' +
        '    \n' +
        '}',
      directionAfterSubmit: {
        name: 'ProjectsDetail',
        id: this.$route.params.id
      },
      isPrivateFormAttribute: {
        type: 'checkbox',
        validations: []
      } as InputAttribute,
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttribute
    }
  },
  components: {
    BreadcrumbContainer,
    FormLabel,
    FormInputCheckbox,
    FormContainer,
    SwaggerComponent,
    VAceEditor,
    ContractPageContainer,
    FormButton
  },
  mounted () {
    this.breadCrumbAttributes = [
      {
        label: 'Project',
        routerLink: {
          name: 'ProjectsDetail',
          id: this.$route.params.id
        },
        isActive: false
      },
      {
        label: 'Create',
        routerLink: {
          name: 'ProjectsCreate'
        },
        isActive: true
      }
    ]
  },
  methods: {
    createNewContracts () {
      this.isPrivateFormAttribute.formSubmitted = true
      this.payloadRequest = {
        private: this.isPrivateFormAttribute.value,
        projectId: this.$route.params.id,
        spec: JSON.parse(this.contract)
      } as ContractCreateRequest
      this.formButtonAttributes.isLoading = false
      this.createNewData()
    },
    initTemplateContract () {
      this.contract = JSON.stringify(defaultContract, null, '\t')
    }
  }
})
</script>

<style scoped lang="scss">
#code-editor {
  position: relative;
  width: 100%;
  min-height: 720px;
}
</style>
