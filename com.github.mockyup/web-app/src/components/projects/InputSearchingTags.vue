<template>
  <div class="mt-3">
    <div class="position-relative" v-show="projectTagResponses.length!==0">
      <div class="position-absolute bottom-0 end-0 w-100">
        <div class="list-group mb-1">
          <button type="button" class="list-group-item list-group-item-action text-start"
                  v-for="(tag,index) in projectTagResponses" :key="index" @click="addTag(tag.tag)">
            {{ tag.tag }}
          </button>
        </div>
      </div>
    </div>
    <input type="text" class="form-control w-100" v-model="inputTag"
           placeholder="Another tag" @input="getTags()" v-on:keyup.enter="onTagInput">
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BaseComponent from '@/components/base/BaseComponent'
import { ProjectService } from '@/plugins/webclient/service/CmsService'
import { GetProjectTags, ProjectTagsResponse } from '@/plugins/webclient/model/Projects'

export default defineComponent({
  name: 'InputSearchingTags',
  emits: ['update:projectTags'],
  mixins: [BaseComponent],
  components: {
  },
  data () {
    return {
      service: ProjectService,
      projectTags: [] as string[],
      projectTagResponses: [] as ProjectTagsResponse[],
      inputTag: '',
      parameter: {} as GetProjectTags
    }
  },
  methods: {
    getTags () {
      if (this.inputTag !== '') {
        this.parameter.tag = this.inputTag
        this.service.getTags(this.parameter)
          .then(value => {
            this.projectTagResponses = value
          }).catch(reason => {
            this.validateResponse(reason)
          })
      }
    },
    onTagInput () {
      if (this.inputTag !== '') {
        this.$emit('update:projectTags', this.inputTag)
        this.inputTag = ''
      }
    },
    addTag (tag:string) {
      this.$emit('update:projectTags', tag)
      this.projectTagResponses = []
      this.inputTag = ''
    }
  }
})
</script>

<style scoped lang="scss">
.popup-tag {

}
</style>
