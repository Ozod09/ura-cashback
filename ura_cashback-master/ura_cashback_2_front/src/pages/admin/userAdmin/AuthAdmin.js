import React, {Component} from 'react';
import {Button, Input, InputGroup, Modal, ModalFooter, ModalHeader, Table} from "reactstrap";
import {connect} from "react-redux";
import {getUser, isActiveUser, removeUser} from "../../../redux/actions/AppAction";
import Sidebar from "../../clint/navbar/Sidebar";
import './auth.css';

class AuthAdmin extends Component {

    componentDidMount() {
        this.props.dispatch(getUser());
    }

    render() {

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "rgba(231, 229, 229, 0.73)"

        const {user, search, dispatch, currentUser, deleteShowModal, activeUser} = this.props;

        const paginate = (number) => {

        }

        const deleteModal = (item) => {
            dispatch({
                type: "updateState",
                payload: {
                    deleteShowModal: !deleteShowModal,
                    currentUser: item
                }
            });
        }

        const changeActive = () => {
            dispatch({
                type: "updateState",
                payload: {
                    activeUser: !activeUser,
                }
            });
        }

        const changeActiveUser = (item) => {
            if (item.id !== undefined) {
                this.props.dispatch(isActiveUser(item.id));
            }
        }

        const deleteUser = () => {
            this.props.dispatch(removeUser(currentUser));
            deleteModal("")
            dispatch({
                type: "updateState",
                payload: {
                    deleteShowModal: !deleteShowModal
                }
            });
        }

        const clickSearch = () => {
            console.log(document.getElementById("search").value)
        }

        const pageNumbers = [];
        pageNumbers.push(user.totalPages);

        return (
            <div className="">
                <Sidebar/>
                <div className="searchSuperAdminClient">
                    <InputGroup>
                        <Input type="text" id="search" placeholder="Enter phone number"/>
                        <Button color="primary" type="button" onClick={clickSearch}><i
                            className="pi pi-search searchIconcaSuperAdmin"/></Button>
                    </InputGroup>
                </div>
                <div className="ms-5 me-5 superAdminClientTable">
                    <Table>
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Password</th>
                            <th>Balance</th>
                            <th colSpan="2">Action</th>
                        </tr>
                        </thead>
                        {user.object &&
                            user.object.map((item, i) =>
                                <tbody key={i}>
                                <tr>
                                    <td>{i + 1}</td>
                                    <td>{item.firstName}</td>
                                    <td>{item.lastName}</td>
                                    <td>{item.email}</td>
                                    <td>{item.phoneNumber}</td>
                                    <td>{item.password}</td>
                                    <td>{item.salary}</td>
                                    <td><Button color="danger" outline onClick={() => deleteModal(item)}>Delete</Button>
                                    </td>
                                </tr>
                                </tbody>
                            )}
                    </Table>
                </div>

                <nav>
                    <ul className="pagination">
                        {pageNumbers.map((number, i) =>
                            <li key={i} className="page-item">
                                <a onClick={() => paginate(number)} className="page-link">{number}</a>
                            </li>
                        )}
                    </ul>
                </nav>

                <Modal isOpen={deleteShowModal}>
                    <ModalHeader>Delete User</ModalHeader>
                    <ModalFooter>
                        <Button color="secondary" outline onClick={() => deleteModal("")}>Cansel</Button>
                        <Button color="danger" outline onClick={deleteUser}>Delete</Button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

AuthAdmin.propTypes = {};

export default connect(
    ({app: {user, search, dispatch, currentUser, deleteShowModal, activeUser, pages}}) =>
        ({user, search, dispatch, currentUser, deleteShowModal, activeUser, pages}))
(AuthAdmin);

