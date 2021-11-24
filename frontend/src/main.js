import Vue from 'vue'
import App from './App.vue'
import vuetify from '@/plugins/vuetify'
import router from './router'
import axios from 'axios'
import store from './store'
import cookies from '@/plugins/vueCookies'
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import 'codemirror/lib/codemirror.css'
import {Editor, Viewer} from '@toast-ui/vue-editor'

Vue.config.productionTip = false
Vue.prototype.$http = axios

Vue.component('editor',Editor)
Vue.component('viewer',Viewer)

new Vue({
  vuetify,
  router,
  store,
  cookies,
  render: h => h(App)
}).$mount('#app')
