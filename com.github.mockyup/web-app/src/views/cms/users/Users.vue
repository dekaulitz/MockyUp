<template>
  <page-container>
    <div class="d-flex align-items-center holder mt-2">
      <h1 class="page-title">Users</h1>
      <div class="page-controller ms-auto">
        <router-link class="btn btn-primary" :to="{name:'UsersCreate'}">Create New User
        </router-link>
      </div>
    </div>
    <div class="holder d-flex align-items-center mb-3">
      <div class="me-auto d-inline-flex">
        <form-input-search class="me-2 flex-shrink-1 input-sm" v-model="parameter.userNameOrEmail"/>
        <button class="btn btn-primary btn-md w-sm" @click="searching">Search</button>
      </div>
      <div class="ms-auto d-inline-flex">
        <user-sorting-drop-down v-model="parameter.sort" @onChange:sort="getAllAndCount"/>
      </div>
    </div>
    <table class="table table-hover mt-2">
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
      <tr v-for="(user,index) in values" :key="index">
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
            <button class="btn btn-danger p-0 px-2"><span class="bi bi-trash-fill"></span></button>
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
import { GetUsersParam, UserCardsResponse } from '@/service/webclient/model/Users'
import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import UserSortingDropDown from '@/components/sorting/UserSortingDropDown.vue'
import { UserService } from '@/service/webclient/service/UserService'

export default defineComponent({
  name: 'Users',
  mixins: [BasePagingComponent],
  data () {
    return {
      service: UserService,
      values: [] as UserCardsResponse[],
      parameter: {
        page: 1,
        size: 10,
        sort: 'id:desc',
        userNameOrEmail: ''
      } as GetUsersParam
    }
  },
  components: {
    UserSortingDropDown,
    FormInputSearch,
    PaginationContainer,
    PageContainer
  },
  mounted () {
    this.getAll()
    this.getCount()
  },
  methods: {
    searching () {
      this.getAllAndCount()
    }
  }
})
</script>

<style scoped>

</style>
