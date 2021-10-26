<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Projects</h1>
      <div class="page-controller ms-auto" v-if="hasAccessPermissions('USERS_READ_WRITE')">
        <router-link class="btn btn-primary btn-md" :to="{
            name:'ProjectsCreate'}"><span class="fas fa-plus"/> New Project
        </router-link>
      </div>
    </div>
    <div class=" d-flex align-items-center mt-2">
      <div class="me-auto d-flex">
        <form-input-search class="me-2 flex-shrink-1 input-w-sm input-md"
                           v-model="parameter.projectName"/>
        <button class="btn btn-primary btn-md w-sm" @click="getDatas"><span class="fas fa-search"/>Search
        </button>
      </div>
      <div class="ms-auto d-inline-flex">
        <project-sorting-drop-down v-model="parameter.sort" @onChange:sort="getAllAndCount"/>
      </div>
    </div>
    <place-holder-container class="mt-2" v-if="placeHolderActive">
      <line-placeholder class="col-md-12"/>
      <line-placeholder class="col-md-12"/>
      <line-placeholder class="col-md-12"/>
    </place-holder-container>
    <table class="table table-hover mt-2" v-if="!placeHolderActive">
      <thead class="border-top">
      <tr>
        <th>Project Name</th>
        <th>Tags</th>
        <th>Updated Date</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(project,index) in data" :key="index">
        <td>{{ project.projectName }}</td>
        <td>{{ project.projectTags.join(',') }}</td>
        <td>{{ $filters.localDate(project.updatedDate) }}</td>
        <td>
          <div class="d-flex">
            <router-link class="btn btn-primary p-0 px-2 me-2" :to="{
            name:'ProjectsDetail',
            params:{id:project.id}}"
            ><span class="bi bi-book"></span></router-link>
            <button class="btn btn-danger p-0 px-2" @click="deleteById(project.id)"
                    v-if="hasAccessPermissions('PROJECTS_READ_WRITE')"><span
              class="bi bi-trash-fill"></span></button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <pagination-container class="d-flex mt-3 justify-content-end" v-model="parameter.page"
                          :paging-attributes="pagingAttributes" @click="getAllAndCount"/>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'

import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import { GetProjectParam, ProjectCardResponse } from '@/service/webclient/model/Projects'
import ProjectSortingDropDown from '@/components/sorting/ProjectsSortingDropDown.vue'
import BasePagingComponent from '@/shared/base/BasePagingComponent'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import { ProjectService } from '@/service/webclient/service/ProjectService'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BaseAccessMixins from '@/shared/base/BaseAccessMixins'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import LinePlaceholder from '@/shared/placeholder/LinePlaceholder.vue'

export default defineComponent({
  name: 'Projects',
  mixins: [BasePagingComponent, BreadhCrumbMixins, BaseAccessMixins],
  data () {
    return {
      service: ProjectService,
      data: [] as ProjectCardResponse[],
      parameter: {
        page: 1,
        size: 10,
        sort: 'id:desc'
      } as GetProjectParam,
      breadCrumbAttributes: [
        {
          label: 'Projects',
          routerLink: { name: 'Projects' },
          isActive: true
        }
      ]
    }
  },
  components: {
    LinePlaceholder,
    PlaceHolderContainer,
    BreadcrumbContainer,
    FormInputSearch,
    ProjectSortingDropDown,
    PaginationContainer,
    PageContainer
  },
  mounted () {
    this.getAllAndCount()
  },
  methods: {
    getDatas () {
      this.getAllAndCount()
    }
  }
})
</script>

<style scoped>

</style>
