<template>
  <nav class="navbar navbar-expand-lg bg-indigo-800">
    <div class="container-fluid">
      <router-link class="navbar-brand text-white" to="/"><span class="fas fa-align-left me-3"></span>DevStock | MOCK Service</router-link>
    </div>
  </nav>
  <div class="d-inline-flex  login-page w-100 ">
    <div class="d-inline-flex align-items-center flex-fill">
      <div class="container w-100 d-inline-flex">
        <div class="col-md-8">
          <h3>Login Dashboard Mockyup</h3>
          <p>API Contract and Mock server base on swagger </p>
          <ul>
            <li>As Mock Server</li>
            <li>As API Contract</li>
            <li>As Simplify your collaboration kit</li>
          </ul>
        </div>
        <div class="col-md-4">
          <card-container>
            <card-body>
              <div class="card-header no-bg text-center">
                <h5 class="card-title">Sign in to your account</h5>
              </div>
              <card-body>
                <form-group>
                  <form-container>
                    <alert-container v-if="alertAttributes.show" :alert-attributes="alertAttributes" @showAlert:alert="closeAlert"/>
                    <form-label for="username">Username</form-label>
                    <form-input id="username" v-model="userNameOrEmailInputAttributes.value"
                                :input-attributes="userNameOrEmailInputAttributes"
                                :event-submitted="userNameOrEmailInputAttributes.formSubmitted"
                    />
                  </form-container>
                  <form-container>
                    <form-label>Password</form-label>
                    <form-input v-model="passwordInputAttributes.value"
                                :input-attributes="passwordInputAttributes"
                                :event-submitted="passwordInputAttributes.formSubmitted"
                    />
                  </form-container>
                  <form-container class="form-check">
                    <form-input-checkbox v-model="rememberMeInputAttributes.value"
                                         :input-attributes="rememberMeInputAttributes"
                                         :event-submitted="rememberMeInputAttributes.formSubmitted"
                                         value=true
                    />
                    <form-label>Remember me</form-label>
                  </form-container>
                  <form-button class="mt-2 w-100" @click.stop.prevent="doLogin"
                               :form-button-attribute="formButtonAttributes">Sign in
                  </form-button>
                </form-group>
              </card-body>
            </card-body>
          </card-container>
        </div>
      </div>
    </div>
  </div>
  <footer-navigation/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import FormLabel from '@/shared/form/FormLabel.vue'
import FormContainer from '@/shared/form/FormContainer.vue'
import FormGroup from '@/shared/form/FormGroup.vue'
import CardBody from '@/shared/card/CardBody.vue'
import FormInput from '@/shared/form/FormInput.vue'
import CardContainer from '@/shared/card/CardContainer.vue'
import {
  ButtonAttribute,
  InputAttribute,
  InputValidationType
} from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import FormHelper from '@/shared/form/FormHelper'
import FormInputCheckbox from '@/shared/form/FormInputCheckbox.vue'
import AuthService from '@/plugins/webclient/tmp/serice/AuthService'
import FooterNavigation from '@/components/FooterNavigation.vue'
import AlertContainer from '@/shared/alert/AlertContainer.vue'
import BaseComponent from '@/shared/base/BaseComponent'
export default defineComponent({
  mixins: [FormHelper, BaseComponent],
  data () {
    return {
      userNameOrEmailInputAttributes: {
        type: 'text',
        placeHolder: 'Username or Email',
        hint: 'Your username or email',
        validMessage: 'Looks good !',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input your username or email'
          }
        ]
      } as InputAttribute,
      passwordInputAttributes: {
        type: 'password',
        placeHolder: 'Password',
        validMessage: 'Looks good !',
        validations: [
          {
            validationType: InputValidationType.REQUIRED,
            errMessage: 'Please input your password'
          }
        ]
      } as InputAttribute,
      rememberMeInputAttributes: {
        type: 'checkbox',
        validations: []
      } as InputAttribute,
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttribute
    }
  },
  methods: {
    doLogin (): void {
      this.userNameOrEmailInputAttributes.formSubmitted = true
      this.passwordInputAttributes.formSubmitted = true
      this.formButtonAttributes.isLoading = true
      const userNameOrEmail = this.userNameOrEmailInputAttributes.value
      const password = this.passwordInputAttributes.value
      const rememberMe = this.rememberMeInputAttributes.value
      if (!this.passwordInputAttributes.isValid || !this.userNameOrEmailInputAttributes.isValid) {
        this.formButtonAttributes.isLoading = false
      } else {
        AuthService.doLogin({
          usernameOrEmail: userNameOrEmail,
          password: password,
          rememberMe: rememberMe
        }).then(value => {
          this.formButtonAttributes.isLoading = false
          this.$router.push('/')
        }).catch(error => {
          this.formButtonAttributes.isLoading = false
          this.validateResponse(error)
        })
      }
    }
  },
  components: {
    AlertContainer,
    FooterNavigation,
    FormInputCheckbox,
    FormButton,
    CardContainer,
    FormLabel,
    FormContainer,
    FormInput,
    FormGroup,
    CardBody
  }
})
</script>
<style scoped lang="scss">
.login-page {
  width: auto;
  height: 720px;
}
</style>
