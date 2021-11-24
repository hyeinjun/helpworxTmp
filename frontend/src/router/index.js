import Vue from "vue";
import VueRouter from "vue-router";
import CustMain from "../Views/common/CustMain.vue";
import SrDetail from "../Views/customer/SrDetail.vue";
import ManagerMain from "../Views/common/ManagerMain.vue";
import LogIn from "../Views/common/LogIn.vue";
import SrModify from "../Views/customer/SrModify.vue";
import SrRegist from "../Views/customer/SrRegist.vue";
import CustSrList from "../Views/customer/CustSrList.vue";
import CoList from "../Views/manager/CoListPage.vue";
import CoDetail from "../Views/manager/CoDetailPage.vue";
import CoModify from "../Views/manager/CoModifyPage.vue";
import CoInsert from "../Views/manager/CoInsertPage.vue";
import InquList from "../Views/operator/InquList.vue";
import OperSrList from "../Views/operator/OperSrList.vue";
import CustList from "../Views/manager/CustListPage.vue";
import CustDetail from "../Views/manager/CustDetailPage.vue";
import CustModify from "../Views/manager/CustModifyPage.vue";
import CustInsert from "../Views/manager/CustInsertPage.vue";
import OperSrRcptTreat from "../Views/operator/SrRcptTreat.vue";
import SrSystemChart from "../Views/operator/SrSystemChart.vue";
import SrStatusChart from "../Views/operator/SrStatusChart.vue";
import CustInquList from "../Views/customer/CustInquList.vue";
import OperMain from "../Views/common/OperMain.vue";
import CustInfoList from "../Views/customer/CustInfoListPage.vue";
import CustInfoDetail from "../Views/customer/CustInfoDetailPage.vue";
import CustInfoModify from "../Views/customer/CustInfoModifyPage.vue";
import CustInfoRegist from "../Views/customer/CustInfoRegistPage.vue";
import MngInfoList from "../Views/manager/MngInfoListPage.vue";
import MngInfoDetail from "../Views/manager/MngInfoDetailPage.vue";
import API from "../api/API.js";

import store from "../store/index.js";

Vue.use(VueRouter);

//고객 권한 검사
var checkCustAuth = function(to, from, next) {
  console.log(store.state.usrInfo.usrAuth);
  if (store.state.usrInfo.usrAuth == true && changeCoCd()) {
    next();
  } else {
    next(returnUrl());
  }
};

//운영자 권한 검사
var checkOperAuth = function(to, from, next) {
  console.log(store.state.usrInfo.operatorAuth);
  if (store.state.usrInfo.operatorAuth == true && changeCoCd()) {
    next();
  } else {
    next(returnUrl());
  }
};

//관리자 권한 검사
var checkMngAuth = function(to, from, next) {
  console.log(store.state.usrInfo.managerAuth);
  if (store.state.usrInfo.managerAuth == true) {
    next();
  } else {
    next(returnUrl());
  }
};

//권한에 맞는 메인으로 돌려보낸다.
var returnUrl = function() {
  console.log(store.state.usrInfo.menuAuth);
  if (store.state.usrInfo.menuAuth == "user") {
    return "/customer/main";
  } else if (store.state.usrInfo.menuAuth == "operator") {
    return "/operator/main";
  } else if (store.state.usrInfo.menuAuth == "manager") {
    return "/manager/main";
  } else {
    return "/login";
  }
};

//매니저일 경우 본인 회사로 바꾼다.
var changeCoCd = function() {
  if (store.state.usrInfo.managerAuth == true) {
    API.getCoCd(
      "",
      (res) => {
        store.commit("usrInfo/changeCo", {
          coCd: res,
          coNm: store.state.CommonData.companyList.find((e) => e.value == res).text,
        });
        return true;
      },
      (err) => {
        console.log(err);
        return false;
      }
    );
  }
  return true;
};

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/customer/main",
    name: "custMain",
    component: CustMain,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/sr/detail/:srId",
    name: "srDetail",
    component: SrDetail,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/operator/main",
    name: "operMain",
    component: OperMain,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/manager/main",
    name: "ManagerMain",
    component: ManagerMain,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/login",
    name: "LogIn",
    component: LogIn,
  },
  {
    path: "/manager/co/list",
    name: "CoListPage",
    component: CoList,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/customer/sr/modify/:srId",
    name: "srModify",
    component: SrModify,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/sr/regist",
    name: "srRegist",
    component: SrRegist,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/sr/list",
    name: "custSrList",
    component: CustSrList,
    beforeEnter: checkCustAuth,
  },

  {
    path: "/manager/co/modify/:coCd",
    name: "coModify",
    component: CoModify,
    beforeEnter: checkMngAuth,
  },

  {
    path: "/manager/co/detail/:coCd",
    name: "coDetail",
    component: CoDetail,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/operator/srlist",
    name: "operSrList",
    component: OperSrList,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/manager/co/insert",
    name: "coInsert",
    component: CoInsert,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/operator/inqu/list",
    name: "inquList",
    component: InquList,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/manager/cust/insert",
    name: "custInsert",
    component: CustInsert,
    beforeEnter: checkMngAuth,
  },

  {
    path: "/manager/cust/list",
    name: "custList",
    component: CustList,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/manager/cust/detail/:usrId",
    name: "custDetail",
    component: CustDetail,
    beforeEnter: checkMngAuth,
  },

  {
    path: "/manager/cust/modify/:usrId",
    name: "custModify",
    component: CustModify,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/operator/sr/rcpt-treat/:srId",
    name: "operSrRcptTreat",
    component: OperSrRcptTreat,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/operator/sr/systemchart",
    name: "srSystemChart",
    component: SrSystemChart,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/operator/sr/status",
    name: "srStatusChart",
    component: SrStatusChart,
    beforeEnter: checkOperAuth,
  },
  {
    path: "/customer/inqu/list",
    name: "custInquList",
    component: CustInquList,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/info/list",
    name: "custInfoModReqList",
    component: CustInfoList,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/manager/info/list",
    name: "mngInfoModReqList",
    component: MngInfoList,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/manager/info/detail/:reqId",
    name: "mngInfoModReqDetail",
    component: MngInfoDetail,
    beforeEnter: checkMngAuth,
  },
  {
    path: "/customer/info/modify/:reqId",
    name: "custInfoModReqModify",
    component: CustInfoModify,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/info/regist",
    name: "custInfoModReqRegist",
    component: CustInfoRegist,
    beforeEnter: checkCustAuth,
  },
  {
    path: "/customer/info/detail/:reqId",
    name: "custInfoModReqDetail",
    component: CustInfoDetail,
    beforeEnter: checkCustAuth,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
