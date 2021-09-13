import axios from "axios";

declare module '@vue/runtime-core' {
  // provide typings for `this.$store`


  interface ComponentCustomProperties {
    $http: typeof axios
  }
}
