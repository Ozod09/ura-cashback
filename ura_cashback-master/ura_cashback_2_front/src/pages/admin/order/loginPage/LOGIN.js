import React from 'react';
import logo from "./image/img_1.png";
import {Button, Input} from "reactstrap";

function Login({onSubmit}) {

    function enterButton() {
        document.getElementById("password").addEventListener("keyup", function (event) {
            if (event.keyCode === 13) document.getElementById("next").click();
        });
    }

    return (
        <><div className="login">
            <div className="login-page">
                <h4 className="text-center m-0">Kassir</h4>
                <div className="big-logo-box">
                    <img src={logo} alt="cashback" className="kasserLogo"/>
                </div>
                <div className="login-form inputs">
                    <div className="login-form-container">
                        <Input type="text" placeholder="Номер телефона" id="phoneNumber"
                               className="login-form-input"/>
                        <Input type="text" placeholder="Пароль" id="password"
                               className="login-form-input mt-4"/>
                    </div>
                    <h3 style={{color: "red"}} id="error"> </h3>
                    <Button
                        className="btn btn-primary form-btn-login w-100"
                        type="button" id="next"
                        onClick={onSubmit} onChange={enterButton}
                    >
                        Войти
                    </Button>
                </div>
            </div>
        </div></>
    );
}

export default Login;