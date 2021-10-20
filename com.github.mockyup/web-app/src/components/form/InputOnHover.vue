<template>
  <div class="d-flex editor-on-hover">
    <div v-show="!isEditActive">
      <slot name="content"></slot>
    </div>
    <div v-show="isEditActive">
      <slot name="editor"></slot>
    </div>
    <div class="editor-flag button-container ms-2" :class="{'d-block':isEditActive}">
      <button @click="editorActive" class=""><span class="fas fa-pencil-alt small"></span></button>
      <button @click="editorNotActive" class=""><span class="fas fa-times small"></span></button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import BaseComponent from '@/shared/base/BaseComponent'
import { InputAttribute, InputValidationType } from '@/shared/form/InputModel'

export default defineComponent({
  name: 'InputOnHover',
  mixins: [BaseComponent],
  emits: ['update:editorIsActive'],
  data () {
    return {
      isEditActive: false,
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
      } as InputAttribute
    }
  },
  props: {
    id: { type: String },
    eventSubmitted: {
      type: Boolean,
      default: false
    },
    inputAttributes: { type: Object as PropType<InputAttribute> }
  },
  methods: {
    editorActive () {
      this.isEditActive = true
      this.$emit('update:editorIsActive', true)
    },
    editorNotActive () {
      this.isEditActive = false
      this.$emit('update:editorIsActive', false)
    }
  }
})
</script>

<style scoped lang="scss">
.editor-on-hover {
  >.editor-flag{
    display: none;
  }
  &:hover {
    >.editor-flag {
      display: block;
    }
  }
}
</style>
