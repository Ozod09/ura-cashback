import {Button, Col, Row} from "reactstrap";
import "./statistic.css";

function Statistic() {
    document.body.style.backgroundColor = "#dcdcde";

    return (
        <>
            <div>
                <div className="s_container ">
                    <div>
                        <h2 className="d-inline s_button">Statistika</h2>
                        <Button color="primary">Filter</Button>
                    </div>
                    <Row className="mt-5">
                        <Col className="col-md-4">
                            <div className="s_1c_color">
                                <h1 className="s_size">0</h1>
                                <p className="s_info">djnsajdnjasn</p>
                            </div>
                        </Col>
                        <Col className="col-md-4">
                            <div className="s_1c_color">
                                <h1 className="s_size">0</h1>
                                <p className="s_info">djnsajdnjasn</p>
                            </div>
                        </Col>
                        <Col className="col-md-4">
                            <div className="s_1c_color">
                                <h1 className="s_size">0</h1>
                                <p className="s_info">djnsajdnjasn</p>
                            </div>
                        </Col>
                    </Row>
                </div>
            </div>
        </>
    )
}

export default Statistic;