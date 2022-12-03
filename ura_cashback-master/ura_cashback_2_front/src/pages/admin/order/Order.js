import React, {Component} from 'react';
import "./orderC.css"
import {Button, Input, InputGroup, Table} from "reactstrap";
import {getOrder} from "../../../redux/actions/AppAction";
import {connect} from "react-redux";
import Sidebar from "../../clint/navbar/Sidebar";

class Order extends Component {
    componentDidMount() {
        this.props.dispatch(getOrder())
    }

    state = {
        currentUserOrder: {},
    }

    render() {

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "rgba(231, 229, 229, 0.73)"

        const {orders, dispatch, size, page, search} = this.props;

        const paginate = (number) => {
            dispatch({
                type: "updateState",
                payload: {
                    page: number
                }
            })
        }

        //Search
        const clickSearch = () => {
            dispatch({
                type: "updateState",
                payload: {
                    search: document.getElementById("search").value.toLowerCase()
                }
            })
        }

        const filter = orders.filter((el) => {
            if (search === '') {
                return el;
            } else {
                return el.company.name.toLowerCase().includes(search)
            }
        })

        const indexOfLasPost = page * size;
        const indexOfFirstPosts = indexOfLasPost - size;
        const currentPosts = filter.slice(indexOfFirstPosts, indexOfLasPost);

        const companyName = [];
        for (let i = 1; i <= Math.ceil(orders.length / size); i++) {
            companyName.push(i);
        }

        return (
            <div className="superAdminCashback">
                <Sidebar/>
                <div className="searchSuperAdminCashback">
                    <InputGroup>
                        <Input type="text" id="search" placeholder="Enter company name"/>
                        <Button color="primary" type="button"><i
                            className="pi pi-search searchIconcaSuperAdmin" onClick={clickSearch}/></Button>
                    </InputGroup>
                </div>
                <div className="me-5 ms-5 superAdminTable">
                    <Table>
                        <thead>
                        <tr>
                            <th>Comapny</th>
                            <th>Kassir</th>
                            <th>Cash price</th>
                            <th>Cashback</th>
                            <th>Client</th>
                        </tr>
                        </thead>
                        {currentPosts.map((item, i) =>
                            <tbody key={i}>
                            <tr>
                                <td>{item.company && item.company.name}</td>
                                <td>{item.admin && item.admin.firstName} {item.admin && item.admin.lastName}</td>
                                <td>{item.cashPrice}</td>
                                <td>{item.cashback}</td>
                                <td>{item.client && item.client.firstName} {item.client && item.client.lastName}</td>
                            </tr>
                            </tbody>
                        )
                        }
                    </Table>
                </div>


                <nav>
                    <ul className="pagination">
                        {companyName.map((number, i) =>
                            <li key={i} className="page-item">
                                <a onClick={() => paginate(number)} className="page-link">{number}</a>
                            </li>
                        )}
                    </ul>
                </nav>


            </div>
        );
    }
}

Order.propTypes = {};

export default connect(
    ({app: {orders, dispatch, size, page, search}}) =>
        ({orders, dispatch, size, page, search}))
(Order);
