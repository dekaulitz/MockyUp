<template>
  <div class="page-container">
    <div class="holder d-flex align-items-center">
      <h1 class="page-title">Projects</h1>
      <div class="page-controller ms-auto">
        <button class="btn btn-primary">Create New Project</button>
      </div>
    </div>
    <div class="holder d-flex align-items-center mb-3">
      <div>
        <ul class="nav nav-pills">
          <li class="nav-item">
            <a class="nav-link " aria-current="page" href="#">Your Projects
              <span class="badge bg-secondary">{{ projectCards.length }}</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled">Disabled</a>
          </li>
        </ul>
      </div>
      <div class="ms-auto d-inline-flex">
        <form-input-search class="me-2 flex-shrink-1"/>
        <dropdown-container>
          <dropdown-button id="sortBy" class="btn btn-outline-secondary">Sorting by
          </dropdown-button>
          <dropdown-content-container>
            <dropdown-content-item-container><content-item-link class="disabled">
              Sorty By</content-item-link>
            </dropdown-content-item-container>
            <dropdown-content-item-container><content-item-link >
              Something</content-item-link>
            </dropdown-content-item-container>
            <dropdown-content-item-container><content-item-link>
              Something else</content-item-link>
            </dropdown-content-item-container>
          </dropdown-content-container>
        </dropdown-container>
      </div>
    </div>
    <template v-if="showProjectPlaceHolder">
      <place-holder-container/>
    </template>
    <div v-for="(projectCard, index) in projectCards" :key="index" class="list-item">
      <project-card :project-card="projectCard" class="mb-2 bg-body rounded "/>
    </div>
    <pagination-container class="d-flex mt-3 justify-content-end"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ProjectService from '@/plugins/webclient/tmp/serice/ProjectService'
import ProjectCard from '@/components/cards/ProjectCard.vue'
import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import DropdownContainer from '@/shared/dropdown/DropdownContainer.vue'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import { ProjectCardInterface } from '@/plugins/webclient/model/ResponseModel'
import DropdownButton from '@/shared/dropdown/DropdownButton.vue'
import DropdownContentContainer from '@/shared/dropdown/DropdownContentContainer.vue'
import DropdownContentItemContainer from '@/shared/dropdown/DropdownContentItemContainer.vue'
import ContentItemLink from '@/shared/dropdown/ContentItemLink.vue'

export default defineComponent({
  name: 'ProjectCardList',
  components: {
    ContentItemLink,
    DropdownContentItemContainer,
    DropdownContentContainer,
    DropdownButton,
    FormInputSearch,
    DropdownContainer,
    PlaceHolderContainer,
    PaginationContainer,
    ProjectCard
  },
  data () {
    return {
      showProjectPlaceHolder: true,
      projectCards: [] as ProjectCardInterface[]
    }
  },

  mounted () {
    ProjectService.getProjects()
      .then(value => {
        if (value.data) {
          this.showProjectPlaceHolder = false
          this.projectCards = value.data
        }
      })
  }
})
</script>

<style scoped>

</style>
