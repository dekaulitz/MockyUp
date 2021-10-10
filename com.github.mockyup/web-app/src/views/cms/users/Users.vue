<template>
  <page-container>
    <div class="d-flex align-items-center">
      <h1 class="page-title">Users</h1>
      <div class="page-controller ms-auto">
        <button class="btn btn-primary">Create New User</button>
      </div>
    </div>
    <div class="holder d-flex align-items-center mb-3">
      <div class="me-auto d-inline-flex">
        <form-input-search class="me-2 flex-shrink-1" v-model="parameter.userNameOrEmail"/>
        <button class="btn btn-info" @click="getAllAndCount">Search</button>
      </div>
      <div class="ms-auto d-inline-flex">
        <user-sorting-drop-down v-model="parameter.sort" @onChange:sort="getAllAndCount"/>
      </div>
    </div>
    <table class="table table-striped">
      <thead>
      <tr>
        <th>Username</th>
        <th>Email</th>
        <th>Status</th>
        <th>Updated Date</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user,index) in values" :key="index">
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>{{ user.enabled?'active':'not active'}}</td>
        <td>{{ user.updatedDate }}</td>
      </tr>
      </tbody>
    </table>
    <pagination-container class="d-flex mt-3 justify-content-end" v-model="parameter.page" :paging-attributes="pagingAttributes" @click="getAllAndCount"/>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'
import BasePagingComponent from '@/components/base/BasePagingComponent'
import { UserService } from '@/plugins/webclient/service/CmsService'
import { GetUsersParam, UserCardsResponse } from '@/plugins/webclient/model/Users'
import PaginationContainer from '@/shared/pagination/PaginationContainer.vue'
import FormInputSearch from '@/shared/form/FormInputSearch.vue'
import UserSortingDropDown from '@/components/sorting/UserSortingDropDown.vue'

export default defineComponent({
  name: 'Users',
  mixins: [BasePagingComponent],
  data () {
    return {
      service: UserService,
      values: []as UserCardsResponse[],
      parameter: {
        page: 1,
        size: 10,
        sort: 'id:desc',
        userNameOrEmail: ''
      } as GetUsersParam
    }
  },
  components: { UserSortingDropDown, FormInputSearch, PaginationContainer, PageContainer },
  mounted () {
    this.getAll()
    this.getCount()
  }
})
</script>

<style scoped>

</style>
