<template>
  <dropdown-container>
    <dropdown-button id="sortBy" class="btn btn-outline-secondary btn-md w-md">{{ defaultSorting }}
    </dropdown-button>
    <dropdown-content-container>
      <dropdown-content-item-container v-for="(attribute,index) in sortingAttributes" :key="index">
        <button class="dropdown-item"
                v-on:click="$emit('update:modelValue', attribute.value),replacingHeaderSort(attribute.fieldName)"
        >
        <div class="d-flex inline-flex">
          {{attribute.fieldName}}
          <span class="bi bi-check ms-auto" v-if="defaultSorting === attribute.fieldName"></span>
        </div>
        </button>
      </dropdown-content-item-container>
    </dropdown-content-container>
  </dropdown-container>
</template>

<script lang="ts">
import DropdownContainer from '../../shared/dropdown/DropdownContainer.vue'
import { defineComponent } from 'vue'
import DropdownButton from '@/shared/dropdown/DropdownButton.vue'
import DropdownContentContainer from '@/shared/dropdown/DropdownContentContainer.vue'
import DropdownContentItemContainer from '@/shared/dropdown/DropdownContentItemContainer.vue'

export default defineComponent({
  name: 'ProjectSortingDropDown',
  emits: ['onChange'],
  data: function () {
    return {
      defaultSorting: 'Sorting By',
      sortingAttributes: [
        {
          fieldName: 'New Project',
          value: 'updatedDate:desc'
        },
        {
          fieldName: 'Old Project',
          value: 'updatedDate:asc'
        }
      ]
    }
  },
  components: {
    DropdownContentItemContainer,
    DropdownContentContainer,
    DropdownButton,
    DropdownContainer
  },
  methods: {
    replacingHeaderSort (value: string) {
      this.defaultSorting = value
      this.$emit('onChange:sort', value)
    }
  }
})
</script>

<style scoped>

</style>
