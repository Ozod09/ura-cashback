import * as types from "../actionTypes/AppActionTypes";
import {createReducer} from "../../utils/StoreUtils";



const initState = {
    openCompany: false,
    companyKassa: [],
    openLogin: false,
    openCabinet: false,
    adminPanel: false,
    res:false,
    user: [],
    search: '',
    page: "1",
    size:"5",
    activeUser: false,
    orders: [],
    company: [],
    companyInfo: [],
    companyStat: {},
    oneOrder: {},
    imgId: {},
    active: true,
    showModal: false,
    deleteShowModal: false,
    editModal: false,
    modal: false,
    attachmentId: '',
    currentCompany: '',
    currentUser: '',
    currentAdmin: '',
}

const reducers = {

    [types.REQUEST_SUCCESS](state) {
        state.showModal = false
        state.deleteShowModal = false
    },
    [types.GET_ORDER_LIST](state, payload) {
        state.orders = payload.payload
    },
    [types.GET_USER_LIST](state, payload) {
        state.user = payload.payload
    },
    [types.REQUEST_SUCCESS](state) {
        state.showModal = false
    },
    [types.GET_COMPANY_LIST](state, payload) {
        state.company = payload.payload
    },
    [types.GET_ORDER_LOGIN](state, payload) {
        state.currentAdmin = payload.payload
    },
    [types.GET_ATTACHMENT_ID](state, payload) {
        state.attachmentId = payload.payload
    },

    updateState(state, {payload}) {
        return {
            ...state,
            ...payload,
        };
    },
};

export default createReducer(initState, reducers);
