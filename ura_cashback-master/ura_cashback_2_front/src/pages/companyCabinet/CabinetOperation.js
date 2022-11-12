import React, {Component} from 'react';
import {Button, Col, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table} from "reactstrap";
import {connect} from "react-redux";
import CompanySidebar from "./CompanySidebar";
import {loginCompany} from "../../redux/actions/AppAction";

class CabinetOperation extends Component {

    componentDidMount() {
        this.props.dispatch(loginCompany({
            password: sessionStorage.getItem("Password"),
            phoneNumber: sessionStorage.getItem("PhoneNumber")
        }))
    }

    state = {
        infoModal: false,
        currentUserOrder: {},
        admin: {}
    }

    render() {

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "white";

        const { companyInfo} = this.props;


        const infoModal = (admin, client) => {
            this.setState({currentUserOrder: client});
            this.setState({admin: admin})
            openInfoModal();
        }

        const openInfoModal = () => {
            this.setState({infoModal: !this.state.infoModal});
        }

        // const openDeleteModal = (item) => {
        //     // this.props.dispatch(delOrder(item.id));
        //     dispatch({
        //         type: 'updateState',
        //         payload:{
        //             deleteShowModal: !deleteShowModal
        //         }
        //     })
        // };
        //
        // const deleteOrders = (item) => {
        //     console.log(item.id)
        //     this.props.dispatch(delOrder(item));
        // }

        return (
            <div>
               <CompanySidebar/>
                <div className="ms-5 me-5 mt-5">
                    <Table>
                        <thead>
                        <tr>
                            <th>Kassir</th>
                            <th>Date</th>
                            <th>Client</th>
                            <th>Cash price</th>
                            <th>Cashback</th>
                            <th colSpan="1">Action</th>
                        </tr>
                        </thead>
                        {companyInfo.orders != null &&
                            companyInfo.orders.map((item, i) =>
                                <tbody key={i}>
                                <tr>
                                    <td>{item.admin.firstName} {item.admin.lastName}</td>
                                    <td>{item.admin.createdAt}</td>
                                    <td>{item.client.firstName} {item.client.lastName}</td>
                                    <td>{item.cash_price}</td>
                                    <td>{item.cashback}</td>
                                    <td><Button color="primary" outline
                                                onClick={() => infoModal(item.admin, item.client)}>full info</Button></td>
                                </tr>
                                </tbody>
                            )
                        }
                    </Table>

                    <Offcanvas
                        isOpen={this.state.infoModal}
                        direction="end" style={{width: "40%"}}
                        toggle={openInfoModal}
                    >
                        <OffcanvasHeader className="d-block">
                            <h3 className="text-center">Full info</h3>
                            <hr/>
                        </OffcanvasHeader>
                        <OffcanvasBody>
                            <div>
                                <Row>
                                    <Col className="border-end">
                                        {this.state.admin.id &&
                                            <div className="ms-3">
                                                <h4 className="text-center mb-3">Admin</h4>
                                                <h5>firstName: {this.state.admin.firstName}</h5>
                                                <h5>lastName: {this.state.admin.lastName}</h5>
                                                <h5>p-Number: {this.state.admin.phoneNumber}</h5>
                                                <h5>email: {this.state.admin.email}</h5>
                                            </div>
                                        }
                                    </Col>
                                    <Col>
                                        {this.state.currentUserOrder.id &&
                                            <div className="ms-3">
                                                <h4 className="text-center mb-3">Client</h4>
                                                <h5>firstName: {this.state.currentUserOrder.firstName}</h5>
                                                <h5>lastName: {this.state.currentUserOrder.lastName}</h5>
                                                <h5>p-Number: {this.state.currentUserOrder.phoneNumber}</h5>
                                                <h5>email: {this.state.currentUserOrder.email}</h5>
                                                <h5>cashback: {this.state.currentUserOrder.salary}</h5>
                                            </div>
                                        }
                                    </Col>
                                </Row>
                            </div>
                        </OffcanvasBody>
                    </Offcanvas>

                    {/*<Modal isOpen={deleteShowModal} toggle={() => openDeleteModal("")}>*/}
                    {/*    <ModalHeader> Delete operation </ModalHeader>*/}
                    {/*    <ModalFooter>*/}
                    {/*        <Button color="success" outline onClick={() => openDeleteModal()}>Cancel</Button>*/}
                    {/*        <Button color="danger" outline onClick={deleteOrders}>Delete</Button>*/}
                    {/*    </ModalFooter>*/}
                    {/*</Modal>*/}
                </div>
            </div>
        );
    }
}

CabinetOperation.propTypes = {};

export default connect(
    ({app: {companyInfo}}) =>
        ({companyInfo}))
(CabinetOperation);
