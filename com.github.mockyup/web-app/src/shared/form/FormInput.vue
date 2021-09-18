<template>
  <input v-bind="$attrs" class="form-control"
         :id="id" :type="inputAttributes.type"
         @input="$emit('update:modelValue', $event.target.value);validateAttributes(inputAttributes)"
         :placeholder="inputAttributes.placeHolder" :class="isValidate">
  <div class="form-text">{{ inputAttributes.hint }}</div>
  <div class="valid-feedback">
    {{ successMessage }}
  </div>
  <div class="invalid-feedback">
    {{ errorMessage }}
  </div>
</template>
<script lang="ts">

import { defineComponent, PropType } from 'vue'
import { InputAttributeInterface } from '@/shared/form/InputModel'
import FormHelper from '@/shared/form/FormHelper'

interface InputAdditionalInfo {
  hasError: boolean
  errorMessage?: string
  successMessage?: string
  isValidate?: boolean
}

export default defineComponent({
  emits: ['update:userInput', 'update:modelValue'],
  mixins: [FormHelper],
  data () {
    return {
      errorMessage: '',
      successMessage: '',
      hasError: false
    } as InputAdditionalInfo
  },
  props: {
    id: { type: String },
    eventSubmitted: {
      type: Boolean,
      default: false
    },
    inputAttributes: { type: Object as PropType<InputAttributeInterface> }
  },
  computed: {
    isValidate (): string {
      if (this.hasError) {
        return 'is-invalid'
      } else if (!this.hasError && this.inputAttributes.isValid) {
        return 'is-valid'
      }
      return ''
    }
  },
  methods: {
    validateAttributes (formInputAction: InputAttributeInterface) {
      this.validate(formInputAction)
      if ((undefined !== formInputAction.isValid && formInputAction.isValid === false) && (formInputAction.errorMessage !== undefined || formInputAction.errorMessage !== '')) {
        this.hasError = true
        this.errorMessage = formInputAction.errorMessage
      } else {
        this.hasError = false
        this.errorMessage = ''
        this.successMessage = formInputAction.validMessage
      }
    }
  },
  watch: {
    eventSubmitted (submitted: boolean) {
      if (submitted === true) {
        this.validateAttributes(this.inputAttributes)
      }
    }
  }

})
</script>

<style scoped>

</style>
