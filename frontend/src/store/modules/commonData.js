import API from "@/api/API";
import Vue from "vue";

// state
const state = () => ({
    companyList: Object.create(null),

    // 전체 시스템
    systemName: [],

    systemByCompany: Object.create(null),
    
    /**
     * jobGb 구조
     * [
     * {
     *  sysCd : 1,
     *  job: [
     *      {
     *          job: 평가,
     *          jobGbId: 2,
     *          sysCd: 1
     *      },
     *      ...
     *  ]
     * },
     * ...]
     */
    jobGb: [],

    // (key: jobGbId, value: jobGbName)
    jobGbName: Object.create(null)
    
})

// getters
const getters = {
    
}

// mutations
const mutations = {
    // company, system, jobGb 과 관련된 정보를 set
    setSystemName (state) {

        // 전체 회사 정보
        API.getCoNmList('',
        (res) => {
            state.companyList = [];
            let result = res.filter(e => e.useYn);
            result.forEach(element => {
                state.companyList.push({
                    value: element.coCd,
                    text: element.coNm
                });

                // 회사별 시스템 정보
                API.getSysNmByCoCd(`coCd=${element.coCd}`,
                (res) => {
                    //text: sysCd, value : sysNm
                    Vue.set(state.systemByCompany, element.coCd, res);
                },
                (err) => {
                    console.log(err);
                });
            });
        },
        (err) => {
            console.log(err);
        });

        // 전체 jobGb
        API.getAllJobGbInfo('',
        (res) => {
            res.forEach(element => {
                Vue.set(state.jobGbName, element.jobGbId, element.job)
            })
        },
        (err) => {
            console.log(err);
        })

        // 전체 시스템 정보
        API.getAllSysInfo('',
        (res) => {
            state.systemName = [];
            res.forEach(e=> {
                state.systemName.push({
                    text: e.sysNm,
                    value: e.sysCd
                })
            })

             // 저장된 system 정보를 바탕으로 해당하는 jobGb 정보를 리스트로 저장
            setJobGb(state);
        },
        (err) => {
            console.log(err);
        })
    },
}

function setJobGb(state) {
    state.jobGb = [];
    for(let sys of state.systemName) {
        API.getJobGbBySysCd(
            'sysCd='+sys.value,
            (res) => {
                let jobData = {
                    sysCd: sys.value,
                    job: res
                }
                state.jobGb.push(jobData);
            },
            (err) => {
                console.log(err);
            })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations
}