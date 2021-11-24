<template>
  <div>
    <v-app>
      <v-main class="ma-3">
    유저이름: 
    <input
      v-model="usrNm"
      type="text"
    >
    내용: <input
      v-model="msg"
      type="text"
      @keyup="sendMessage"
    >
    <div
      v-for="(item, idx) in recvList"
      :key="idx"
    >
      <h3>유저이름: {{ item.usrNm }}</h3>
      <h3>내용: {{ item.msg }}</h3>
    </div>
        <v-btn @click="closeModal">닫기</v-btn>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  data() {
    return {
      usrNm: "",
      msg: "",
      recvList: [],
      dialog_inqu: true,
    }
  },
  created() {
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect()
  },
  methods: {
    sendMessage (e) {
      if(e.keyCode === 13 && this.usrNm !== '' && this.msg !== ''){
        this.send()
        this.msg = ''
      }
    },    
    send() {
      console.log("Send message:" + this.msg);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          usrNm: this.usrNm,
          msg: this.msg 
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    },    
    connect() {
      const serverURL = "http://localhost:8080/socket"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe("/send", res => {
            console.log('구독으로 받은 메시지 입니다.', res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            this.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
    },
    closeModal() {
      this.dialog_inqu= false;
      this.$emit('changeDialogInqu', this.dialog_inqu);
    }
  },

}
</script>