<template>
  <input v-bind="$attrs" class="form-check-input"
         :id="id" :type="inputAttributes.type"
         :checked="isChecked"
         @input="onChange($event);validateAttributes(inputAttributes)"
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
import { InputAttribute } from '@/shared/form/InputModel'
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
    checked: {
      type: Boolean,
      default: false
    },
    value: { type: String },
    modelValue: { default: '' },
    trueValue: { default: true },
    falseValue: { default: false },
    inputAttributes: { type: Object as PropType<InputAttribute> }
  },
  computed: {
    isValidate (): string {
      if (this.hasError) {
        return 'is-invalid'
      } else if (!this.hasError && this.inputAttributes.isValid) {
        return 'is-valid'
      }
      return ''
    },
    isChecked () {
      if (this.modelValue instanceof Array) {
        return this.modelValue.includes(this.value)
      }
      // Note that `true-value` and `false-value` are camelCase in the JS
      return this.modelValue === this.trueValue
    }
  },
  methods: {
    validateAttributes (formInputAction: InputAttribute) {
      this.validate(formInputAction)
      if ((undefined !== formInputAction.isValid && formInputAction.isValid === false) && (formInputAction.errorMessage !== undefined || formInputAction.errorMessage !== '')) {
        this.hasError = true
        this.errorMessage = formInputAction.errorMessage
      } else {
        this.hasError = false
        this.errorMessage = ''
        this.successMessage = formInputAction.validMessage
      }
    },
    onChange (event:Event) {
      const target = event.target as HTMLInputElement
      const isChecked = target.checked
      if (this.modelValue instanceof Array) {
        const newValue = [...this.modelValue]
        if (isChecked) {
          newValue.push(this.value)
        } else {
          newValue.splice(newValue.indexOf(this.value), 1)
        }
        this.$emit('update:modelValue', newValue)
      } else {
        this.$emit('update:modelValue', isChecked ? this.trueValue : this.falseValue)
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
