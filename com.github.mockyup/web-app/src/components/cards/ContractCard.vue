<template>
  <card-container class="project-card">
    <card-body class="d-flex align-items-center">
      <div class="avatar avatar-sm flex-shrink-0">
        <span>{{$filters.subString(contractCard.info.title,0,1)}}</span>
      </div>
      <div class="flex-row ms-2 project-card-description">
        <div>
          <router-link :to="{
            name:'ContractDetail',
            params:{id:contractCard.projectId,contractId:contractCard.id}}">
            <span v-if="contractCard.info.contact.name">
              {{contractCard.info.contact.name}} /
            </span>
            {{contractCard.info.title}}</router-link>
          <span class="fas fa-lock ms-1" v-if="contractCard.private"/>
        </div>
        <span class="card-subtitle">{{contractCard.info.description}}</span>
      </div>
      <div class="ms-auto">
       Last update {{ $filters.localDate(contractCard.updatedDate) }}
      </div>
    </card-body>
  </card-container>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import {
  ContractCardInterface
} from '@/plugins/webclient/model/ResponseModel'

export default defineComponent({
  name: 'ContractCard',
  components: {
    CardBody,
    CardContainer
  },
  props: {
    contractCard: { type: Object as PropType<ContractCardInterface> }
  },
  methods: {
    popUpCard () {
      alert('berak')
    }
  }
})
</script>

<style scoped lang="scss">
</style>
