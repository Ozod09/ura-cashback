import React, {useEffect, useState} from 'react';
import shape from "../companyCabinet/img/Shape.png";
import copy from "../companyCabinet/img/RectangleCopy.png";
import strelka from '../companyCabinet/img/Strelka.png';
import Navbar from "../clint/navbar/Navbar";
import CompanySidebar from "./CompanySidebar";
import {Button, Col, Input, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import {connect} from "react-redux";
import {byId, companyStatistic, loginCompany} from "../../redux/actions/AppAction";

function CabinetHome(props) {
    const {companyInfo, companyStat, dispatch} = props;

    useEffect(() => {
        getCompany();
    }, []);

    function getCompany() {
        console.log({password: sessionStorage.getItem("Password"), phoneNumber: sessionStorage.getItem("PhoneNumber")})
        dispatch(loginCompany({password: sessionStorage.getItem("Password"), phoneNumber: sessionStorage.getItem("PhoneNumber")}));
    }

    document.body.style.marginLeft = "3.7%";
    document.body.style.backgroundColor = "rgb(231, 230, 230)";

    const [open, setOpen] = useState(false);
    const [res, setRes] = useState(false);

    console.log(companyStat, "company stat")

    const filterDate = () => {
        props.dispatch(companyStatistic({
            startTime: byId("startTime"),
            finishTime: byId("finishTime"),
            companyId: companyInfo.id
        }));
        setRes(true);
        setOpen(false);
    }

    const imgList = [
        {
            cleant: res ? companyStat.jamiClient :  companyInfo.resStatistic.jamiClient,
            name: "Mijozlar soni"
        },
        {
            cleant: res ? companyStat.allBalance :  companyInfo.resStatistic.allBalance,
            name: "Jami savdo"
        },
        {
            cleant: res ? companyStat.companyClientCash :  companyInfo.resStatistic.companyClientCash,
            name: "To'langan cashback"
        },
        {
            cleant: res ? companyStat.clientCash : companyInfo.resStatistic.clientCash,
            name: "Mijozlar cashback"
        },
        {
            cleant: res ? companyStat.urtachaCheck : companyInfo.resStatistic.urtachaCheck,
            name: "O'rtacha cheklar"
        }
    ]

    const openModal = () => setOpen(!open);



    return (
        <div className="cabOperation">
            <Navbar/>
            <CompanySidebar/>
            <Button onClick={() => openModal()}>Filter</Button>
            <div className="mt-5 row" style={{marginRight: '0'}}>
                {imgList.map((item, i) =>
                    <Col className="col-3 operationTable" key={i}>
                        <img className="shape" src={shape} alt="loading.."/>
                        <img className="copy" src={copy} alt="loading.."/>
                        <img className="strelka" src={strelka} alt="loading.."/>
                        <h3>{item.cleant}</h3>
                        <h5>{item.name}</h5>
                    </Col>
                )}
            </div>

            <Modal isOpen={open}>
                <ModalHeader>
                    <h3>Filter date</h3>
                </ModalHeader>
                <ModalBody>
                    <label className="mt-3" style={{color: 'blue'}}>Start Filter</label>
                    <Input type="datetime-local" id="startTime"/>
                    <label className="mt-3" style={{color: 'blue'}}>Finish Filter</label>
                    <Input type="datetime-local" id="finishTime"/>
                </ModalBody>
                <ModalFooter>
                    <Button color="info" onClick={() => openModal()}>Consel</Button>
                    <Button color="success" onClick={() => filterDate()}>Next</Button>
                </ModalFooter>
            </Modal>

        </div>
    );
}

CabinetHome.prototype = {};
export default connect(
    ({app: {companyInfo, companyStat, dispatch}}) =>
        ({companyInfo, companyStat, dispatch}))
(CabinetHome);