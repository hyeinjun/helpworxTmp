<template>
    <v-app>
      <v-main class="ma-3 login_card">
        <v-container fluid>
          <v-card :elevation="2" class="pa-10 login" height="350" width="600">
              <v-row no-gutters dense justify="center" class="mb-2"
                ><v-col sm="10">
                  <v-text-field
                    label="ID"
                    hide-details="auto"
                    v-model="ID"
                    :rules="[rules.required]"
                    @keyup.enter="loginSubmit"
                  ></v-text-field> </v-col></v-row
              ><v-row no-gutters dense justify="center" class="mb-10"
                ><v-col sm="10">
                  <v-text-field
                    label="PW"
                    v-model="PW"
                    :rules="[rules.required,rules.pwdLength]"
                    type="password"
                    hide-details="auto"
                    @keyup.enter="loginSubmit"
                  ></v-text-field> </v-col
              ></v-row>
              <v-radio-group v-model="menuAuth">
                <v-row no-gutters dense class="ml-9 mb-5">
                  <v-col cols="12" md="4">
                    <v-radio label="고객 로그인" value="user"></v-radio> </v-col
                  ><v-col cols="12" md="4">
                    <v-radio
                      label="운영자 로그인"
                      value="operator"
                    ></v-radio> </v-col
                  ><v-col>
                    <v-radio label="관리자 로그인" value="manager"></v-radio>
                  </v-col>
                </v-row>
              </v-radio-group>
              <v-row no-gutters dense justify="center">
                <v-btn dark @click="loginSubmit" @keyup.enter="loginSubmit">
                  Login
                </v-btn>
              </v-row>
              <v-dialog v-model="selectCoModal" width="500">
                <select-co-modal @modalValue="selectModalValue" />
              </v-dialog>
              <alert-dialog
                v-if="logInFailModal"
                dialogTitle="로그인에 실패하였습니다."
                dialogText="아이디, 비밀번호를 확인해주세요."
                @close="loginModalValue"
              ></alert-dialog>
              <alert-dialog
                v-if="authCheckFailModal"
                dialogTitle="로그인에 실패하였습니다."
                dialogText="권한이 없습니다."
                @close="authCheckModalValue"
              ></alert-dialog>
          </v-card>
        </v-container>
      </v-main>
    </v-app>
</template>

<script>
import API from "@/api/API";
import SelectCoModal from "../../components/login/SelectCoModal.vue";
import AlertDialog from "../../components/common/AlertDialog.vue"

export default {
  components: {
    SelectCoModal,
    AlertDialog
  },
  data() {
    return {
      operatorOrManagerYn: false,
      ID: "",
      PW: "",
      selectCoModal: false,
      modal: false,
      logInFailModal: false,
      authCheckFailModal: false,
      menuAuth: "user",
      rules: {
        required: (value) => !!value || "반드시 입력이 필요한 항목입니다.",
        pwdLength: (value) =>
          (value.length > 7 && value.length < 17) ||
          "패스워드는 8자 이상 16자 이하여야 합니다.",
      },
    };
  },
  methods: {
    openCheckCoModal() {
      this.selectCoModal = true;
    },
    openLoginFailModal() {
      this.logInFailModal = true;
    },
    openAuthCheckFailModal() {
      this.authCheckFailModal = true;
    },
    loginSubmit() {
      console.log("로그인");
      console.log(this.ID);

      let data = {};

      data.usrId = this.ID;
      data.pwd = this.PW;

      if (this.$cookies.isKey("refreshToken")) {
        data.refreshToken = this.$cookies.get("refreshToken");
      } else {
        data.refreshToken = null;
      }

      API.postLogInInfo(
        data,
        (res) => {
          console.log(res);
          console.log("success");

          this.$store.commit("token/setToken", {
            accessToken: res.accessToken,
          });
          this.$cookies.set("refreshToken", res.refreshToken);
          console.log(this.$store.state.token.accessToken);
          this.getMenus();
        },
        (err) => {
          console.log(err);
          this.openLoginFailModal();
          console.log(this.loginFailModal);
        }
      );
    },

    checkInfoAndAuth() {
      console.log("사용자 정보 가져오기");

      API.getAuth(
        "",
        (res) => {
          console.log(res.usrId);

          if (this.menuAuth == "manager") {
            if (res.managerAuth) {
              this.setInfo(res);
              this.openCheckCoModal();
            } else {
              this.openAuthCheckFailModal();
            }
          } else if (this.menuAuth == "operator") {
            if (res.operatorAuth) {
              this.setInfo(res);
              this.$store.commit("usrInfo/loginCnt");
              this.$router.push("/operator/main");
            } else {
              this.openAuthCheckFailModal();
            }
          } else {
            if (this.menuAuth == "user") {
              this.setInfo(res);
              this.$store.commit("usrInfo/loginCnt");
              this.$router.push("/customer/main");
            } else {
              this.openAuthCheckFailModal();
            }
          }
        },
        (err) => {
          console.log(err);
          this.openLoginFailModal();
        }
      );
    },
    setInfo(res) {
      this.$store.commit("usrInfo/setUsrInfo", {
        usrId: res.usrId,
        coCd: res.coCd,
        coNm: res.coNm,
        usrNm: res.nm,
        usrAuth: res.usrAuth,
        operatorAuth: res.operatorAuth,
        managerAuth: res.managerAuth,
        menuAuth: this.menuAuth,
      });
    },
    setMenu(res) {
      this.$store.commit("usrInfo/setMenus", {
        menus: res,
      });
      console.log(this.$store.state.usrInfo.menus);
    },
    loginModalValue(value) {
      console.log(value);
      this.logInFailModal = value;
    },
    authCheckModalValue(value) {
      console.log(value);
      this.authCheckFailModal = value;
    },
    selectModalValue(value) {
      this.selectCoModal = value;
    },
    getMenus() {
      API.getMenusByAuth(
        "auth=" + this.menuAuth,
        (res) => {
          this.setMenu(res);
          this.checkInfoAndAuth();
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
};
</script>

<style>
.login_card {
  align-self: center;
  position: relative;
}

.login{
  top: 130px;
}
</style>
