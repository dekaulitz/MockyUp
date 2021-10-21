<template>
  <div>
    <div class="d-flex align-items-center holder mt-1">
      <h3 class="page-title">Projects</h3>
      <div class="page-controller ms-auto">
        <router-link class="btn btn-primary btn-md" :to="{
            name:'ProjectsCreate'}"><span class="fas fa-plus"/>New Project
        </router-link>
      </div>
    </div>
    <div class="holder d-flex align-items-center mb-3">
      <div>
        <ul class="nav nav-tabs tab-list" id="myTab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="home-tab" data-bs-toggle="tab"
                    data-bs-target="#yourProjects" type="button" role="tab" aria-controls="home"
                    aria-selected="true">Your Projects <span
              class="badge bg-secondary">{{ pagingAttributes.totalData }}</span>
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="profile-tab" data-bs-toggle="tab"
                    data-bs-target="#allProjects"
                    type="button" role="tab" aria-controls="allProjects" aria-selected="false">
              Profile
            </button>
          </li>
        </ul>
      </div>
      <div class="ms-auto d-flex">
        <form-input-search class="me-2 flex-shrink-1 input-w-sm input-md"
                           v-model="parameter.projectName"/>
        <button class="btn btn-primary btn-md" @click="searching"><span class="fas fa-search"/>Search
        </button>
        <project-sorting-drop-down class="ms-2" v-model="parameter.sort"
                                   @onChange:sort="getAllAndCount"/>
      </div>
    </div>
    <div class="tab-content" id="myTabContent">
      <div class="tab-pane fade show active" id="yourProjects" role="tabpanel"
           aria-labelledby="home-tab">
        <project-card v-for="(project,index) in values" :key="index" class="mb-2"
                      :project-card="project"></project-card>
      </div>
      <div class="tab-pane fade" id="allProjects" role="tabpanel"
           aria-labelledby="home-tab">
        {{ values }}
      </div>
    </div>
    <pagination-container class="d-flex mt-3 justify-content-end" v-model="parameter.page"
                          :paging-attributes="pagingAttributes" @click="getAllAndCount"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BasePagingComponent from '@/shared/base/BasePagingComponent'
import { GetProjectParam } from '@/service/webclient/model/Projects'
import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import { ProjectService } from '@/service/webclient/service/ProjectService'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import ProjectSortingDropDown from '@/components/sorting/ProjectsSortingDropDown.vue'
import ProjectCard from '@/components/cards/ProjectCard.vue'

export default defineComponent({
  name: 'ProjectDashboard',
  mixins: [BasePagingComponent],
  data () {
    return {
      parameter: {
        page: 1,
        size: 10,
        sort: 'id:desc'
      } as GetProjectParam,
      service: ProjectService
    }
  },
  mounted () {
    this.getAll()
    this.getCount()
  },
  methods: {
    searching () {
      this.parameter.page = 1
      this.getAllAndCount()
    }
  },
  components: {
    ProjectCard,
    ProjectSortingDropDown,
    FormInputSearch,
    PaginationContainer
  }
})
</script>
<style scoped lang="scss">
</style>
