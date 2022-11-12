import React, {Component} from 'react';
import {connect} from "react-redux";
import CompanySidebar from "./CompanySidebar";
import {Button, Input, Modal, ModalBody, ModalFooter, ModalHeader, Table} from "reactstrap";
import add from '../companyCabinet/img/add2.png';
import edit from '../companyCabinet/img/edit2.png';
import delit from '../companyCabinet/img/delete2.png';
import './cabinet.css'
import {loginCompany, removeUser, saveCompanyKassa} from "../../redux/actions/AppAction";
import '../admin/userAdmin/auth.css';


class CompanyKassa extends Component {

    componentDidMount() {
        this.props.dispatch(loginCompany({
            password: sessionStorage.getItem("Password"),
            phoneNumber: sessionStorage.getItem("PhoneNumber")
        }))
    }

    state={
        openPassword:false,
        openPrePassword:false,
        resRegex:false,
        openModal:false,
        deleteModal:false,
        kassaId: '',
        items:[
            {id: 1 ,name: "salom1"},
            {id: 2,name: "salom2"},
            {id: 3,name: 'salom3'}
        ]
    }

    render() {

        const getCompany = () => {
            this.props.dispatch(loginCompany({
                password: sessionStorage.getItem("Password"),
                phoneNumber: sessionStorage.getItem("PhoneNumber")
            }))
        }
        const openModal = ()=>{
            this.setState({openModal: !this.state.openModal});
        }

        const deleteModal = ()=>{
            this.setState({deleteModal: !this.state.deleteModal});
        }

        const password = ()=>{
            this.setState({openPassword: !this.state.openPassword})
        }

        const prePassword = ()=>{
            this.setState({openPrePassword: !this.state.openPrePassword})
        }

        const {companyInfo} = this.props;






        const flag = /^(?=.*[0-9]).{8,}$/;
        const regex = new RegExp(flag);

        const registerCompanyKassr = ()=>{
            const password = document.getElementById("password").value;
            const prePassword = document.getElementById("prePassword").value;

            if(password.match(regex) !== null && prePassword.match(regex) !== null){
                const firstName = document.getElementById("firstName").value;
                const lastName = document.getElementById("lastName").value;
                const phoneNumber = document.getElementById("phoneNumber").value;
                const email = document.getElementById("email").value;
                if(this.state.kassaId !== null){
                    let obj = {id: this.state.kassaId, firstName,lastName,phoneNumber,email,password,prePassword, companyId: companyInfo.id};
                    this.props.dispatch(saveCompanyKassa(obj))
                    this.setState({openModal: false})
                    window.location.reload();
                }else {
                    let obj = {firstName,lastName,phoneNumber,email,password,prePassword, companyId: companyInfo.id};
                    this.props.dispatch(saveCompanyKassa(obj))
                    this.setState({openModal: false})
                }
            }else {
                this.setState({resRegex: !this.state.resRegex})
            }
            getCompany()
        }

        const deleteCompanyKassr = ()=>{
            console.log(this.state.kassaId, "kassa id")
            this.setState({deleteModal: !this.state.deleteModal})
            this.props.dispatch(removeUser(this.state.kassaId));
            window.location.reload();
            getCompany()
        }






        return (
            <div>
                <CompanySidebar/>
                <div className="container">
                    <Button className="mt-5 compButton" onClick={()=>openModal()}><img src={add} /></Button>
                <Table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Password</th>
                        <th colSpan="2">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {companyInfo.kassa &&
                        companyInfo.kassa.map((item,i)=>
                    <tr key={i}>
                        <td>{i + 1}</td>
                        <td>{item.firstName}</td>
                        <td>{item.lastName}</td>
                        <td>{item.email}</td>
                        <td>{item.phoneNumber}</td>
                        <td>{item.password}</td>
                        <td><img  onClick={()=> {openModal(); this.setState({kassaId: item.id})}} src={edit}/></td>
                        <td><img  onClick={()=> {deleteModal(); this.setState({kassaId: item.id})}} src={delit}/></td>
                    </tr>
                    )}
                    </tbody>
                </Table>
                </div>

                <Modal isOpen={this.state.openModal}>
                    <ModalHeader>{this.state.kassaId ? "Edit Kassir" : "Add Kassir"}</ModalHeader>
                    <ModalBody>
                        <Input className="mb-2" type="text" id="firstName" placeholder="First name"
                               required/>
                        <Input className="mb-2" type="text" id="lastName" placeholder="Last name" required/>
                        <Input className="mb-2" type="text" id="phoneNumber" placeholder="Phone number"
                               required/>
                        <Input className="mb-2" type="email" id="email" placeholder="Email" required/>
                        <Input className="mb-4" type={this.state.openPassword ? "text": "password"} id="password" placeholder="Password" required/>
                        <li className="row icon1" onClick={()=> password()}>
                            {this.state.openPassword ? <i className="pi pi-eye-slash"/> : <i className="pi pi-eye"/>}</li>
                        <Input className="mb-2" style={{marginTop:"20px"}} type={this.state.openPrePassword ? "text" : "password"} id="prePassword" placeholder="Pre password" required/>
                        <li className="row icon2" onClick={()=> prePassword()}>
                            {this.state.openPrePassword ? <i className="pi pi-eye-slash" /> : <i className="pi pi-eye"/> }</li>
                        {this.state.resRegex ? <p style={{color:"red",marginTop:"10px"}}>Password error 0-9 password length = 8</p> : ""}
                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={openModal}>Close</Button>
                        <Button color="success" onClick={registerCompanyKassr}>Save</Button>
                    </ModalFooter>
                </Modal>


                <Modal isOpen={this.state.deleteModal}>
                    <ModalHeader>Kassirni uchirish</ModalHeader>
                    <ModalFooter>
                        <Button color="secondary" onClick={deleteModal}>Close</Button>
                        <Button color="danger" onClick={deleteCompanyKassr}>Delete</Button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

CompanyKassa.propTypes = {};

export default connect(
    ({app:{companyInfo}})=>
        ({companyInfo}))
(CompanyKassa);