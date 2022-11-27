import React, {Component} from 'react';
import "./orderC.css"
import {Input, Table} from "reactstrap";
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
        document.body.style.backgroundColor = "white"

        const {orders, dispatch,size, page ,search } = this.props;


        console.log(orders)

        const paginate = (number) => {
            dispatch({
                type: "updateState",
                payload: {
                    page: number
                }
            })
        }

        //Search
        const set = (item)=>{
            const lowerCase = item.target.value.toLowerCase();
            dispatch({
                type:"updateState",
                payload:{
                    search:lowerCase
                }
            })
        }

        const filter = orders.filter((el)=>{
            if(search === ''){
                return el;
            }else {
                return el.company.name.toLowerCase().includes(search)
            }
        })

        const indexOfLasPost = page * size;
        const indexOfFirstPosts = indexOfLasPost - size;
        const currentPosts = filter.slice(indexOfFirstPosts,indexOfLasPost);

        const companyName = [];
        for (let i = 1; i <= Math.ceil(orders.length / size); i++) {
            companyName.push(i);
        }


        return (
            <div className="superAdminCashback">
                <Sidebar/>
                <div className="searchSuperAdminCashback">
                    <Input type="text" onChange={(item)=> set(item)}  placeholder="Enter company name"/>
                    <i className="pi pi-search searchIconcaSuperAdmin"/>
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
                                    <td>{item.company.name}</td>
                                    <td>{item.admin.firstName} {item.admin.lastName}</td>
                                    <td>{item.cash_price}</td>
                                    <td>{item.cashback}</td>
                                    <td>{item.client.firstName} {item.client.lastName}</td>
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
    ({app: {orders,dispatch,size, page ,search}}) =>
        ({orders,dispatch,size, page ,search}))
(Order);
