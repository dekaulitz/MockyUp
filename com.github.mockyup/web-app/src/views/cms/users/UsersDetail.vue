<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center">
      <div class="avatar avatar-md flex-shrink-0 me-3">
        <h1>{{ $filters.subString(data.username, 0, 1) }}</h1>
      </div>
      <div>
        <h1 class="page-title mb-1">@{{ data.username }}</h1>
        <div class="text-secondary additional-info">Email : {{ data.email }}</div>
      </div>
      <div class="ms-auto">
        <router-link :to="{name:'UsersEdit', params:{id:$route.params.id}}"
                     class="btn btn-primary btn-sm w-sm"><span class="fas fa-edit"></span> Edit
        </router-link>
      </div>
    </div>
    <card-container class="mt-3">
      <card-body>
        <div class="row">
          <div class="col-md-6">
            <h3 class="holder">Personal Information</h3>
            <table>
              <tr>
                <td class="label-bold ">Username</td>
                <td>:</td>
                <td>{{ data.username }}</td>
              </tr>
              <tr>
                <td class="label-bold ">Email</td>
                <td>:</td>
                <td>{{ data.email }}</td>
              </tr>
              <tr>
                <td class="label-bold">Status</td>
                <td>:</td>
                <td>{{ data.enabled ? 'Active' : 'Not Active' }}</td>
              </tr>
              <tr>
                <td class="label-bold ">Locked</td>
                <td>:</td>
                <td>{{ data.accountNonLocked ? 'Unlocked' : 'Locked' }}</td>
              </tr>
              <tr>
                <td class="label-bold ">Last Modified</td>
                <td>:</td>
                <td>{{ data.updatedDate}}</td>
              </tr>
            </table>
          </div>
          <div class="col-md-6">
            <h3 class="holder">Access Information</h3>
            <ul>
              <li v-for="(access,index) in accessList" :key="index"
                  :title="access.description">{{ access.label }}
              </li>
            </ul>
          </div>
        </div>
      </card-body>
    </card-container>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PageContainer from '@/pages/PageContainer.vue'
import { UserService } from '@/service/webclient/service/UserService'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import { UserDetailResponse } from '@/service/webclient/model/Users'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import { AccessData, AccessInterface } from '@/service/helper/AccessHelper'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'UsersCreate',
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      service: UserService,
      data: {} as UserDetailResponse,
      accessList: [] as AccessInterface[]
    }
  },
  components: {
    BreadcrumbContainer,
    CardBody,
    CardContainer,
    PageContainer
  },
  async mounted () {
    await this.getByDetail(this.$route.params.id)
    this.breadCrumbAttributes = [
      {
        label: 'Users',
        routerLink: {
          name: 'Users'
        },
        isActive: false
      },
      {
        label: 'User Detail',
        routerLink: {
          name: 'UsersDetail',
          id: this.data.id
        },
        isActive: false
      }
    ]
    const accessData = AccessData
    this.data.access.forEach(access => {
      if (accessData.has(access)) {
        this.accessList.push(accessData.get(access))
      }
    })
  }
})
</script>

<style scoped lang="scss">
</style>
