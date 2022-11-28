import React from 'react';
import order from '../clint/navbar/sidebarImg/order.png';
import '../clint/navbar/sidebar.scss';
import '../clint/navbar/sidebarImg/sidebar2.css';
import {connect} from "react-redux";
import {Link} from "react-router-dom";
import {api} from "../../api/api";
import clients from './img/clients.png';
import programm from './img/accountant.png'
import calculator from './img/calculator .png'
import parol from './img/parol.png'


function Sidebar(props) {

    const {currentUser} = props;

    const images = [
        {img:calculator,name:'operation', url: '/company/operation'},
        {img: order, name: "cashback", url: "/cabinetOrder"},
        {img: clients, name: "Mijozlar", url: "/cabinetClient"},
        {img: programm, name: "Xodimlar", url: "/company/kassa"},
        // {img: settings, name: "Sozlamalar", url: "/company/settings"},
        {img: parol, name: "Parolni o'zgartirish", url: "/company/password"},
    ]

    return (
        <div>
            <div className="sidebar">
                <div className="offcanvas offcanvas-start show">
                    <div className="offcanvas-body mt-5">
                        {currentUser === 0 ?
                            <div>
                                <img src={api.getAttachment + currentUser.attachment.id} alt="user"/>
                                <h5>{currentUser.firstName}</h5>
                            </div>
                            :
                            <div>

                            </div>
                        }
                        <div className="sidebar-overflow">
                            {images.map((item, i) =>
                                <div key={i}>
                                    <Link to={item.url}>
                                        <img className="float-lg-start mb-4" src={item.img} alt="not"/>
                                        <h5 className="mb-5">{item.name}</h5>
                                    </Link>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

Sidebar.propTypes = {};

export default connect(
    ({app: {currentUser}}) =>
        ({currentUser}))
(Sidebar);
