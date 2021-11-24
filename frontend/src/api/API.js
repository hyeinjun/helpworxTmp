import http from "./http-common.js";
import header from "./header.js";
import store from "../store/index.js";

const getApi = (url, data, callback, errorCallback) => {
  http
    .get(url + "?" + data, header())
    .then((res) => {
      // RestAPI 서버가 null 을 응답하는 경우
      if (res == null) {
        let error = { msg: "Server response is NULL" };
        errorCallback(error);
      }
      // RestAPI 에서 null 이 아닌 응답이 왔을 때
      else {
        // res.data 가 Rest 서버에서 반환한 객체
        let responseData = res.data;

        // 서버의 수행이 성공적이었다면 callback 수행
        // if (responseData != "") {
        //   callback(responseData);
        // }
        // // 서버에서 오류가 발생했다면
        // else {
        //   // 그 오류메시지로 errorCallback 부름
        //   let error = { msg: responseData.msg };
        //   errorCallback(error);
        // }
                if (res.status) {
                  callback(res.data);
                }
                // 서버에서 오류가 발생했다면
                else {
                  // 그 오류메시지로 errorCallback 부름
                  let error = { msg: res.msg };
                  errorCallback(error);
                }
      }
    })
    .catch((err) => {
      err.msg = url + ": Exception Occurred";
      errorCallback(err);
    });
};

const postApi = (url, data, callback, errorCallback) => {
  http.post(url, data, header())
    .then((res) => {
      // RestAPI 서버가 null 을 응답하는 경우
      if (res == null) {
        let error = { msg: "Server response is NULL" };
        errorCallback(error);
      }
      // RestAPI 에서 null 이 아닌 응답이 왔을 때
      else {
        // res.data 가 Rest 서버에서 반환한 객체

        // 서버의 수행이 성공적이었다면 callback 수행
        if (res.status) {
          callback(res.data);
        }
        // 서버에서 오류가 발생했다면
        else {
          // 그 오류메시지로 errorCallback 부름
          let error = { msg: res.msg };
          errorCallback(error);
        }
      }
    })
    .catch((err) => {
      err.msg = url + ": Exception Occurred";
      errorCallback(err);
    });
};

const putApi = (url, data, callback, errorCallback) => {
  http.put(url, data, header())
    .then((res) => {
      // RestAPI 서버가 null 을 응답하는 경우
      if (res == null) {
        let error = { msg: "Server response is NULL" };
        errorCallback(error);
      }
      // RestAPI 에서 null 이 아닌 응답이 왔을 때
      else {
        // res.data 가 Rest 서버에서 반환한 객체

        // 서버의 수행이 성공적이었다면 callback 수행
        if (res.status) {
          callback(res.data);
        }
        // 서버에서 오류가 발생했다면
        else {
          // 그 오류메시지로 errorCallback 부름
          let error = { msg: res.msg };
          errorCallback(error);
        }
      }
    })
    .catch((err) => {
      err.msg = url + ": Exception Occurred";
      errorCallback(err);
    });
};

const deleteApi = (url, data, callback, errorCallback) => {
  let tokendata = "Bearer " + store.state.token.accessToken;
  http.delete(url + "?" + data, {headers:{accessToken:tokendata}})
    .then((res) => {
      // RestAPI 서버가 null 을 응답하는 경우
      if (res == null) {
        let error = { msg: "Server response is NULL" };
        errorCallback(error);
      }
      // RestAPI 에서 null 이 아닌 응답이 왔을 때
      else {
        // res.data 가 Rest 서버에서 반환한 객체

        // 서버의 수행이 성공적이었다면 callback 수행
        if (res.status) {
          console.log("리턴 성공");
          callback(res.data);
        }
        // 서버에서 오류가 발생했다면
        else {
          // 그 오류메시지로 errorCallback 부름
          let error = { msg: res.msg };
          errorCallback(error);
        }
      }
    })
    .catch((err) => {
      err.msg = url + ": Exception Occurred";
      errorCallback(err);
    });
};

const API = {
  getCompanyList: (data, callback, errorCallback) =>
    getApi("/main/co", data, callback, errorCallback),
  getEndSchdCoList: (data, callback, errorCallback) =>
    getApi("/main/endco", data, callback, errorCallback),
  getNewContCoList: (data, callback, errorCallback) =>
    getApi("/main/newco", data, callback, errorCallback),
  postLogInInfo: (data, callback, errorCallback) =>
    postApi("/login", data, callback, errorCallback),
  getSysNmByCoCd: (data, callback, errorCallback) =>
    getApi("/sys/coCd", data, callback, errorCallback),
  getJobGbBySysCd: (data, callback, errorCallback) =>
    getApi("/jobGb/sys", data, callback, errorCallback),
  getCoNmList: (data, callback, errorCallback) =>
    getApi("/conm", data, callback, errorCallback),
  getSelectManagerCo: (data, callback, errorCallback) =>
    getApi("/mngco", data, callback, errorCallback),
  createInquWithSysCd: (data, callback, errorCallback) =>
    postApi("/inqu/create", data, callback, errorCallback),
  getInquUsrIdByInquId: (data, callback, errorCallback) =>
    getApi("/inqu/find/inquUsrId", data, callback, errorCallback),
  saveInquLog: (data, callback, errorCallback) =>
    postApi("/inqu/log/save", data, callback, errorCallback),
  getChatList: (data, callback, errorCallback) =>
    getApi("/inqu/find/all", data, callback, errorCallback),
  getBeforeChat: (data, callback, errorCallback) =>
    getApi("/inqu/bef", data, callback, errorCallback),
  getCntWithSysCd: (data, callback, errorCallback) =>
    getApi("/sys/cnt", data, callback, errorCallback),
  getIncmpListWithUsrId: (data, callback, errorCallback) =>
    getApi("/mysr/incmp", data, callback, errorCallback),
  getIncmpListWithCoCd: (data, callback, errorCallback) =>
    getApi("/cosr/incmp", data, callback, errorCallback),
  getSrRcptTreatWithSrId: (data, callback, errorCallback) =>
    getApi("/sr/progress/srId", data, callback, errorCallback),
  getSrWithSrId: (data, callback, errorCallback) =>
    getApi("/sr/{srId}", data, callback, errorCallback),
  deleteSr: (data, callback, errorCallback) =>
    deleteApi("/sr", data, callback, errorCallback),
  deleteSrList: (data, callback, errorCallback) =>
    deleteApi("/sr/list", data, callback, errorCallback),
  deleteSrListByOper: (data, callback, errorCallback) =>
    deleteApi("/sr/rcpt-treat/list", data, callback, errorCallback),
  rejectSr: (data, callback, errorCallback) =>
    postApi("/sr/reject", data, callback, errorCallback),
  modifySr: (data, callback, errorCallback) =>
    putApi("/sr", data, callback, errorCallback),
  saveSr: (data, callback, errorCallback) =>
    postApi("/sr", data, callback, errorCallback),
  getAuth: (data, callback, errorCallback) =>
    getApi("/usr-info", data, callback, errorCallback),
  getCoDetail: (data, callback, errorCallback) =>
    getApi("/co/detail", data, callback, errorCallback),
  modifyCo: (data, callback, errorCallback) =>
    putApi("/co/detail", data, callback, errorCallback),
  deleteCo: (data, callback, errorCallback) =>
    deleteApi("/co", data, callback, errorCallback),
  insertCo: (data, callback, errorCallback) =>
    postApi("/co", data, callback, errorCallback),
  deleteInqu: (data, callback, errorCallback) =>
    deleteApi("/inqu", data, callback, errorCallback),
  getUsingSysList: (data, callback, errorCallback) =>
    getApi("/sys/using", data, callback, errorCallback),
  getInquByCoCdAndSysAndStateAndDscr: (data, callback, errorCallback) =>
    getApi("/inqu/find/co-sys-state-dscr", data, callback, errorCallback),
  getInquByUsrIdAndSysAndStateAndDscr: (data, callback, errorCallback) =>
    getApi("/inqu/find/usr-sys-state-dscr", data, callback, errorCallback),
  getCustomerList: (data, callback, errorCallback) =>
    getApi("/customer/list", data, callback, errorCallback),
  getCustomerDetail: (data, callback, errorCallback) =>
    getApi("/customer/detail", data, callback, errorCallback),
  modifyCustomer: (data, callback, errorCallback) =>
    putApi("/customer/detail", data, callback, errorCallback),
  insertCustomer: (data, callback, errorCallback) =>
    postApi("/customer", data, callback, errorCallback),
  finInqu: (data, callback, errorCallback) =>
    putApi("/inqu/fin", data, callback, errorCallback),
  deleteRcpt: (data, callback, errorCallback) =>
    deleteApi("/sr/rcpt", data, callback, errorCallback),
  deleteTreat: (data, callback, errorCallback) =>
    deleteApi("/sr/treat", data, callback, errorCallback),
  saveSrRcpt: (data, callback, errorCallback) =>
    postApi("/sr/rcpt", data, callback, errorCallback),
  saveSrTreat: (data, callback, errorCallback) =>
    postApi("/sr/treat", data, callback, errorCallback),
  getRcptBySrId: (data, callback, errorCallback) =>
    getApi("/sr/rcpt", data, callback, errorCallback),
  getTreatBySrId: (data, callback, errorCallback) =>
    getApi("/sr/treat", data, callback, errorCallback),
  deleteCustomerList: (data, callback, errorCallback) =>
    deleteApi("/customer/list", data, callback, errorCallback),
  deleteCustomer: (data, callback, errorCallback) =>
    deleteApi("/customer", data, callback, errorCallback),

  getSrSystemChart: (data, callback, errorCallback) =>
    getApi("/sr/chart/count", data, callback, errorCallback),
  getSrCntStatusChart: (data, callback, errorCallback) =>
    getApi("/sr/chart/count/per-month", data, callback, errorCallback),
  getSrAvgTimeDataChart: (data, callback, errorCallback) =>
    getApi("/sr/chart/avg/rqmt", data, callback, errorCallback),
  getRcptTreatState: (data, callback, errorCallback) =>
    getApi("/sr/state/rcpt-treat", data, callback, errorCallback),
  getMenusByAuth: (data, callback, errorCallback) =>
    getApi("/menu", data, callback, errorCallback),
  getAllSysInfo: (data, callback, errorCallback) =>
    getApi("/sys", data, callback, errorCallback),
  getAllJobGbInfo: (data, callback, errorCallback) =>
    getApi("/jobgb", data, callback, errorCallback),
  cancelReturnSr: (data, callback, errorCallback) =>
    getApi("/sr/cancel/reject", data, callback, errorCallback),
  getProceedInqu: (data, callback, errorCallback) =>
    getApi("/inqu/prgss", data, callback, errorCallback),
  getRejectWithSrId: (data, callback, errorCallback) =>
    getApi("/sr/reject", data, callback, errorCallback),
  getNotfc: (data, callback, errorCallback) =>
    getApi("/notfc", data, callback, errorCallback),
  notfcCnfrmYnTrue: (data, callback, errorCallback) =>
    deleteApi("/notfc", data, callback, errorCallback),
  getNewChatList: (data, callback, errorCallback) =>
    getApi("/inqu/find/new", data, callback, errorCallback),
  getOperChatList: (data, callback, errorCallback) =>
    getApi("/inqu/find/answrr", data, callback, errorCallback),
  startAns: (data, callback, errorCallback) =>
    getApi("/inqu/start/ans", data, callback, errorCallback),
  getNewInquCnt: (data, callback, errorCallback) =>
    getApi("/inqu/new/cnt", data, callback, errorCallback),

  getInfoModReq: (data, callback, errorCallback) =>
    getApi("/cust/info-mod/detail", data, callback, errorCallback),
  insertInfoModReq: (data, callback, errorCallback) =>
    postApi("/cust/info-mod", data, callback, errorCallback),
  modifyInfoModReq: (data, callback, errorCallback) =>
    putApi("/cust/info-mod", data, callback, errorCallback),
  modifyInfoState: (data, callback, errorCallback) =>
    putApi("/mng/info-mod", data, callback, errorCallback),
  getMngInfoModReq: (data, callback, errorCallback) =>
    getApi("/mng/info-mod/detail", data, callback, errorCallback),
  getMainInfoModReq: (data, callback, errorCallback) =>
    getApi("/main/info-req", data, callback, errorCallback),
  putLogStateCnfrm: (data, callback, errorCallback) =>
    putApi("/inqu/log/cnfrm", data, callback, errorCallback),
  getCntUncnfrmLog: (data, callback, errorCallback) =>
    getApi("/inqu/log/uncnfrm/cnt", data, callback, errorCallback),
  getOperCntUnfrmLog: (data, callback, errorCallback) =>
    getApi("/inqu/oper/log/uncnfrm/cnt", data, callback, errorCallback),
  deleteCustInfoModReq: (data, callback, errorCallback) =>
    deleteApi("/cust/info-mod", data, callback, errorCallback),
  deleteMngInfoModReq: (data, callback, errorCallback) =>
    deleteApi("/mng/info-mod", data, callback, errorCallback),
  getReqList: (data, callback, errorCallback) =>
    getApi("/main/info-req", data, callback, errorCallback),
  getCoCdList: (data, callback, errorCallback) =>
    getApi("all/coCd", data, callback, errorCallback),
  getCoCd: (data, callback, errorCallback) =>
    getApi("/cocd-info", data, callback, errorCallback),
};

export default API;
