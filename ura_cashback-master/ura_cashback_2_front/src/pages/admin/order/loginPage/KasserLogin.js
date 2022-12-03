import {connect} from "react-redux";
import "./style.scss";
import logo from "./image/logo.png";
import {Button, Input} from "reactstrap";
import {loginOrderAction} from "../../../../redux/actions/AppAction";
import Kassa from "./Kassa";
import Login from './LOGIN';

function KasserLogin({dispatch, showModal}) {

    const company = JSON.parse(localStorage.getItem("company"));


    const orderLogin = () => {
        const phoneNumber = document.getElementById("phoneNumber").value;
        const password = document.getElementById("password").value;
        dispatch(loginOrderAction({phoneNumber, password}));
        dispatch(loginOrderAction({phoneNumber, password, companyId: company && company.id}));
    }


    if (showModal) {
        return (
            <Kassa/>
        )
    }

    return (


        <Login onSubmit={() => orderLogin()}/>


    );
}

KasserLogin.propTypes = {};

export default connect(({app: {dispatch, currentUser, showModal}}) =>
    ({dispatch, currentUser, showModal}))
(KasserLogin);
