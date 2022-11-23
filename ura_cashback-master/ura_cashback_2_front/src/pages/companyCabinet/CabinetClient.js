import React, {Component} from 'react';
import CompanySidebar from "./CompanySidebar";
import {connect} from "react-redux";
import {Input, Table} from "reactstrap";
import {loginCompany} from "../../redux/actions/AppAction";
import Navbar from "../clint/navbar/Navbar";
import './cabinet.css'

class CabinetClient extends Component {

    componentDidMount() {
        this.props.dispatch(loginCompany({
            password: sessionStorage.getItem("Password"),
            phoneNumber: sessionStorage.getItem("PhoneNumber")
        }))
    }

    render() {

        const {companyInfo, search, dispatch ,size, page} = this.props;

        document.body.style.marginLeft = "3.7%";
        document.body.style.backgroundColor = "white";

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

        const filter = companyInfo.clint.filter((el)=>{
            if(search === ''){
                return el;
            }else {
                return el.firstName.toLowerCase().includes(search)
            }
        })

        const indexOfLasPost = page * size;
        const indexOfFirstPosts = indexOfLasPost - size;
        const currentPosts = filter.slice(indexOfFirstPosts,indexOfLasPost);

        const clientName = [];
        for (let i = 1; i <= Math.ceil(companyInfo.clint.length / size); i++) {
            clientName.push(i);
        }


        return (
            <div id="cabClient">
                <Navbar/>
                <CompanySidebar/>
                <div className="searchClient">
                    <Input type="text" onChange={(item)=> set(item)} placeholder="Enter kassir name"/>
                    <i className="pi pi-search searchIconcaClient"/>
                </div>
                <div className="ms-5 me-5 mt-5 clientTable">
                    <Table>
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Last Name</th>
                            <th>Phone</th>
                            <th>Email</th>
                            <th>Salary</th>
                            <th>Password</th>
                        </tr>
                        </thead>
                        {currentPosts.map((item, i) =>
                                <tbody key={i}>
                                <tr>
                                    <td>{i + 1}</td>
                                    <td>{item.firstName}</td>
                                    <td>{item.lastName}</td>
                                    <td>{item.phoneNumber}</td>
                                    <td>{item.email}</td>
                                    <td>{item.salary}</td>
                                    <td>{item.password}</td>
                                </tr>
                                </tbody>
                            )
                        }
                    </Table>
                </div>


                <nav>
                    <ul className="pagination">
                        {clientName.map((number, i) =>
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

CabinetClient.propTypes = {};

export default connect(
    ({app: {companyInfo, search, dispatch ,size, page}}) =>
    ({ companyInfo, search, dispatch ,size, page}))
(CabinetClient);