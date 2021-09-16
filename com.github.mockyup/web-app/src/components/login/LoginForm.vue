<template>
  <card-container>
    <card-body>
      <card-body>
        <form-group>
          <form-container>
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
            />
            <form-label>Remember me</form-label>
          </form-container>
          <form-button class="mt-2" @click.stop.prevent="something"
                       :form-button-attribute="formButtonAttributes">Sign in
          </form-button>
        </form-group>
      </card-body>
    </card-body>
  </card-container>
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
  ButtonAttributeInterface,
  InputAttributeInterface,
  InputValidationType
} from '@/shared/form/InputModel'
import FormButton from '@/shared/form/FormButton.vue'
import FormHelper from '@/shared/form/FormHelper'
import FormInputCheckbox from '@/shared/form/FormInputCheckbox.vue'
import { AuthLoginService } from '@/plugins/webclient/serice/Service'
import { AuthResponse } from '@/plugins/webclient/model/ResponseModel'

export default defineComponent({
  name: 'LoginForm',
  mixins: [FormHelper],
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
      } as InputAttributeInterface,
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
      } as InputAttributeInterface,
      rememberMeInputAttributes: {
        type: 'checkbox',
        validations: []
      } as InputAttributeInterface,
      formButtonAttributes: {
        isLoading: false,
        usingLoader: true
      } as ButtonAttributeInterface
    }
  },
  methods: {
    something (): void {
      this.userNameOrEmailInputAttributes.formSubmitted = true
      this.passwordInputAttributes.formSubmitted = true
      this.formButtonAttributes.isLoading = true
      const userNameOrEmail = this.userNameOrEmailInputAttributes.value
      const password = this.passwordInputAttributes.value
      const rememberMe = this.rememberMeInputAttributes.value
      if (!this.passwordInputAttributes.isValid || !this.userNameOrEmailInputAttributes.isValid) {
        this.formButtonAttributes.isLoading = false
      }
      AuthLoginService.doLogin({
        usernameOrEmail: userNameOrEmail,
        password: password,
        rememberMe: rememberMe
      }).then(value => {
        this.formButtonAttributes.isLoading = false
        this.$router.push('/')
      }).catch(reason => {
        this.formButtonAttributes.isLoading = false
        console.log(reason)
      })
    }
  },
  components: {
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
