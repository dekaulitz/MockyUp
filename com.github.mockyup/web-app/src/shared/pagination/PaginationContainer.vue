<template>
  <nav aria-label="Page navigation">
    <ul class="pagination">
      <li class="page-item">
        <button class="page-link btn-md" aria-label="Previous" v-on:click="$emit('update:modelValue',
         setCurrentPage(currentPage-1))">
          <span aria-hidden="true">&laquo;</span>
        </button>
      </li>
      <li class="page-item" v-for="(value) in paging" :key="value"
          :class="{'active':value===currentPage}">
        <template v-if="value  === '...'">
          <span class="page-link">
            {{ value }}
          </span>
        </template>
        <template v-else>
          <button class="page-link active btn-md"
                  v-on:click="$emit('update:modelValue',setCurrentPage(value))">
            {{ value }}
          </button>
        </template>
      </li>
      <li class="page-item">
        <button class="page-link btn-md" aria-label="Next" v-on:click="$emit('update:modelValue',
         setCurrentPage(currentPage+1))">
          <span aria-hidden="true">&raquo;</span>
        </button>
      </li>
    </ul>
  </nav>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { PagingAttributes } from '@/shared/pagination/index'

export default defineComponent({
  name: 'PaginationContainer',
  data () {
    return {
      // maximumPageButton: {},
      currentPage: 1,
      nextPage: 1,
      previousPage: 1,
      firstPage: 1,
      lastPage: 5
    }
  },
  props: {
    pagingAttributes: {
      type: Object as PropType<PagingAttributes>,
      default: function (): PagingAttributes {
        return {
          pageSize: 10,
          totalData: 0
        }
      }
    }
  },
  methods: {
    setCurrentPage (currentPage: number): number {
      const maximumPage = this.maximumPageNumber()
      const minimumPage = 1
      this.nextPage = currentPage > maximumPage ? maximumPage : currentPage
      this.previousPage = currentPage < 0 ? 1 : currentPage
      if (currentPage >= maximumPage) {
        this.currentPage = maximumPage
      } else if (currentPage < 1) {
        this.currentPage = minimumPage
      } else {
        this.currentPage = currentPage
      }
      return this.currentPage
    },
    maximumPageNumber (): number {
      const pagingAttribute = this.pagingAttributes
      return Math.ceil(pagingAttribute.totalData / pagingAttribute.pageSize)
    },
    pagination (currentPage, nrOfPages): (string | number)[] {
      const delta = 2
      const range: number[] = []
      const rangeWithDots: (string | number)[] = []
      let l: number
      range.push(1)
      if (nrOfPages <= 1) {
        return range
      }
      for (let i: any = currentPage - delta; i <= currentPage + delta; i++) {
        if (i < nrOfPages && i > 1) {
          range.push(i)
        }
      }
      range.push(nrOfPages)
      for (const something of range) {
        if (l) {
          if (something - l === 2) {
            rangeWithDots.push(l + 1)
          } else if (something - l !== 1) {
            rangeWithDots.push('...')
          }
        }
        rangeWithDots.push(something)
        l = something
      }

      return rangeWithDots
    }
  },
  computed: {
    paging (): (string | number)[] {
      const maximumPageNumber = this.maximumPageNumber()
      return this.pagination(this.currentPage, maximumPageNumber)
    }
  }
})
</script>

<style scoped>

</style>
