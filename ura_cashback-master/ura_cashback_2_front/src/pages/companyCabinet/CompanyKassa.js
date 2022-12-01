import React, {Component} from 'react';
import {connect} from "react-redux";
import CompanySidebar from "./CompanySidebar";
import {Button, Input, Modal, ModalBody, ModalFooter, ModalHeader, Table} from "reactstrap";
import add from '../companyCabinet/img/add2.png';
import edit from '../companyCabinet/img/edit2.png';
import deleteImg from '../companyCabinet/img/delete2.png';
import './cabinet.css'
import {companyKassa, removeUser, saveCompanyKassa} from "../../redux/actions/AppAction";
import Navbar from "../clint/navbar/Navbar";


class CompanyKassa extends Component {


    componentDidMount() {
        this.props.dispatch(companyKassa(sessionStorage.getItem("companyId")));
    }

    state = {
        openPassword: false,
        openPrePassword: false,
        resRegex: false,
        openModal: false,
        deleteModal: false,
        kassaId: '',
    }

    render() {

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "rgb(232, 231, 231)";

        const getCompany = () => {
            this.props.dispatch(companyKassa(sessionStorage.getItem("companyId")))
        }
        const openModal = () => {
            this.setState({openModal: !this.state.openModal});
        }

        const deleteModal = () => {
            this.setState({deleteModal: !this.state.deleteModal});
        }

        const password = () => {
            this.setState({openPassword: !this.state.openPassword})
        }

        const prePassword = () => {
            this.setState({openPrePassword: !this.state.openPrePassword})
        }

        const {companyInfo, cabinetKassa, search, dispatch, size, page} = this.props;


        const paginate = (number) => {
            dispatch({
                type: "updateState",
                payload: {
                    page: number
                }
            })
        }

        const flag = /^(?=.*[0-9]).{8,}$/;
        const regex = new RegExp(flag);

        const registerCompanyKassr = () => {
            const password = document.getElementById("password").value;
            const prePassword = document.getElementById("prePassword").value;

            if (password.match(regex) !== null && prePassword.match(regex) !== null) {
                const firstName = document.getElementById("firstName").value;
                const lastName = document.getElementById("lastName").value;
                const phoneNumber = document.getElementById("phoneNumber").value;
                const email = document.getElementById("email").value;
                if (this.state.kassaId !== null) {
                    let obj = {
                        id: this.state.kassaId,
                        firstName,
                        lastName,
                        phoneNumber,
                        email,
                        password,
                        prePassword,
                        companyId: companyInfo.id
                    };
                    this.props.dispatch(saveCompanyKassa(obj))
                    this.setState({openModal: false})
                    window.location.reload();
                } else {
                    let obj = {
                        firstName,
                        lastName,
                        phoneNumber,
                        email,
                        password,
                        prePassword,
                        companyId: companyInfo.id
                    };
                    this.props.dispatch(saveCompanyKassa(obj))
                    this.setState({openModal: false})
                }
            } else {
                this.setState({resRegex: !this.state.resRegex})
            }
            getCompany()
        }

        const deleteCompanyKassr = () => {
            this.setState({deleteModal: !this.state.deleteModal})
            this.props.dispatch(removeUser(this.state.kassaId));
            window.location.reload();
            getCompany()
        }


        //Search
        const set = (item) => {
            const lowerCase = item.target.value.toLowerCase();
            dispatch({
                type: "updateState",
                payload: {
                    search: lowerCase
                }
            })
        }

        const filter = cabinetKassa && cabinetKassa.filter((el) => {
            console.log(search)
            if (search === '') {
                return el;
            } else {
                return el.firstName.toLowerCase().includes(search)
            }
        })

        const indexOfLasPost = page * size;
        const indexOfFirstPosts = indexOfLasPost - size;
        const currentPosts = filter && filter.slice(indexOfFirstPosts, indexOfLasPost);

        const kassirName = [];
        for (let i = 1; i <= Math.ceil(cabinetKassa && cabinetKassa.length / size); i++) {
            kassirName.push(i);
        }


        return (
            <div id="cabKassa">
                <Navbar/>
                <CompanySidebar/>
                <div className="searchKassa">
                    <Input type="text" onChange={(item) => set(item)} placeholder="Enter kassir name"/>
                    <i className="pi pi-search searchIconcaKassa"/>
                </div>
                <div className="ms-5 me-5 mt-5 kassaTable">
                    <Button className="mt-1 compButton" onClick={() => openModal()}><img src={add}/></Button>
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
                        {currentPosts &&
                            currentPosts.map((item, i) =>
                                <tr key={i}>
                                    <td>{i + 1}</td>
                                    <td>{item.firstName}</td>
                                    <td>{item.lastName}</td>
                                    <td>{item.email}</td>
                                    <td>{item.phoneNumber}</td>
                                    <td>{item.password}</td>
                                    <td><img onClick={() => {
                                        openModal();
                                        this.setState({kassaId: item.id})
                                    }} src={edit}/></td>
                                    <td><img onClick={() => {
                                        deleteModal();
                                        this.setState({kassaId: item.id})
                                    }} src={deleteImg}/></td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </div>

                <nav>
                    <ul className="pagination">
                        {kassirName.map((number, i) =>
                            <li key={i} className="page-item">
                                <a onClick={() => paginate(number)} className="page-link">{number}</a>
                            </li>
                        )}
                    </ul>
                </nav>

                <Modal isOpen={this.state.openModal}>
                    <ModalHeader>{this.state.kassaId ? "Edit Kassir" : "Add Kassir"}</ModalHeader>
                    <ModalBody>
                        <Input className="mb-2" type="text" id="firstName" placeholder="First name"
                               required/>
                        <Input className="mb-2" type="text" id="lastName" placeholder="Last name" required/>
                        <Input className="mb-2" type="text" id="phoneNumber" placeholder="Phone number"
                               required/>
                        <Input className="mb-2" type="email" id="email" placeholder="Email" required/>
                        <Input className="mb-4" type={this.state.openPassword ? "text" : "password"} id="password"
                               placeholder="Password" required/>
                        <li className="row icon1" onClick={() => password()}>
                            {this.state.openPassword ? <i className="pi pi-eye-slash"/> :
                                <i className="pi pi-eye"/>}</li>
                        <Input className="mb-2" style={{marginTop: "20px"}}
                               type={this.state.openPrePassword ? "text" : "password"} id="prePassword"
                               placeholder="Pre password" required/>
                        <li className="row icon2" onClick={() => prePassword()}>
                            {this.state.openPrePassword ? <i className="pi pi-eye-slash"/> :
                                <i className="pi pi-eye"/>}</li>
                        {this.state.resRegex ? <p style={{color: "red", marginTop: "10px"}}>Password error: </p> : ""}
                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={openModal}>Close</Button>
                        <Button color="success" onClick={registerCompanyKassr}>Save</Button>
                    </ModalFooter>
                </Modal>


                <Modal isOpen={this.state.deleteModal}>
                    <ModalHeader>Kassirni o'chirish</ModalHeader>
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
    ({app: {companyInfo, cabinetKassa, search, dispatch, size, page}}) =>
        ({companyInfo, cabinetKassa, search, dispatch, size, page}))
(CompanyKassa);