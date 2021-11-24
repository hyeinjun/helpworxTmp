<template>
  <v-card class="pa-6 my-5"><v-row class="justify-center">
    <v-col cols="3">
      
      <v-card-title>고객 목록</v-card-title>
    </v-col>
    <v-spacer></v-spacer>
    <v-col cols="12" sm="1" xs="2">
        <v-btn color="rgb(42, 46, 72)" dark @click="insertCust">등록</v-btn>
    </v-col>

  </v-row>
  <v-data-table
    :headers="headers"
    :items="customerList"
    :items-per-page="5"
    @click:row='goCustDetail'
    class="elevation-1"
  ></v-data-table>
  </v-card>
</template>

<script>
import API from "@/api/API";

export default {
    data(){
        return{
            headers:[
                {text:'고객사',value:'coCd'},
                {text:'고객 ID',value:'usrId'},
                {text:'고객 이름',value:'nm'},
                {text:'사용 여부',value:'useYn'}
            ],
            customerList: [],
        }
    },
    created(){
      console.log(this.$store.state.token.accessToken);
        API.getCustomerList('',
            (res) => {
          this.customerList=res;
          console.log(this.customerList);
        },
        (err) => {
          console.log(err);
        }
        )
    },
    methods:{
      goCustDetail(target) {
        console.log(target.coCd+" "+target.coNm);
        this.$router.push('/manager/cust/detail/'+target.usrId);
      },
      insertCust(){
        this.$router.push('/manager/cust/insert');
      }
    }

}
</script>

<style>

</style>