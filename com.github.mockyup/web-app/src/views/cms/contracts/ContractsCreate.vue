<template>
  <contract-page-container>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Create New Contract</h1>
      <div class="page-controller ms-auto">
        <div class="d-inline-flex align-content-center  justify-content-center">
          <div class="me-3 ">
            <form-container class="form-check d-inline-flex pt-2">
              <form-input-checkbox v-model="isPrivateFormAttribute.value"
                                   :input-attributes="isPrivateFormAttribute"
                                   :event-submitted="isPrivateFormAttribute.formSubmitted"
                                   value=true
              />
              <form-label class="ms-2"><span class="fas fa-lock"/>Private Project</form-label>
            </form-container>
          </div>
          <form-button class="btn btn-primary btn-md" @click.stop.prevent="createNewContracts"
                       :form-button-attribute="formButtonAttributes">Submit New Contract
          </form-button>
        </div>
      </div>
    </div>
    <div class="mt-3">
      <div class="row">
        <alert-container v-if="alertAttributes.show" :alert-attributes="alertAttributes" @showAlert:alert="closeAlert"/>
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
import { defaultContract } from '@/service/helper/ContractService'
import { ContractService } from '@/service/webclient/service/ContractService'
import { ContractCreateRequest } from '@/service/webclient/model/Contracts'
import AlertContainer from '@/shared/alert/AlertContainer.vue'

export default defineComponent({
  name: 'ContractsCreate',
  mixins: [BaseViewComponent],
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
    AlertContainer,
    FormLabel,
    FormInputCheckbox,
    FormContainer,
    SwaggerComponent,
    VAceEditor,
    ContractPageContainer,
    FormButton
  },
  methods: {
    createNewContracts () {
      this.isPrivateFormAttribute.formSubmitted = true
      this.payloadRequest = {
        isPrivate: this.isPrivateFormAttribute.value,
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
