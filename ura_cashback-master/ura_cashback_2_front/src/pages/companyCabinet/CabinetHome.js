import React, {useState} from 'react';
import shape from "../companyCabinet/img/Shape.png";
import copy from "../companyCabinet/img/RectangleCopy.png";
import strelka from '../companyCabinet/img/Strelka.png';
import Navbar from "../clint/navbar/Navbar";
import CompanySidebar from "./CompanySidebar";
import {Button, Col, Input, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import {connect} from "react-redux";
import {companyStatistic} from "../../redux/actions/AppAction";



function CabinetHome(props) {


    const {companyInfo, companyStat} = props;

    const [open,setOpen] = useState(false);
    const [res, setRes] = useState(false);

    const openModal = ()=>{
        setOpen(!open);
    }

    const filterDate = ()=>{
        const filterDate = document.getElementById("filterDate").value;
        props.dispatch(companyStatistic({filterDate,companyId: companyInfo.id}));
        setRes(true);
        setOpen(false);
    }



    return (
        <div className="cabOperation">
            <Navbar/>
            <CompanySidebar/>
            <Button onClick={()=> openModal()}>Filter</Button>
            <div className="mt-5 row" style={{marginRight:'0'}}>
                <Col className="col-3 operationTable">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.jamiClient : companyInfo.resStatistic.jamiClient}</h3>
                    <h5>all client</h5>
                </Col>
                <Col className="col-3 operationTable2">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.clientCompCash : companyInfo.resStatistic.clientCompCash}</h3>
                    <h5>client cashback</h5>
                </Col>
                <Col className="col-3 operationTable3">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.clientCompCash : companyInfo.resStatistic.clientCompCash}</h3>
                    <h5>client company cashback</h5>
                </Col>
                <Col className="col-3 operationTable4">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.allBalance : companyInfo.resStatistic.allBalance}</h3>
                    <h5>all balance</h5>
                </Col>
                <Col className="col-3 operationTable5">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>678</h3>
                    <h5>cashback</h5>
                </Col>
                <Col className="col-3 operationTable6">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>678</h3>
                    <h5>cashback</h5>
                </Col>
            </div>

            <Modal isOpen={open}>
                <ModalHeader>
                    <h3>Filter date</h3>
                </ModalHeader>
                <ModalBody>
                    <Input type="date" className="mt-3" id="filterDate"/>
                </ModalBody>
                <ModalFooter>
                    <Button color="info" onClick={()=> openModal()}>Consel</Button>
                    <Button color="success" onClick={()=> filterDate()}>Next</Button>
                </ModalFooter>
            </Modal>

        </div>
    );
}

CabinetHome.prototype= {};
export default connect(
    ({app:{companyInfo, companyStat}}) =>
({companyInfo, companyStat}))
(CabinetHome);