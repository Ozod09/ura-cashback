import React, {Component} from 'react';
import cashbackLogo from "../order/loginPage/image/logo.png";
import registerFoto from "../userAdmin/registerFoto.png";
import {Button, Input} from "reactstrap";
import {loginCompany} from "../../../redux/actions/AppAction";
import {connect} from "react-redux";
import './company.css'
import CabinetHome from "../../companyCabinet/CabinetHome";

class LoginCompany extends Component {


    state = {
        openPassword: false,
        resRegex: false
    }

    render() {
        document.body.style.marginLeft = "3.7%";
        // document.body.style.backgroundColor = "rgb(231,230,230)";
        document.body.style.backgroundColor = "white";
        const {dispatch, openLogin} = this.props;

        const flag = /^(?=.*[0-9]).{8,}$/;
        const regex = new RegExp(flag);

        const login = () => {
            let password = document.getElementById("password").value;
            let phoneNumber = document.getElementById("phoneNumber").value;
            let obj = {phoneNumber, password};
            console.log(obj);
            this.props.dispatch(loginCompany(obj));
            sessionStorage.setItem('PhoneNumber', phoneNumber)
            sessionStorage.setItem('Password', password)
        }

        const password = () => {
            this.setState({openPassword: !this.state.openPassword})
        }

        return (
            <div>
                {openLogin ?
                    <CabinetHome/> :
                    <div className="row home m-0">
                        <div className='col-6'>
                            <div className="m-0">
                                <img className="img1" src={cashbackLogo} alt="not"/>
                                <a className="regCom" href="/#">Register</a>
                            </div>
                            <img className="img2" src={registerFoto} alt="not"/>
                            <h3 className="mt-5">Savdolaringizni istalgan vaqtda kuzatib boring,</h3>
                            <h3 className="mb-5">Avvalgidan ko'ra osonroq va samaraliroq</h3>
                        </div>
                        <div className="col-6 loginCom">
                            <div className="row loginCompany">
                                <h2>Kirish</h2>
                                <div className="col-10 pe-0">
                                    <Input className="mb-5" type="text" id="phoneNumber" name="phoneNumber"
                                           placeholder="Phone number"
                                           required/>
                                    <Input className="mb-5" type={this.state.openPassword ? "text" : "password"}
                                           id="password" name="password" placeholder="Password"
                                           required/>
                                    {this.state.resRegex ?
                                        <p style={{color: "red"}}>Password error 0-9 password length = 8</p> : ""}
                                </div>
                                <div className="col-2">
                                    <ul>
                                        <li className="row iconcaCom1"><i className="pi pi-phone"/></li>
                                        <li className="row iconcaCom2"
                                            onClick={() => password()}>{this.state.openPassword ?
                                            <i className="pi pi-eye-slash"/> : <i className="pi pi-eye"/>}</li>
                                    </ul>
                                </div>
                            </div>
                            <Button color="info" type="submit" outline onClick={() => login()}>Next</Button>
                        </div>
                    </div>
                }
            </div>
        );
    }
}

LoginCompany.propTypes = {};

export default connect(
    ({app: {dispatch, openLogin}}) =>
        ({dispatch, openLogin}))
(LoginCompany);