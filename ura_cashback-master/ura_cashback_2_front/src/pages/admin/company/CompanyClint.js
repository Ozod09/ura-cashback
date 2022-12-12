import React, {Component} from 'react';
import {addAttachmentAction, saveCompany} from "../../../redux/actions/AppAction";
import {Button, Input} from "reactstrap";
import {connect} from "react-redux";
import "./company.css";
import registerCom from '../company/img/IMG_20221021_070117_004.jpg';
import logoCash from '../company/img/img_1.png';
import registerCom1 from "../company/img/Rectangle.png"
import done from "../company/img/photo_2022-10-20_11-19-28.jpg"
import {api} from "../../../api/api";

class CompanyRegister extends Component {
    render() {
        const {currentCompany, currentUser, attachmentId, active} = this.props;
        const sendPhoto = (item) => {
            let obj = new FormData();
            obj.append("request", item.target.files[0]);
            this.props.dispatch(addAttachmentAction(obj));
        }

        const addCompany = () => {
            let name = document.getElementById("name").value;
            let bio = document.getElementById("bio").value;
            let description = document.getElementById("description").value;
            let clientPercentage = document.getElementById("clintPercentage").value;
            console.log(clientPercentage);
            let obj = {
                name,
                bio,
                description,
                clientPercentage,
                attachmentId: attachmentId,
                userId: currentUser,
                active
            };
            this.props.dispatch(saveCompany(obj));
        }
        return (
            <>
                <div className="main-div">
                    <h1 className="informationComp">Company xaqida malumot</h1>
                    <h1 className="registerCom">Register company</h1>
                    <h1 className="logotipCom">Logotip company</h1>
                    <img className="img"
                         data-aos-duration="1000"
                         data-aos-easing="ease-in-back" src={registerCom1} alt="Loading..."/>
                    <h1 className="malumot">Company xaqida malumot</h1>
                    <img className="compImg1"
                         data-aos-duration="1000"
                         data-aos-easing="ease-in-back" src={logoCash} alt="Loading..."/>
                    <img className="oval"
                         data-aos-duration="1000"
                         data-aos-easing="ease-in-back" src={done} alt="Loading..."/>
                    <Input className="companyImg" type="file"
                           src={"http://localhost/api/attachment/getFile" + attachmentId} multiple
                           onChange={(item) => sendPhoto(item)}
                           required accept="image/*"/>
                    <img className="compLogo" data-aos-duration="1000"
                         src={api.getAttachment + attachmentId} data-aos-easing="ease-in-back" alt="Loading..."/>
                    <Input className='company-name' name="name" id="name" type='text'
                           defaultValue={currentCompany ? currentCompany.name : ""} placeholder='Enter company name'
                           required/>
                    <Input className='company-bio' name="bio" id="bio" type='text'
                           defaultValue={currentCompany ? currentCompany.bio : ""} placeholder='Enter company bio'
                           required/>
                    <Input className='company-description' name="description" id="description" type='text'
                           defaultValue={currentCompany ? currentCompany.description : ""}
                           placeholder='Enter company description'
                           required/>
                    <Input className='company-cPercentage' name="clintPercentage" id="clintPercentage" type='number'
                           defaultValue={currentCompany ? currentCompany.clintPercentage : ""}
                           placeholder='Enter company clintPercentage'
                           required/>
                    <Button className="registerComp" color='primary' type="submit"
                            onClick={addCompany}> <a href={'http://localhost:3000/company/login'}>✔Register</a></Button>
                    <img className="compImg2"
                         data-aos-duration="1000"
                         data-aos-easing="ease-in-back" src={registerCom} alt="Loading..."/>

                </div>
            </>
        );
    }
}

CompanyRegister.propTypes = {};

export default connect(
    ({app: {currentCompany, currentUser, attachmentId, active}}) =>
        ({currentCompany, currentUser, attachmentId, active}))
(CompanyRegister);