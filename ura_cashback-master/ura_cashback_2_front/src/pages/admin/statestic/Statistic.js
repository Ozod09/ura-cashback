import {Button, Col, Input, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import "./statistic.css";
import {useState} from "react";

const list = [
    {value: 0, name: "ali"},
    {value: 0, name: "ali"},
    {value: 0, name: "ali"},
    {value: 0, name: "ali"},
    {value: 0, name: "ali"},
    {value: 0, name: "ali"}
]

function Statistic() {
    document.body.style.backgroundColor = "#dcdcde";
    const [modal, setmodal] = useState(false);

    const openModal = () => setmodal(!modal);
    return (
        <>
            <div>
                <div className="s_container ">
                    <div>
                        <h2 className="d-inline s_button">Statistika</h2>
                        <Button color="primary" onClick={openModal}>Filter</Button>
                    </div>
                    <Row className="mt-5">
                        {
                            list.map((item, i) =>
                                <Col className="col-md-4 mt-4" key={i}>
                                    <div className="s_1c_color">
                                        <h1 className="s_size">{item.value}</h1>
                                        <p className="s_info">{item.name}</p>
                                    </div>
                                </Col>
                            )
                        }
                    </Row>
                </div>
            </div>

            <Modal isOpen={modal} toggle={openModal}>
                <ModalHeader toggle={openModal}>Filter</ModalHeader>
                <ModalBody className="mt-3">
                    <Input type="datetime-local" />
                    <Input type="datetime-local" className="mt-4"/>
                </ModalBody>
                <ModalFooter>
                    <Button onClick={openModal}>Cancel</Button>
                    <Button color="success">Filter</Button>
                </ModalFooter>
            </Modal>
        </>
    )
}

export default Statistic;