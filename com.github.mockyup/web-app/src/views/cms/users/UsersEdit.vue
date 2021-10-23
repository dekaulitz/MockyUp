<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Edit User</h1>
      <div class="page-controller ms-auto">
        <form-button class="btn btn-primary w-md btn-md" @click.stop.prevent="updateUser"
                     :form-button-attribute="formButtonAttributes"><span class="fa fa-save"/> Update
          User
        </form-button>
      </div>
    </div>
    <card-container class="mt-3">
      <card-body>
        <div v-if="showPlaceHolder">
          <place-holder-container/>
        </div>
        <form-group v-if="!showPlaceHolder">
          <alert-container v-if="alertAttributes.show" :alert-attributes="alertAttributes"
                           @showAlert:alert="closeAlert"/>
          <div class="row">
            <div class="col-md-8">
              <form-container>
                <form-label for="username">Username</form-label>
                <form-input class="input-w-md" id="username" v-model="usernameInputAttributes.value"
                            :input-attributes="usernameInputAttributes"
                            :event-submitted="usernameInputAttributes.formSubmitted"
                />
              </form-container>
              <form-container>
                <form-label for="email">Email</form-label>
                <form-input class="input-w-md" id="email" v-model="emailInputAttributes.value"
                            :input-attributes="emailInputAttributes"
                            :event-submitted="emailInputAttributes.formSubmitted"
                />
              </form-container>
            </div>
            <div class="col-md-3">
              <form-label>Access Permissions:</form-label>
              <form-container class="form-check">
                <form-input-checkbox id="MOCKS_READ"
                                     v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=MOCKS_READ
                />
                <form-label>Can Read Mocks</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox id="MOCKS_READ_WRITE"
                                     v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value="MOCKS_READ_WRITE"
                />
                <form-label>Can Read and Modified Mocks</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value="USERS_READ"
                />
                <form-label>Can Read Users</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=USERS_READ_WRITE
                />
                <form-label>Can Read and Modified Users</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=PROJECTS_READ
                />
                <form-label>Can Read Projects</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=PROJECTS_READ_WRITE
                />
                <form-label>Can Read and Modified Projects</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=PROJECT_CONTRACTS_READ
                />
                <form-label>Can Read Contracts</form-label>
              </form-container>
              <form-container class="form-check">
                <form-input-checkbox v-model="accessPermissionsInputAttributes.values"
                                     :input-attributes="accessPermissionsInputAttributes"
                                     :event-submitted="accessPermissionsInputAttributes.formSubmitted"
                                     value=PROJECT_CONTRACTS_READ_WRITE
                />
                <form-label>Can Read and Modified Contracts</form-label>
              </form-container>
            </div>
          </div>
        </form-group>
      </card-body>
    </card-container>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'
import { UserService } from '@/service/webclient/service/UserService'
import { ButtonAttribute, InputAttribute, InputValidationType } from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import FormGroup from '@/shared/form/FormGroup.vue'
import FormLabel from '@/shared/form/FormLabel.vue'
import FormInput from '@/shared/form/FormInput.vue'
import { UserDetailResponse, UserUpdateRequest } from '@/service/webclient/model/Users'
import AlertContainer from '@/shared/alert/AlertContainer.vue'
import FormInputCheckbox from '@/shared/form/FormInputCheckbox.vue'
import { AccessData, AccessInterface } from '@/service/helper/AccessHelper'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'UsersEdit',
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      service: UserService,
      payloadRequest: {} as UserUpdateRequest,
      data: {} as UserDetailResponse,
      accessList: [] as AccessInterface[],
      directionAfterSubmit: {
        name: 'Users'
      },
      usernameInputAttributes: {
        type: 'text',
        placeHolder: 'Username',
        hint: 'Your username',
        validMessage: 'Looks good !',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input username'
          }
        ]
      } as InputAttribute,
      emailInputAttributes: {
        type: 'email',
        placeHolder: 'email',
        hint: 'Your email',
        validMessage: 'Looks good !',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input email'
          },
          {
            validationType: InputValidationType.EMAIL,
            errMessage: 'Please input email'
          }
        ]
      } as InputAttribute,
      accessPermissionsInputAttributes: {
        type: 'checkbox',
        validations: [],
        values: [] as string[]
      } as InputAttribute,
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttribute
    }
  },
  components: {
    BreadcrumbContainer,
    PlaceHolderContainer,
    FormInputCheckbox,
    AlertContainer,
    FormInput,
    FormLabel,
    FormGroup,
    FormContainer,
    CardBody,
    CardContainer,
    FormButton,
    PageContainer
  },
  async mounted () {
    await this.getByDetail(this.$route.params.id)
    this.breadCrumbAttributes = [
      {
        label: 'User',
        routerLink: {
          name: 'UsersDetail',
          id: this.data.id
        },
        isActive: false
      },
      {
        label: 'Edit',
        routerLink: {
          name: 'UsersEdit',
          id: this.data.id
        },
        isActive: true
      }
    ]

    const accessData = AccessData
    const user = this.data
    this.accessPermissionsInputAttributes.values = user.access
    this.usernameInputAttributes.value = user.username
    this.emailInputAttributes.value = user.email
    this.data.access.forEach(access => {
      if (accessData.has(access)) {
        this.accessList.push(accessData.get(access))
      }
    })
    this.showPlaceHolder = false
  },
  methods: {
    updateUser () {
      this.usernameInputAttributes.formSubmitted = true
      this.emailInputAttributes.formSubmitted = true
      this.accessPermissionsInputAttributes.formSubmitted = true
      this.formButtonAttributes.isLoading = true
      this.payloadRequest = {
        username: this.usernameInputAttributes.value,
        email: this.emailInputAttributes.value,
        access: this.accessPermissionsInputAttributes.values
      }
      if (!this.usernameInputAttributes.isValid || !this.emailInputAttributes.isValid) {
        this.formButtonAttributes.isLoading = false
      } else {
        this.formButtonAttributes.isLoading = false
        this.updateData()
      }
    }
  }
})
</script>

<style scoped lang="scss">
</style>
