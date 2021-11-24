<template>
  <v-card>
    <v-app-bar color="rgb(42, 46, 72)" dark app class="zIndex" rounded="0">
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title style="cursor:pointer" @click="goMain"
        >HelpWorX</v-toolbar-title
      >

      <v-spacer></v-spacer>

      <!-- 고객의 실시간 문의 버튼 -->
      <v-dialog
        v-if="auth == 'user'"
        v-model="dialog_lobby"
        persistent
        width="400px"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-badge
            v-model="isInquBadgeShow"
            overlap
            :content="chatListCnt"
            offset-x="22"
            offset-y="22"
            color="red"
          >
            <v-btn dark v-bind="attrs" v-on="on"  icon justify="space-around">
              <v-icon dark>
                mdi-comment-question
              </v-icon>
            </v-btn></v-badge
          >
        </template>

        <!-- 실시간 문의 호출 -->
        <rltm-inqu-lobby-modal
          :chatList="chatList"
          @changeDialogInqu="changeDialogInqu"
        ></rltm-inqu-lobby-modal>
      </v-dialog>

      <v-dialog v-model="dialog_start" persistent width="400px">
        <rltm-inqu-start-modal
          :resetSysInfo="resetSysInfo"
          @changeDialogStart="changeDialogStart"
        ></rltm-inqu-start-modal>
      </v-dialog>

      <v-dialog v-model="dialog_title" persistent width="400px">
        <rltm-inqu-input-title-modal
          :sysCd="selectedSysCd"
          :dialogFlg="dialogFlg"
          @changeDialogTitle="changeDialogTitle"
        ></rltm-inqu-input-title-modal>
      </v-dialog>

      <v-dialog v-model="dialog_chat" persistent width="400px">
        <rltm-inqu-chat-modal
          :inquId="inquId"
          :sysCd="selectedSysCd"
          @changeDialogChat="changeDialogChat"
        ></rltm-inqu-chat-modal>
      </v-dialog>

      <!-- 운영자 실시간 문의 버튼 -->
      <v-dialog
        v-if="auth == 'operator'"
        v-model="dialog_oper"
        persistent
        width="400px"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-badge
            v-model="isInquBadgeShow"
            overlap
            :content="totalOperInquSize"
            offset-x="22"
            offset-y="22"
            color="red"
          >
            <v-btn dark v-bind="attrs" v-on="on" icon justify="space-around">
              <v-icon dark>
                mdi-comment-question
              </v-icon>
            </v-btn>
          </v-badge>
        </template>

        <!-- 실시간 문의 호출 -->
        <oper-lobby-modal
          :chatList="operChatList"
          :newChatList="newChatList"
          :newCnt="newChatList.length"
          :chatListCnt="operInquSize"
          @changeDialogOper="changeDialogOper"
        ></oper-lobby-modal>
      </v-dialog>

      <!-- 알람 버튼 -->
      <v-menu
        class="justify-center"
        bottom
        left
        offset-y
        :nudge-width="300"
        v-if="auth == 'user'"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-badge
            v-model="isBadgeShow"
            overlap
            :content="notfcSize"
            offset-x="22"
            offset-y="22"
            color="red"
          >
            <v-btn icon justify="space-around" dark v-bind="attrs" v-on="on">
              <v-icon dark>
                mdi-bell
              </v-icon>
            </v-btn>
          </v-badge>
        </template>
        <!-- 알람 버튼 클릭 시 나오는 메뉴 -->
        <v-card>
          <v-list-item-content class="justify-center">
            <div class="mx-auto text-center alertMenu">
              <!-- alerts에 있는 알림 반복해서 생성 -->
              <v-alert
                v-for="(alert, index) in alerts"
                :key="index"
                color="rgb(42, 46, 72)"
                border="left"
                elevation="2"
                colored-border
                icon="mdi-bell-ring"
              >
                <div @click="goSrDetail(alerts[index])" style="cursor:pointer">
                  {{ alerts[index].msg }}
                </div>
              </v-alert>

              <div class="text-center" v-if="alerts.length == 0">
                알람이 없습니다!
              </div>
            </div>
          </v-list-item-content>
        </v-card>
      </v-menu>

      <!-- 유저 버튼 -->
      <v-menu class="justify-center" bottom left offset-y :nudge-width="150">
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon justify="space-around" dark v-bind="attrs" v-on="on">
            <v-avatar>
              <v-icon dark>
                mdi-account-circle
              </v-icon>
            </v-avatar>
          </v-btn>
        </template>
        <!-- 유저 버튼 클릭 시 나오는 메뉴 -->
        <v-card>
          <v-list-item-content class="justify-center">
            <div class="mx-auto text-center">
              <v-avatar color="teal" class="mb-3">
                <span class="white--text headline">{{ avatarNm }}</span>
              </v-avatar>
              <div class="mt-4 mb-7">
                <h4>{{ usrNm }}님</h4>
                <h4>반갑습니다. :)</h4>
                <br /><br />
                <h5>소속 회사명</h5>
                <h4>{{ coNm }}</h4>
              </div>
              <v-btn color="rgb(42, 46, 72)" dark @click="logout">
                로그아웃
              </v-btn>
            </div>
          </v-list-item-content>
        </v-card>
      </v-menu>
    </v-app-bar>
    <v-navigation-drawer
      v-model="drawer"
      dark
      color="rgb(42, 46, 72)"
      app
      temporary
    >
      <v-list-item class="header-padding">
        <v-list-item-content>
          <v-list-item-title>
            MENU
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-divider></v-divider>
      <v-list nav dense>
        <v-list-item-group v-model="group" active-class="text--accent-4">
          <v-list-item
            v-for="menu in menus"
            :key="menu.menuNm"
            :to="menu.routerAddr"
          >
            <v-list-item-title>{{ menu.menuNm }} </v-list-item-title>
          </v-list-item>
        </v-list-item-group>
        <div class="mt-15 ">
          <v-list-item-content class="justify-center mb-n10 pb-n5">
            <v-select
              v-if="menuList.length > 1"
              v-model="auth"
              @change="changeMenu"
              :items="menuList"
              dense
              outlined
            ></v-select>
          </v-list-item-content>

          <v-list-item-content class="justify-center">
            <v-select
              v-if="this.$store.state.usrInfo.menuAuth == 'manager'"
              v-model="selectedCo"
              @change="changeCo"
              :items="coList"
              dense
              outlined
            ></v-select>
          </v-list-item-content>
        </div>
      </v-list>
    </v-navigation-drawer>
    <alert-dialog
      v-if="logOutModal"
      dialogTitle="로그아웃 되었습니다."
      dialogText="서비스를 이용하기 위해서 재로그인이 필요합니다."
      @close="logOutModalValue"
    ></alert-dialog>

    <!-- 신규 문의 발생 시 모달창 -->
    <v-dialog v-model="newInquModal" width="400" hide-overlay>
      <NewInquDialog
        :newInquModalData="newInquModalData"
        @changeNewInquModal="changeNewInquModal"
      />
    </v-dialog>

    <!-- 문의 답변 도착 시 모달창 -->
    <v-dialog v-model="inquAnsModal" width="400" hide-overlay>
      <InquAnsDialog
        :inquAnsModalData="inquAnsModalData"
        @changeInquAnsModal="changeInquAnsModal"
      />
    </v-dialog>
  </v-card>
</template>
<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mdiBell } from "@mdi/js";
import { mdiChatQuestion } from "@mdi/js";
import { mdiCommentQuestion } from "@mdi/js";
import { mdiChatQuestionOutline } from "@mdi/js";
import { mdiCheckboxBlankCircle } from "@mdi/js";
import { mdiBellRing } from "@mdi/js";
import RltmInquLobbyModal from "../rltmInqu/RltmInquLobbyModal.vue";
import RltmInquStartModal from "../rltmInqu/RltmInquStartModal.vue";
import RltmInquInputTitleModal from "../rltmInqu/RltmInquInputTitleModal.vue";
import RltmInquChatModal from "../rltmInqu/RltmInquChatModal.vue";
// import OperLobbyModalStart from "../rltmInqu/OperLobbyModalStart.vue";
// import OperLobbyModalCont from "../rltmInqu/OperLobbyModalCont.vue";

import AlertDialog from "./AlertDialog.vue"
import NewInquDialog from "../rltmInqu/NewInquDialog.vue";
import InquAnsDialog from "../rltmInqu/InquAnsDialog.vue";
import OperLobbyModal from "../rltmInqu/OperLobbyModal.vue";
import { mapState } from "vuex";

import API from "../../api/API";

export default {
  components: {
    RltmInquLobbyModal,
    RltmInquStartModal,
    RltmInquInputTitleModal,
    RltmInquChatModal,
    AlertDialog,
    // OperLobbyModalStart,
    // OperLobbyModalCont,
    NewInquDialog,
    InquAnsDialog,
    OperLobbyModal,
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용
      avatarNm: "", // 이름 2글자만 보여줌
      coNm: this.$store.state.usrInfo.coNm,
      coCd: this.$store.state.usrInfo.coCd,
      auth: this.$store.state.usrInfo.menuAuth,
      tab: null,
      mdiBell,
      mdiCommentQuestion,
      mdiChatQuestion,
      mdiChatQuestionOutline,
      mdiCheckboxBlankCircle,
      mdiBellRing,
      drawer: false,
      group: null,
      menus: this.$store.state.usrInfo.menus,
      selectedCo: this.$store.state.usrInfo.coCd,
      // 알람 create될 때 백에서 데이터 받아오기
      alerts: [],
      dialog_lobby: false,
      dialog_start: false,
      dialog_title: false,
      dialog_chat: false,
      dialog_oper: false,
      dialogFlg: false,
      selectedSysCd: 0,
      selectedSysNm: "",
      inquId: 0,
      chatList: [],
      operChatList: [],
      newChatList: [],
      sbjct: "",
      resetSysInfo: false,
      logOutModal: false,
      notfcSize: 0,
      isBadgeShow: false,
      isInquBadgeShow: false,
      menuList: [],

      // 새로운 문의 도착 시 모달
      newInquModal: false,
      newInquModalData: "",

      // 답변 모달
      inquAnsModal: false,
      inquAnsModalData: "",
      
      chatListCnt: 0,
      newInquSize: 0,
      operInquSize: 0,
      totalOperInquSize: 0,
    };
  },
  computed: {
    storeMenus() {
      return this.$store.state.usrInfo.cnt;
    },
    ...mapState({
      coList: (state) => state.CommonData.companyList,
    }),
  },
  methods: {
    logout() {
      // 로그아웃 함수 구현

      this.logOutModal = true;
      this.newInquModal = false;
      this.notfcSize = 0;
      this.newInquSize = 0;
    },
    logOutModalValue(value) {
      this.logOutModal = value;
      
      this.$store.commit("usrInfo/clearInfo");
      this.$store.commit("token/clearToken");
    },
    goSrDetail(trget) {
      // 디비에 읽음처리
      console.log(trget);
      API.notfcCnfrmYnTrue(
        "notfcId=" + trget.notfcId,
        (res) => {
          this.$router.push(trget.trgetUrl);
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    },
    changeCo() {
      console.log(this.selectedCo);
      this.$store.commit("usrInfo/changeCo", {
        coCd: this.selectedCo,
        coNm: this.coList.find((e) => e.value == this.selectedCo).text,
      });
      this.coNm = this.coList.find((e) => e.value == this.selectedCo).text;
    },
    changeMenu(value) {
      if (this.$store.state.usrInfo.menuAuth != value) {
        //usrInfo의 메뉴 정보를 바꾼다.
        this.$store.commit("usrInfo/changeMenu", {
          menuAuth: this.auth,
        });

        //api에서 메뉴를 새로 받아온다.
        this.getMenus();
      }
    },
    getMenus() {
      API.getMenusByAuth(
        "auth=" + this.$store.state.usrInfo.menuAuth,
        (res) => {
          console.log("성공!");
          this.$store.commit("usrInfo/changeMenus", {
            menus: res,
          });
          console.log(this.$store.state.usrInfo.menus);

          this.goMain();
          this.$router.go(0);
        },
        (err) => {
          console.log(err);
        }
      );
    },
    changeDialogInqu(dialog_lobby, dialog_start, inqu) {
      this.dialog_lobby = dialog_lobby;
      if (inqu == null) {
        // 새로 채팅 시작
        this.dialog_start = dialog_start;
      } else {
        // 기존 채팅 열기
        this.inquId = inqu;
        this.selectedSysCd = inqu.sysCd;
        this.dialog_chat = true;
      }
    },
    changeDialogStart(dialog_start, selectedSysCd, selectedSysNm) {
      this.dialog_start = dialog_start;
      this.selectedSysCd = selectedSysCd;
      this.selectedSysNm = selectedSysNm;
      if (this.selectedSysCd > 0) {
        // 시스템 선택
        this.dialog_title = true;
        this.dialogFlg = !this.dialogFlg;
      }
    },
    changeDialogTitle(dialog_title, dialog_start, inquId) {
      this.dialog_title = dialog_title;
      this.dialog_start = dialog_start;
      this.inquId = inquId;
      if (inquId != null) {
        this.dialog_chat = true;
      }
    },
    changeDialogChat(dialog_chat) {
      this.dialog_chat = dialog_chat;
      if(this.auth=="user")
        this.getUsrUncnfrmCnt();
      else if(this.auth=="operator") {
        this.getOperInquCnt();
        this.getNewInquCnt();
      }
    },
    changeDialogOper(inqu) {
      this.dialog_oper = false;
      if (inqu != null) {
        this.inquId = inqu;
        this.selectedSysCd = inqu.sysCd;
        this.dialog_chat = true;
      }
    },
    changeNewInquModal(value) {
      this.newInquModal = false;
      // y이면 모달창 띄우고 이 운영자가 답변
      if (value != null) {
        API.startAns(
          "inquId=" + value,
          (res) => {
            this.changeDialogOper(value);
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
    changeInquAnsModal(value) {
      this.inquAnsModal = false;
      // 클릭하면 문의창 띄우기, 닫기하면 창닫기
      if (value != null) {
        this.inquId = value;
        this.dialog_chat = true;
      }
    },
    goMain() {
      if (this.$store.state.usrInfo.menuAuth == "user")
        this.$router.push("/customer/main");
      else if (this.$store.state.usrInfo.menuAuth == "operator")
        this.$router.push("/operator/main");
      else this.$router.push("/manager/main");
    },
    getNotfc() {
      if (this.$store.state.usrInfo.usrAuth) {
        API.getNotfc(
          "usrId=" + this.usrId,
          (res) => {
            this.alerts = res;
            this.notfcSize = this.alerts.length;
            if (this.notfcSize > 0) this.isBadgeShow = true;
            else this.isBadgeShow = false;
          },
          (err) => {
            console.log(err);
          }
        );
      } else {
        this.alerts = [];
        this.notfcSize = 0;
      }
    },
    getNewInquCnt() {
      if (this.$store.state.usrInfo.usrId) {
        API.getNewInquCnt(
          "",
          (res) => {
            this.newInquSize = res;
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
    getOperInquCnt() {
      API.getOperCntUnfrmLog(
          "",
          (res) => {
            this.operInquSize = res;
          },
          (err) => {
            console.log(err);
          }
      )
    },
    getUsrUncnfrmCnt() {
        API.getCntUncnfrmLog(
          "",
          (res) => {
            this.chatListCnt = res;
            if(res>0) this.isInquBadgeShow= true;
          },
          (err) => {
            console.log(err);
          }
        );
    },

    connect() {
      const serverURL = `${process.env.VUE_APP_API_URL}/socket`;
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          // 신규 문의 받아옴
          this.stompClient.subscribe("/topic/alert", (res) => {
            if (
              this.auth == "operator" &&
              JSON.parse(res.body).coCd == this.coCd
            ) {
              this.newInquModalData = JSON.parse(res.body);
              this.newInquModal = true;
              this.newInquSize += 1;
            }
          });
          // 기존 진행중이던 문의 답변
          this.stompClient.subscribe("/topic/log", (res) => {
            if (
              (this.usrId == JSON.parse(res.body).inquUsrId &&
                this.usrId != JSON.parse(res.body).sndr) ||
              (this.usrId == JSON.parse(res.body).answrr &&
                this.usrId != JSON.parse(res.body).sndr)
            ) {
              if (
                !(
                  this.dialog_chat == true &&
                  this.inquId == JSON.parse(res.body).inquId
                )
              ) {
                // 현재 채팅화면 보고 있으면 모달X
                this.inquAnsModalData = JSON.parse(res.body);
                this.inquAnsModal = true;
                if(this.auth=="user") this.chatListCnt+=1;
                else if(this.auth=="operator") this.operInquSize+=1;
              }
            }
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },

  },
  created() {
    this.isBadgeShow = false;
    this.isInquBadgeShow = false;
    this.chatList = [];
    this.newChatList = [];
    if (this.$store.state.usrInfo.usrNm.length > 0) {
      this.avatarNm = this.usrNm.substring(
        this.usrNm.length - 2,
        this.usrNm.length
      );
    }
    // 알림 받아오기
    this.getNotfc();
    // 주기적으로 받아오기
    setInterval(this.getNotfc, 10000);
    
    // 문의 badge개수 받아오기
    if(this.$store.state.usrInfo.menuAuth=="operator"){
      this.getNewInquCnt();
      this.getOperInquCnt();
    }
    if(this.$store.state.usrInfo.menuAuth=="user")
      this.getUsrUncnfrmCnt();

    //메뉴 셀렉트 넣기
    if (this.$store.state.usrInfo.usrAuth == true) {
      this.menuList.push({ text: "고객 페이지", value: "user" });
    }

    if (this.$store.state.usrInfo.operatorAuth == true) {
      this.menuList.push({ text: "운영자 페이지", value: "operator" });
    }

    if (this.$store.state.usrInfo.managerAuth == true) {
      this.menuList.push({ text: "관리자 페이지", value: "manager" });
    }

    this.connect();
  },
  watch: {
    group() {
      this.drawer = false;
    },
    dialog_lobby() {
      if (this.dialog_lobby) {
        // 채팅 목록 갱신
        API.getChatList(
          "usrId=" + this.usrId,
          (res) => {
            this.chatList = res;
          },
          (err) => {
            console.log(err);
          }
        );
      this.getUsrUncnfrmCnt();
      }
    },
    dialog_oper() {
      if (this.dialog_oper) {
        API.getOperChatList(
          "",
          (res) => {
            this.operChatList = res;
          },
          (err) => {
            console.log(err);
          }
        );
        API.getNewChatList(
          "",
          (res) => {
            this.newChatList = res;
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
    dialog_start() {
      this.resetSysInfo = !this.resetSysInfo;
    },
    storeMenus() {
      this.$router.go(0);
    },
    newInquSize() {
      this.totalOperInquSize = this.newInquSize+this.operInquSize;
      if(this.totalOperInquSize>0) this.isInquBadgeShow= true;
    },
    operInquSize() {
      this.totalOperInquSize = this.newInquSize+this.operInquSize;
      if(this.totalOperInquSize>0) this.isInquBadgeShow= true;
    }
  },
};
</script>
<style scoped>
.header-padding {
  padding-top: 15px;
}
.v-navigation-drawer {
  z-index: 100 !important;
}
.zIndex {
  z-index: 100 !important;
}
.alertMenu {
  max-height: 270px;
  overflow-y: auto;
}
</style>
