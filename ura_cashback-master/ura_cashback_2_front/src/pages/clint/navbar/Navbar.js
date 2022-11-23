import React from 'react';
import rasm from "../../companyCabinet/image.jpg";
import  "./navbar.css";
import {connect} from "react-redux";


function Navbar(props) {

    const {companyInfo} = props;


    return (
        <div className="row" style={{marginRight:"0"}}>
            <div id="navbar" >
            <h5>Salom {companyInfo.user.firstName} {companyInfo.user.lastName}, sizga katta sotuvlar tilayman! <img  src={rasm}/></h5>
                <li className="signOut" ><i  className="pi pi-sign-out"/></li>
            </div>
        </div>
    );
}
Navbar.prototype = {};
export default connect(
    ({app:{dispatch,companyInfo}})=>
        ({dispatch,companyInfo}))
(Navbar);