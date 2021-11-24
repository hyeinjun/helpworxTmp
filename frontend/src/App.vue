<template>
  <div id="app">
    <v-app>
      <Header></Header>
      <router-view></router-view>
    </v-app>

  </div>
</template>

<script>
import Header from './components/common/Header.vue'
export default {
  name: 'App',
  components: {
    Header,
  },
  data() {
    return{

    }
  },
  created() {
    // sys 와 jobGb info를 vuex에 저장하는 mutation
    this.$store.commit('CommonData/setSystemName');
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: aliceblue;
}
.textViewer {
  height: 300px; 
  border: 1px solid #EEEEEE; 
  border-radius: 5px; 
  overflow-y:auto;
}
.v-subheader {
  font-weight: bold !important;
  font-size: 1.07em !important;
}
.theme--light.v-breadcrumbs .v-breadcrumbs__item--disabled {
  color: rgba(0, 0, 0, 0.80) !important;
}
</style>
