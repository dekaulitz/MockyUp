<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Edit User</h1>
      <div class="page-controller ms-auto">
        <form-button class="btn btn-primary w-md btn-md" @click.stop.prevent="updateProfile"
                     :form-button-attribute="formButtonAttributes"><span class="fa fa-save"/> Update
          Profile
        </form-button>
      </div>
    </div>
    <card-container class="mt-3">
      <card-body>
        <div v-if="placeHolderActive">
          <place-holder-container/>
        </div>
        <form-group v-if="!placeHolderActive">
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
              <form-container>
                <form-label for="password">Password</form-label>
                <form-input class="input-w-md" id="password" v-model="passwordInputAttributes.value"
                            :input-attributes="passwordInputAttributes"
                            :event-submitted="passwordInputAttributes.formSubmitted"
                />
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
import { ButtonAttribute, InputAttribute, InputValidationType } from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import FormGroup from '@/shared/form/FormGroup.vue'
import FormLabel from '@/shared/form/FormLabel.vue'
import FormInput from '@/shared/form/FormInput.vue'
import AlertContainer from '@/shared/alert/AlertContainer.vue'
import { AccessInterface } from '@/service/helper/AccessHelper'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'
import AuthService from '@/service/webclient/service/AuthService'
import { AuthResponse } from '@/service/webclient/model/ResponseModel'
import { UpdateProfileAuthRequest } from '@/service/webclient/model/RequestModel'
import { StorageService } from '@/service/webclient/service/CommonService'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'

export default defineComponent({
  name: 'AuthProfileEdit',
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      service: AuthService,
      request: {} as UpdateProfileAuthRequest,
      data: {} as AuthResponse,
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
      passwordInputAttributes: {
        type: 'password',
        placeHolder: 'Password',
        validMessage: 'Looks good !',
        hint: 'If not set will not update current password',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input your password'
          },
          {
            validationType: InputValidationType.Length,
            minLength: 10,
            errMessage: 'Minimal 10 characters'
          }
        ]
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
    this.data = await this.service.getAuthDetail().then(value => {
      return value
    }).catch(reason => {
      this.validateResponse(reason)
    })
    this.breadCrumbAttributes = [
      {
        label: 'Profile',
        routerLink: {
          name: 'AuthProfile'
        },
        isActive: false
      },
      {
        label: 'Edit',
        routerLink: {
          name: 'AuthProfileEdit'
        },
        isActive: true
      }
    ]
    const user = this.data
    this.usernameInputAttributes.value = user.username
    this.emailInputAttributes.value = user.email
    this.placeHolderActive = false
  },
  methods: {
    async updateProfile () {
      this.usernameInputAttributes.formSubmitted = true
      this.emailInputAttributes.formSubmitted = true
      this.formButtonAttributes.isLoading = true
      this.request = {
        username: this.usernameInputAttributes.value,
        email: this.emailInputAttributes.value,
        password: this.passwordInputAttributes.value
      }
      if (!this.usernameInputAttributes.isValid || !this.emailInputAttributes.isValid) {
        this.formButtonAttributes.isLoading = false
      } else {
        this.service.doUpdateProfile(this.request)
          .then(value => {
            StorageService.setData<AuthResponse>(StorageKeyType.AUTH_PROFILE, value)
          }).catch(reason => {
            this.validateResponse(reason)
          }).finally(() => {
            this.formButtonAttributes.isLoading = false
          })
      }
    }
  }
})
</script>

<style scoped lang="scss">
</style>
