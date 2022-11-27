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

    console.log(companyInfo)

    const [open,setOpen] = useState(false);
    const [res, setRes] = useState(false);

    const openModal = ()=>{
        setOpen(!open);
    }

    const filterDate = ()=>{
        const startTime = document.getElementById("startTime").value;
        const finishTime = document.getElementById('finishTime').value;
        props.dispatch(companyStatistic({startTime,finishTime,companyId: companyInfo.id}));
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
                    <h5>Mijozlar soni</h5>
                </Col>
                <Col className="col-3 operationTable2">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.allBalance : companyInfo.resStatistic.allBalance}</h3>
                    <h5>Jami savdo</h5>
                </Col>
                <Col className="col-3 operationTable3">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.clientCompCash : companyInfo.resStatistic.companyClientCash}</h3>
                    <h5>To'langan cashback</h5>
                </Col>
                <Col className="col-3 operationTable4">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.allBalance : companyInfo.resStatistic.clientCash}</h3>
                    <h5>Mijozlar cashback</h5>
                </Col>
                <Col className="col-3 operationTable5">
                    <img className="shape" src={shape}/>
                    <img className="copy" src={copy}/>
                    <img className="strelka" src={strelka}/>
                    <h3>{res ? companyStat.urtachaCheck : companyInfo.resStatistic.urtachaCheck}</h3>
                    <h5>O'rtacha cheklar</h5>
                </Col>

            </div>

            <Modal isOpen={open}>
                <ModalHeader>
                    <h3>Filter date</h3>
                </ModalHeader>
                <ModalBody>
                    <label className="mt-3" style={{color:'blue'}}>Start Filter</label>
                    <Input type="datetime-local"  id="startTime"/>
                    <label className="mt-3" style={{color:'blue'}}>Finish Filter</label>
                    <Input type="datetime-local"  id="finishTime"/>
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