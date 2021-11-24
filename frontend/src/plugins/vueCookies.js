import Vue from "vue";
import VueCookies from "vue-cookies";

Vue.use(VueCookies);

Vue.$cookies.config("14d");

const cookies=require('vue-cookies');
export default cookies;