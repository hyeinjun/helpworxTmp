import axios from "axios";
import router from "../router";
import store from "../store/index.js"

const instance=axios.create({
    baseURL: process.env.VUE_APP_API_URL
});

instance.interceptors.response.use(
  function(response) {
    if (response.data == "accessToken doesn't validate") {
      console.log(
        "axios interceptor : accessToken doesn't validate"
      );
      
      store.commit("usrInfo/clearInfo");
      store.commit("token/clearToken");
      
      setTimeout(router.push("/login"),2000);
      
    }
    return response;
  },
  function(error) {
    // 오류 응답을 처리
    // ...
    console.log("axios interceptor : " + error);
    return Promise.reject(error);
  }
);

export default instance;