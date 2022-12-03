import React, {useEffect, useState} from 'react';
import {connect} from "react-redux";
import {activeCompany, addAttachmentAction, getCompany, saveCompany,} from "../../../redux/actions/AppAction";
import {Button, Input, InputGroup, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row, Table} from "reactstrap";
import Sidebar from "../../clint/navbar/Sidebar";
import './company.css';
import {api} from "../../../api/api";

function CompanyAdmin(props) {

    const {company, search, page, size, dispatch, active, attachmentId, currentUser} = props;

    useEffect(() => {
        dispatch(getCompany());
    }, [dispatch]);

    const sendPhoto = (item) => {
        let obj = new FormData();
        obj.append("request", item.target.files[0]);
        dispatch(addAttachmentAction(obj));
    }

    const [editModal, setEditModal] = useState(false);
    const [currentCompany, setCurrentCompany] = useState({});

    const openEdit = () => setEditModal(!editModal);

    document.body.style.marginLeft = "3.7%";
    document.body.style.backgroundColor = "rgba(231, 229, 229, 0.73)";


    const set = (item) => {
        dispatch({
            type: "updateState",
            payload: {
                search: item.target.value.toLowerCase()
            }
        })
    }

    //Search
    const filter = company.filter((el) => {
        if (search === '') {
            return el;
        } else {
            return el.name.toLowerCase().includes(search)
        }
    })

    const indexOfLasPost = page * size;
    const indexOfFirstPosts = indexOfLasPost - size;
    const currentPosts = filter.slice(indexOfFirstPosts, indexOfLasPost);


    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(company.length / size); i++) {
        pageNumbers.push(i);
    }

    const paginate = (number) => {
        dispatch({
            type: "updateState",
            payload: {
                page: number
            }
        })
    }

    const changeActive = () => {
        dispatch({
            type: 'updateState',
            payload: {
                active: !active
            }
        });
    }

    const changeActiveCompany = (item) => {
        dispatch(activeCompany(item));
    }

    function addCompany() {
        let name = document.getElementById("name").value;
        let bio = document.getElementById("bio").value;
        let description = document.getElementById("description").value;
        let clientPercentage = document.getElementById("clientPercentage").value;
        let obj = {
            id: currentCompany.id,
            name,
            bio,
            description,
            clientPercentage,
            attachmentId, currentUser,
            userId: currentUser,
            active
        };
        dispatch(saveCompany(obj));
        dispatch(getCompany());
        openEdit();
    }

    return (
        <div>
            <Sidebar/>
            <div className="searchSuperAdminCompany">
                <InputGroup>
                    <Input type="text" onChange={(item) => set(item)} placeholder="Enter company name"/>
                    <Button color="primary" type="button"><i
                        className="pi pi-search searchIconcaSuperAdmin"/></Button>
                </InputGroup>
            </div>
            <div className="me-5 ms-5 superAdminCompanyTable">
                <Table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Attachment</th>
                        <th>Name</th>
                        <th>Bio</th>
                        <th>Description</th>
                        <th>ClintPercentage</th>
                        <th>Active</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    {currentPosts.map((item, i) =>
                        <tbody key={i}>
                        <tr>
                            <td>{i + 1}</td>
                            <td><img className="company-img" src={api.getAttachment + item.attachment.id}
                                     alt="not"/></td>
                            <td>{item.name}</td>
                            <td>{item.bio}</td>
                            <td>{item.description}</td>
                            <td>{item.clientPercentage}</td>
                            <td>
                                <Row>
                                    <Label check for="active">
                                        <div className="form-check form-switch">
                                            <Input type="checkbox" defaultChecked={item.active1}
                                                   onChange={() => {
                                                       changeActive();
                                                       changeActiveCompany(item.id)
                                                   }}/>
                                        </div>
                                    </Label>
                                </Row>
                            </td>
                            <td><Button color="warning" outline onClick={() => {
                                openEdit();
                                setCurrentCompany(item);
                            }}>Edit</Button></td>
                        </tr>
                        </tbody>
                    )}
                </Table>
            </div>

            <Modal isOpen={editModal} toggle={openEdit}>
                <ModalHeader toggle={openEdit}>
                    EditCompany
                </ModalHeader>
                <ModalBody>
                    {currentCompany &&
                        <div>
                            <Input id="name" type='text'
                                   defaultValue={currentCompany ? currentCompany.name : ""} placeholder='Enter company name'/>
                            <Input className="mt-4" id="bio" type='text'
                                   defaultValue={currentCompany ? currentCompany.bio : ""} placeholder='Enter company bio'/>
                            <Input className="mt-4" id="description" type='text'
                                   defaultValue={currentCompany ? currentCompany.description : ""}
                                   placeholder='Enter company description'/>
                            <Input className="mt-4" id="clientPercentage" type='number'
                                   defaultValue={currentCompany ? currentCompany.clientPercentage : ""}
                                   placeholder='Enter company clientPercentage'/>
                            <Input className="mt-4"  type="file"
                                   onChange={(item) => sendPhoto(item)}/>
                        </div>
                    }
                </ModalBody>
                <ModalFooter>
                    <Button onClick={openEdit} outline>Cancel</Button>
                    <Button onClick={addCompany} outline color="success">Edit</Button>
                </ModalFooter>
            </Modal>


            <div className='comPagination'>
                <nav>
                    <ul className="pagination">
                        {pageNumbers.map((number, i) =>
                            <li key={i} className="page-item">
                                <a onClick={() => paginate(number)} className="page-link">{number}</a>
                            </li>
                        )}
                    </ul>
                </nav>
            </div>

        </div>
    );
}

CompanyAdmin.propTypes = {};

export default connect(
    ({app: {company, search, page, size, active, attachmentId, currentUser}}) =>
        ({company, search, page, size, active, attachmentId, currentUser}))
(CompanyAdmin);

