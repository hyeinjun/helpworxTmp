import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import token from "./modules/token"
import usrInfo from "./modules/usrInfo"
import CommonData from './modules/commonData.js'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    token,
    usrInfo,
    CommonData
  },
  plugins: [createPersistedState({
    // paths에 추가한 모듈만 저장됨
    paths:['CommonData','usrInfo','token'],
  })]
})
