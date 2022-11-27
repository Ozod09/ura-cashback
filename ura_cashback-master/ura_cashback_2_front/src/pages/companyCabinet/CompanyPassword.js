import React, {useState} from 'react';
import {Button, Input} from "reactstrap";
import Navbar from "../clint/navbar/Navbar";
import CompanySidebar from "./CompanySidebar";
import './cabinet.css'
import {connect} from "react-redux";
import {editCompanyAdminPassword} from "../../redux/actions/AppAction";

function CompanyPassword(props) {

    const {dispatch, companyInfo} = props;

    const [oldPassword, setOldPassword] = useState(false);
    const [password, setPassword] = useState(false);
    const [prePassword, setPrePassword] = useState(false);

    const oldPasswordClick = ()=>{
        setOldPassword(!oldPassword);
    }
    const passwordClick = () =>{
        setPassword(!password);
    }
    const prePasswordClick = ()=>{
        setPrePassword(!prePassword)
    }


    const editPassword = ()=>{
        const oldPassword = document.getElementById('oldPassword').value;
        const password = document.getElementById("password").value;
        const prePassword = document.getElementById('prePassword').value;
        dispatch(editCompanyAdminPassword({joriyPassword: oldPassword, prePassword,password,userId:companyInfo.user.id}));
    }


    return (
        <div className="cabinetPassword">
            <Navbar/>
            <CompanySidebar/>
            <div className="cabPassword">
                <Input type={oldPassword ? "text" : "password"} id="oldPassword" placeholder="Enter old password"/>
                <li className="comOldPassword" onClick={()=> oldPasswordClick()}>{oldPassword ? <i className=" pi pi-eye-slash"/> :<i className="pi pi-eye"/>}</li>
                <Input type={password ? "text" : "password"} id="password" placeholder="Enter new password"/>
                <li className="comPassword" onClick={()=> passwordClick()}>{password ? <i className=" pi pi-eye-slash"/> :<i className="pi pi-eye"/>}</li>
                <Input type={prePassword ? "text" : "password"} id="prePassword" placeholder="Enter new pre password"/>
                <li className="comPrePassword" onClick={()=> prePasswordClick()}>{prePassword ? <i className=" pi pi-eye-slash"/> :<i className="pi pi-eye"/>}</li>

                <Button type='submit' onClick={()=> editPassword()}>Save</Button>
            </div>
        </div>
    );
}
CompanyPassword.prototype = {};
export default connect(
    ({app:{dispatch,companyInfo}})=>
        ({dispatch,companyInfo}))
(CompanyPassword);