// state
const state = () => ({
  accessToken: [],
});

// getters
const getters = {};

const mutations = {
  setToken(state, payload) {
    state.accessToken = payload.accessToken;
  },
  clearToken(state){
    state.accessToken=[];
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations
};
