<template>
  <div v-bind="$attrs">
    <div id="toolbar-container"></div>
    <textarea id="text-editor" v-model="content">
    </textarea>
  </div>
</template>

<script lang="ts">
import ClassicEditor from '@ckeditor/ckeditor5-build-classic/build/ckeditor'
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'TextEditor',
  emits: ['update:modelValue'],
  data () {
    return {
      contentData: ClassicEditor,
      content: '',
      $_instance: null
    }
  },
  props: {
    contentValue: { type: String, default () { return '' } }
  },
  async mounted () {
    // eslint-disable-next-line no-undef
    await this.contentData.create(document.querySelector('#text-editor'))
      .then(editor => {
        // const toolbarContainer = document.querySelector('#toolbar-container')
        // // toolbarContainer.appendChild(editor.ui.view.toolbar.element)
        // // this.contentData = editor
        // // this.contentData = markRaw(editor)
        this.$_instance = editor
        this.setUpEditor()
      })
      .catch(error => {
        console.error(error)
      })
    this.content = this.contentValue
  },
  watch: {
    modelValue (newValue, oldValue) {
      if (newValue !== oldValue && newValue !== this.$_lastEditorData) {
        this.$_instance.setData(newValue)
      }
    }
  },
  methods: {
    setUpEditor () {
      const editor = this.$_instance
      editor.setData(this.contentValue)
      const publishMessage = (message: any) => {
        this.$emit('update:modelValue', message)
      }
      editor.model.document.on('change:data', function () {
        const message = editor.getData()
        publishMessage(message)
      })
    }
  }
})
</script>

<style>
.ck-content {
  min-height: 450px !important;
}
</style>
