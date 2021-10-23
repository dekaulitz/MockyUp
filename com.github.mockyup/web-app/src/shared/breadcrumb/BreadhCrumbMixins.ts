export interface BreadCrumbAttribute {
  label?: string
  routerLink?: any
  description?: string,
  isActive: boolean
}

export default {
  emits: [],
  data () {
    return {
      breadCrumbAttributes: [] as BreadCrumbAttribute[]
    }
  }
}
