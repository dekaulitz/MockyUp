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
    await this.contentData.create(document.querySelector('#text-editor'), {
      codeBlock: {
        languages: [
          // Do not render the CSS class for the plain text code blocks.
          { language: 'plaintext', label: 'Plain text', class: '' },

          // Use the "php-code" class for PHP code blocks.
          { language: 'php', label: 'PHP', class: 'php-code' },

          // Use the "js" class for JavaScript code blocks.
          // Note that only the first ("js") class will determine the language of the block when loading data.
          { language: 'javascript', label: 'JavaScript', class: 'js javascript js-code' },

          // Python code blocks will have the default "language-python" CSS class.
          { language: 'python', label: 'Python' }
        ]
      },
      toolbar: {
        items: [
          'heading', '|',
          'fontfamily', 'fontsize', '|',
          'alignment', '|',
          'fontColor', 'fontBackgroundColor', '|',
          'bold', 'italic', 'strikethrough', 'underline', 'subscript', 'superscript', '|',
          'link', '|',
          'outdent', 'indent', '|',
          'bulletedList', 'numberedList', 'todoList', '|',
          'code',
          'codeBlock', '|',
          'insertTable', '|',
          // 'uploadImage',
          'blockQuote', '|',
          'undo', 'redo'
        ],
        shouldNotGroupWhenFull: false
      }
    })
      .then(editor => {
        // const toolbarContainer = document.querySelector('#toolbar-container')
        // // toolbarContainer.appendChild(editor.ui.view.toolbar.element)
        // // this.contentData = editor
        // // this.contentData = markRaw(editor)
        this.$_instance = editor
        this.setUpEditor()
        console.log(Array.from(editor.ui.componentFactory.names()))
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
