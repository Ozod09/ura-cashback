import * as api from "../../api/AppApi";
import {
    activeUser,
    addAttachment,
    addCompany,
    addCompanyAdmin,
    addCompanyKassa,
    addCompanyUser,
    addOrder,
    deleteOrder,
    editCompany,
    editCompanyKassa,
    editCompanyPassword,
    editOrder,
    findByPhoneNumber,
    findByUser,
    getCabinetClient,
    getCabinetCompany,
    getCabinetKassa,
    getCabinetOrder,
    getCompanies,
    getOneUsers,
    getOrders,
    getUsers,
    loginOrder,
    loginSuperAdmin,
    removeUsers,
    statisticCompany
} from "../../api/AppApi";
import * as types from "../actionTypes/AppActionTypes";
import {toast} from "react-toastify";

export const byId = id => document.getElementById(id) && document.getElementById(id).value;

export const getUser = () => (dispatch) => {
    dispatch({
        api: getUsers,
        types: [
            types.REQUEST_START,
            types.GET_USER_LIST,
            types.REQUEST_ERROR
        ]
    })
}

export const loginCompany = (payload) => (dispatch) => {
    dispatch({
        api: getCabinetCompany,
        types: [
            types.REQUEST_START,
            types.GET_USER_COMPANY,
            types.REQUEST_ERROR,
        ],
        data: payload
    }).then(res => {
        if (res !== undefined) {
            console.log(res);
            sessionStorage.setItem("companyId", res.payload.id)
            dispatch({
                type: 'updateState',
                payload: {
                    companyInfo: res.payload,
                    openLogin: true
                }
            })
        } else toast.error("Company not Active");
    })
}

export const companyClient = (payload) => (dispatch) => {
    dispatch({
        api: getCabinetClient,
        types: [
            types.REQUEST_START,
            types.GET_USER_COMPANY,
            types.REQUEST_ERROR,
        ],
        data: payload
    }).then(res => {
        console.log(res.payload);
        dispatch({
            type: 'updateState',
            payload: {
                cabinetClient: res.payload
            }
        })
    })
}

export const companyOrder = (payload) => (dispatch) => {
    dispatch({
        api: getCabinetOrder,
        types: [
            types.REQUEST_START,
            types.GET_USER_COMPANY,
            types.REQUEST_ERROR,
        ],
        data: payload
    }).then(res => {
        dispatch({
            type: 'updateState',
            payload: {
                cabinetOrder: res.payload
            }
        })
    })
}

export const companyKassa = (payload) => (dispatch) => {
    dispatch({
        api: getCabinetKassa,
        types: [
            types.REQUEST_START,
            types.GET_USER_COMPANY,
            types.REQUEST_ERROR,
        ],
        data: payload
    }).then(res => {
        dispatch({
            type: 'updateState',
            payload: {
                cabinetKassa: res.payload
            }
        })
    })
}

export const editCompanyAdminPassword = (payload) => (dispatch) => {
    dispatch({
        api: editCompanyPassword,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res.success) {
            toast.success("Successfully edit password")
        } else toast.error("Password not found");
    })
}

export const companyStatistic = (payload) => (dispatch) => {
    dispatch({
        api: statisticCompany,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        console.log(res)
        if (res) {
            dispatch({
                type: 'updateState',
                payload: {
                    companyStat: res.payload
                }
            })
        }
    })
}

export const findByUserPhoneNumber = (payload) => (dispatch) => {
    dispatch({
            api: findByPhoneNumber,
            types: [
                types.REQUEST_START,
                types.GET_ONE_USER_LIST,
                types.REQUEST_ERROR
            ],
            data: payload
        }
    ).then(res => {
        console.log("res: ", res);
        if (res !== undefined) {
            dispatch({
                type: "updateState",
                payload: {
                    activeUser: true,
                    currentUser: res.payload
                }
            });
        } else toast.warning("user not fount");
    });
}

export const getOneUser = (payload) => (dispatch) => {
    dispatch({
        api: getOneUsers,
        types: [
            types.REQUEST_START,
            types.GET_ONE_USER_LIST,
            types.REQUEST_ERROR
        ],
        data: payload
    });
}
export const superAdminLogin = (payload) => (dispatch) => {
    dispatch({
        api: loginSuperAdmin,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res.success) {
            toast.success(res.message)
            dispatch({
                type: 'updateState',
                payload: {
                    openLogin: true
                }
            })
        }
        toast.error(res.message)
    })
}

//company
export const getCompany = () => (dispatch) => {
    dispatch({
        api: getCompanies,
        types: [
            types.REQUEST_START,
            types.GET_COMPANY_LIST,
            types.REQUEST_ERROR
        ]
    })
}

export const saveCompanyAdmin = (payload) => (dispatch) => {
    dispatch({
        api: addCompanyAdmin,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        dispatch({
            type: 'updateState',
            payload: {
                showModal: true,
                currentUser: res && res.payload
            }
        })
        toast.success("Successfully save")
    })
}

export const saveCompanyUser = (payload) => (dispatch) => {
    dispatch({
        api: addCompanyUser,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res !== undefined) {
            toast.success("Successfully save")
            dispatch({
                type: 'updateState',
                payload: {
                    showModal: true,
                    currentUser: res.payload.payload
                }
            })
        } else toast.error("Error");
    })
}

export const saveCompanyKassa = (payload) => (dispatch) => {
    dispatch({
        api: payload.id ? editCompanyKassa : addCompanyKassa,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res !== undefined) {
            dispatch({
                type: 'updateState',
                payload: {
                    showModal: true,
                    openModal: true,
                    currentUser: res.payload
                }
            });
            companyKassa(sessionStorage.getItem("companyId"));
        } else toast.error("Error");
    })
}

export const removeUser = (payload) => (dispatch) => {
    dispatch({
        api: removeUsers,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        toast.success(res.success);
    })
}

export const isActiveUser = (payload) => (dispatch) => {
    dispatch({
        api: activeUser,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(() => {
        dispatch(getUser())
    })
}
export const saveCompany = (payload) => (dispatch) => {
    dispatch({
        api: payload.id ? editCompany : addCompany,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(() => {
        toast.success(`Company ${payload.id ? "Edit" : "Saving"} successfully!`)
        dispatch({
            type: 'updateState',
            payload: {
                openLogin: true
            }
        })
    }).catch(() => {
        toast.error(`Error ${payload.id ? "Edit" : "Saving"} company!`);
    });
}


export const activeCompany = (payload) => (dispatch) => {
    dispatch({
        api: api.activeCompany12(payload),
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload.id
    }).then(() => {
        dispatch(getCompany())
        toast.success("Company active successfully!");
    }).catch(() => {
        toast.error("Error active company!");
    })
}

//////// ------------ Order ------------- ////////////
export const getOrder = () => (dispatch) => {
    dispatch({
        api: getOrders,
        types: [
            types.REQUEST_START,
            types.GET_ORDER_LIST,
            types.REQUEST_ERROR
        ]
    })
}
export const saveOrder = (payload) => (dispatch) => {
    dispatch({
        api: addOrder,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res.success) {
            dispatch(getOrder());
            toast.success("Saqlandi");
            payload.CallBack()
        } else toast.error("Error");
    })
}

export const loginOrderAction = (payload) => (dispatch) => {
    dispatch({
        api: loginOrder,
        types: [
            types.REQUEST_START,
            types.GET_ORDER_LOGIN,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        if (res !== undefined) {
            toast.success("Successfully login");
            dispatch({
                type: "updateState",
                payload: {
                    showModal: true,

                }
            });
        } else toast.error("Kasser not fount");
    });
}

export const delOrder = (payload) => (dispatch) => {
    dispatch({
        api: deleteOrder,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload,
    }).then(() => {
        toast.success("Order deleted successfully!");
    }).catch(() => {
        toast.error("Error delete order!");
    })
}
export const editOrders = (payload) => (dispatch) => {
    dispatch({
        api: editOrder,
        types: [
            types.REQUEST_START,
            types.REQUEST_SUCCESS,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        dispatch(getOrder())
        toast.success(res);
    });
}

export const getOrderFindByUser = (payload) => (dispatch) => {
    dispatch({
        api: findByUser,
        types: [
            types.REQUEST_START,
            types.GET_ORDER_LIST,
            types.REQUEST_ERROR
        ],
        data: payload
    });
}

//finish
export const addAttachmentAction = (payload) => (dispatch) => {
    dispatch({
        api: addAttachment,
        types: [
            types.REQUEST_START,
            types.GET_ATTACHMENT_ID,
            types.REQUEST_ERROR
        ],
        data: payload
    }).then(res => {
        console.log(res)
        dispatch({
            type: 'updateState',
            payload: {
                attachmentId: res.payload,
            }
        });
        toast.success("Attachment saved successfully!");
    }).catch((err) => {
        console.log(err)
        toast.error("Error saving attachment!");
    });
}
