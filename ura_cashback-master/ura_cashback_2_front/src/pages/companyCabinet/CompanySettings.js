import React, {Component} from 'react';
import CompanySidebar from "./CompanySidebar";
import {addAttachmentAction, loginCompany, saveCompany} from "../../redux/actions/AppAction";
import registerCom1 from "../admin/company/img/Rectangle.png";
import {Button, Input} from "reactstrap";
import {api} from "../../api/api";
import registerCom from "../admin/company/img/img_1.png";
import {connect} from "react-redux";
import Navbar from "../clint/navbar/Navbar";
import "./settings.css"


class CompanySettings extends Component {

    render() {

        const {currentCompany, currentUser, attachmentId, active} = this.props;

        this.props.dispatch(loginCompany({
            password: sessionStorage.getItem("Password"),
            phoneNumber:sessionStorage.getItem("PhoneNumber")
        }))

        const sendPhoto = (item) => {
            let obj = new FormData();
            obj.append("file", item.target.files[0]);
            this.props.dispatch(addAttachmentAction(obj));
        }

        const addCompany = () => {
            let name = document.getElementById("name").value;
            let bio = document.getElementById("bio").value;
            let description = document.getElementById("description").value;
            let clintPercentage = document.getElementById("clintPercentage").value;
            let obj = {
                name,
                bio,
                description,
                clintPercentage,
                attachmentId,
                userId: currentUser,
                active
            };
            this.props.dispatch(saveCompany(obj));
        }


        return (
            <div id="cabSettings">
                <Navbar/>
                <CompanySidebar/>
                <div className="row" style={{marginRight:'0'}}>
                <div className="col-6 main-div1">
                    <Input className="companyImg" type="file" src={registerCom1} multiple
                           onChange={(item) => sendPhoto(item)}
                           required accept="image/*"/>
                    <img className="compLogo1" data-aos-duration="1000"
                         src={attachmentId ? api.getAttachment + attachmentId : ""} data-aos-easing="ease-in-back" alt="Loading..."/>
                    <Input className='company-name1 mt-4' name="name" id="name" type='text'
                           defaultValue={currentCompany ? currentCompany.name : ""} placeholder='Enter company name'
                           required/>
                    <Input className='company-bio1 mt-2' name="bio" id="bio" type='text'
                           defaultValue={currentCompany ? currentCompany.bio : ""} placeholder='Enter company bio'
                           required/>
                    <Input className='company-description1 mt-2' name="description" id="description" type='text'
                           defaultValue={currentCompany ? currentCompany.description : ""}
                           placeholder='Enter company description'
                           required/>
                    <Input className='company-cPercentage1 mt-2' name="clintPercentage" id="clintPercentage" type='number'
                           defaultValue={currentCompany ? currentCompany.clintPercentage : ""}
                           placeholder='Enter company clintPercentage'
                           required/>
                    <Button className="registerComp1 mt-2" color='primary' type="submit"
                            onClick={addCompany}>Register</Button>
                </div>
                <div className="col-6 compImg21">
                    <img
                         data-aos-duration="1000"
                         data-aos-easing="ease-in-back" src={registerCom} alt="Loading..."/>
                </div>
                </div>
            </div>
        );
    }
}

CompanySettings.propTypes = {};

export default connect(
    ({app: {currentCompany, currentUser, attachmentId, active}}) =>
        ({currentCompany, currentUser, attachmentId, active}))
(CompanySettings);