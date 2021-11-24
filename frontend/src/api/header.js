import vuex from '../store/index.js'

export default function makeheader() {
    return {
      headers: {
        "Content-type": "application/json",
        "accessToken": "Bearer "+vuex.state.token.accessToken,
        "Access-Control-Allow-Origin": "*",
      }
    }
  }