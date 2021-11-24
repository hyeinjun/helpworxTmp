import Vue from "vue";

// state
const state = () => ({
  usrId: [],
  usrNm: [],
  coCd: [],
  coNm: [],
  usrAuth: false,
  operatorAuth: false,
  managerAuth: false,
  menuAuth: [],
  menus: [],
  cnt: 0,
});

// getters
const getters = {};

const mutations = {
  setUsrInfo(state, payload) {
    state.usrId = payload.usrId;
    state.usrNm = payload.usrNm;
    state.coCd = payload.coCd;
    state.coNm = payload.coNm;
    state.usrAuth = payload.usrAuth;
    state.operatorAuth = payload.operatorAuth;
    state.managerAuth = payload.managerAuth;
    state.menuAuth = payload.menuAuth;
  },
  changeCo(state, payload) {
    state.coCd = payload.coCd;
    state.coNm = payload.coNm;
  },
  changeMenu(state, payload) {
    state.menuAuth = payload.menuAuth;
  },
  changeMenus(state,payload){
    state.menus=payload.menus;
  },
  clearInfo(state) {
    state.coCd = [];
    state.coNm = [];
    state.usrId = [];
    state.usrNm = [];
    state.menus=[];
    state.cnt=0;
    state.usrAuth = false;
    state.operatorAuth = false;
    state.managerAuth = false;
    state.menuAuth = [];
  },
  setMenus(state, payload) {
    state.menus = payload.menus;
  },
  loginCnt(state) {
    state.cnt++;
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
};
