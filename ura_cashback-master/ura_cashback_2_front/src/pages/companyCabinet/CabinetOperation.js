import React, {Component} from 'react';
import {Input, Table} from "reactstrap";
import {connect} from "react-redux";
import CompanySidebar from "./CompanySidebar";
import {companyOrder} from "../../redux/actions/AppAction";
import Navbar from "../clint/navbar/Navbar";
import "./cabinet.css"

class CabinetOperation extends Component {

    componentDidMount() {
        this.props.dispatch(companyOrder(
            sessionStorage.getItem("companyId")
        ));
    }


    render() {

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "rgb(232, 231, 231)";

        const {cabinetOrder, search, dispatch, size, page} = this.props;


        const paginate = (number) => {
            dispatch({
                type: "updateState",
                payload: {
                    page: number
                }
            })
        }

        //Search
        const set = (item) => {
            const lowerCase = item.target.value.toLowerCase();
            dispatch({
                type: "updateState",
                payload: {search: lowerCase}
            })
        }

        const filter = cabinetOrder && cabinetOrder.filter((el) => {
            if (search === '') {
                return el;
            } else {
                return el.admin.firstName.toLowerCase().includes(search)
            }
        })

        const indexOfLasPost = page * size;
        const indexOfFirstPosts = indexOfLasPost - size;
        const currentPosts = filter && filter.slice(indexOfFirstPosts, indexOfLasPost);

        const kassirName = [];
        for (let i = 1; i <= Math.ceil(cabinetOrder && cabinetOrder.length / size); i++) {
            kassirName.push(i);
        }

        return (
            <div>
                <Navbar/>
                <CompanySidebar/>
                <div className="searchOperation">
                    <Input type="text" onChange={(item) => set(item)} placeholder="Enter kassir name"/>
                    <i className="pi pi-search searchIconcaOperation"/>
                </div>
                <div className="ms-5 me-5 mt-5 orderTable">
                    <Table>
                        <thead>
                        <tr>
                            <th>Kassir</th>
                            <th>Date</th>
                            <th>Client</th>
                            <th>Cash price</th>
                            <th>Cashback</th>
                        </tr>
                        </thead>
                        {currentPosts &&
                            currentPosts.map((item, i) =>
                                <tbody key={i}>
                                <tr>
                                    <td>{item.admin.firstName} {item.admin.lastName}</td>
                                    <td>{item.date}</td>
                                    <td>{item.client.firstName} {item.client.lastName}</td>
                                    <td>{item.cashPrice}</td>
                                    <td>{item.cashback}</td>
                                </tr>
                                </tbody>
                            )
                        }
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


            </div>
        );
    }
}

CabinetOperation.propTypes = {};

export default connect(
    ({app: {companyInfo, cabinetOrder, search, dispatch, page, size}}) =>
        ({companyInfo, cabinetOrder, search, dispatch, page, size}))
(CabinetOperation);
