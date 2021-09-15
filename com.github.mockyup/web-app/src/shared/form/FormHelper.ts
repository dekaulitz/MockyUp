import { defineComponent } from 'vue'
import {
  InputAttributeInterface,
  InputValidationType
} from '@/shared/form/InputModel'

export default defineComponent({
  data () {
    return {
      isSubmitted: false
    }
  },
  methods: {
    validate (inputAttribute: InputAttributeInterface): void {
      this.checkingValidation(inputAttribute)
    },
    checkingValidation (inputAttribute: InputAttributeInterface) {
      const value = inputAttribute.value
      for (const validation of inputAttribute.validations) {
        if (validation.validationType === InputValidationType.REQUIRED && (value === '' || value === undefined)) {
          inputAttribute.isValid = false
          inputAttribute.errorMessage = validation.errMessage
          break
        } else if (validation.validationType === InputValidationType.EMAIL) {
          if (!/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(value)) {
            inputAttribute.isValid = false
            inputAttribute.errorMessage = validation.errMessage
            break
          }
        } else if (validation.validationType === InputValidationType.Length) {
          if (validation.minLength != null && value < validation.minLength) {
            inputAttribute.isValid = false
            inputAttribute.errorMessage = validation.errMessage
            break
          } else if (validation.maxLength != null && value > validation.maxLength) {
            inputAttribute.isValid = false
            inputAttribute.errorMessage = validation.errMessage
            break
          }
        } else {
          inputAttribute.isValid = true
          inputAttribute.errorMessage = ''
        }
      }
    }
  }
})
