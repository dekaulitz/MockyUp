<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Users</h1>
      <div class="page-controller ms-auto">
        <router-link class="btn btn-primary btn-md " :to="{name:'UsersCreate'}" v-if="hasAccessPermissions('USERS_READ_WRITE')"><span class="fas fa-plus"/> New User
        </router-link>
      </div>
    </div>
    <div class="d-flex align-items-center mt-2">
      <div class="me-auto d-inline-flex">
        <form-input-search class="me-2 flex-shrink-1 input-w-sm input-md" v-model="parameter.userNameOrEmail"/>
        <button class="btn btn-primary btn-md w-sm" @click="getAllAndCount()"><span class="fas fa-search"/>Search</button>
      </div>
      <div class="ms-auto d-inline-flex">
        <user-sorting-drop-down v-model="parameter.sort" @onChange:sort="getAllAndCount()"/>
      </div>
    </div>
    <place-holder-container class="mt-2" v-if="placeHolderActive">
      <line-placeholder class="col-md-12"/>
      <line-placeholder class="col-md-12"/>
      <line-placeholder class="col-md-12"/>
    </place-holder-container>
    <table class="table table-hover mt-2" v-if="!placeHolderActive">
      <thead>
      <tr>
        <th>Username</th>
        <th>Email</th>
        <th>Status</th>
        <th>Updated Date</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user,index) in data" :key="index">
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>{{ user.enabled ? 'Active' : 'Not Active' }}</td>
        <td>{{ user.updatedDate }}</td>
        <td>
          <div class="d-flex">
            <router-link class="btn btn-primary p-0 px-2 me-2" :to="{
            name:'UsersDetail',
            params:{id:user.id}}"
            ><span class="bi bi-book"></span></router-link>
            <button class="btn btn-danger p-0 px-2" @click="deleteById(user.id)" v-if="hasAccessPermissions('USERS_READ_WRITE')"><span class="bi bi-trash-fill"></span></button>
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
import BasePagingComponent from '@/shared/base/BasePagingComponent'
import { GetUsersParam, UserCardResponse } from '@/service/webclient/model/Users'
import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import UserSortingDropDown from '@/components/sorting/UserSortingDropDown.vue'
import { UserService } from '@/service/webclient/service/UserService'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'
import BaseAccessMixins from '@/shared/base/BaseAccessMixins'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import LinePlaceholder from '@/shared/placeholder/LinePlaceholder.vue'

export default defineComponent({
  name: 'Users',
  mixins: [BasePagingComponent, BreadhCrumbMixins, BaseAccessMixins],
  data () {
    return {
      service: UserService,
      data: [] as UserCardResponse[],
      parameter: {
        page: 1,
        size: 10,
        sort: 'id:desc',
        userNameOrEmail: ''
      } as GetUsersParam
    }
  },
  components: {
    LinePlaceholder,
    PlaceHolderContainer,
    BreadcrumbContainer,
    UserSortingDropDown,
    FormInputSearch,
    PaginationContainer,
    PageContainer
  },
  mounted () {
    this.getAllAndCount()
    this.breadCrumbAttributes = [
      {
        label: 'Users',
        routerLink: {
          name: 'Users'
        },
        isActive: true
      }
    ]
  }
})
</script>

<style scoped>

</style>
