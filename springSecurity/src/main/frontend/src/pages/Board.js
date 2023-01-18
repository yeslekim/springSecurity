import React, { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useCookies } from 'react-cookie'; // useCookies import
import Tr from '../components/Tr';
import Modal from '../components/Modal';
import Post from '../components/Post';

const Complete = () => {
    const [cookies] = useCookies(['access_token']);

    const [info, setInfo] = useState([]);
    const [selected, setSelected] = useState('');
    const [modalOn, setModalOn] = useState(false);

    // useRef : 고유값
    // ref를 사용하여 변수 담기
    const nextId = useRef(11);

    const userGet = () => {        
        axios.defaults.headers.common['Authorization'] = `BEARER ${cookies.access_token}`;
        axios.get('/user/get')
        .then((res) => {
            console.log(res.data)
        });
    
    };

     // 더미 데이터 호출
     useEffect(() => {
        axios.get('https://jsonplaceholder.typicode.com/users')
        .then(res => setInfo(res.data))
        .catch(err => console.log(err));
    }, []);

    const handleSave = (data) => {
        // 데이터 수정하기
        if(data.id) {
            setInfo(
                info.map(row => data.id === row.id ? {
                    id : data.id
                    , name : data.name
                    , email : data.email
                    , phone : data.phone
                    , website : data.website
                } : row)
            )
        } else {
            // 데이터 추가하기
            setInfo(info => info.concat(
                {
                    id : nextId.current
                    , name : data.name
                    , email : data.email
                    , phone : data.phone
                    , website : data.website
                }
            ))
            nextId.current += 1;
        }
    }

    const handleRemove = (id) => {
        setInfo(info => info.filter(item => item.id !== id)); // true면 요소를 유지, false면 필터링
    }

    const handleEdit = (item) => {
        setModalOn(true);
        const selectedData = {
          id: item.id,
          name: item.name,
          email: item.email,
          phone: item.phone,
          website: item.website
        };
        console.log(selectedData);
        setSelected(selectedData);
    };

    const handleCancel = () => {
    setModalOn(false);
    }

    const handleEditSubmit = (item) => {
    console.log(item);
    handleSave(item);
    setModalOn(false);
    }


    userGet();
    return (
        <div className="container max-w-screen-lg mx-auto">
        <div className='text-xl font-bold mt-5 mb-3 text-center'>고객 정보 리스트</div>
        <table className="min-w-full table-auto text-gray-800">
            <thead className='justify-between'>
                <tr className='bg-gray-800'>
                    <th className="text-gray-300 px-4 py-3">Id.</th>
                    <th className="text-gray-300 px-4 py-3">Name</th>
                    <th className="text-gray-300 px-4 py-3">Email</th>
                    <th className="text-gray-300 px-4 py-3">Phone No.</th>
                    <th className="text-gray-300 px-4 py-3">Website</th>
                    <th className="text-gray-300 px-4 py-3">Edit</th>
                    <th className="text-gray-300 px-4 py-3">Delete</th>
                </tr>
            </thead>
            <Tr info={info} handleRemove={handleRemove} handleEdit={handleEdit} />
        </table>
        <Post onSaveData={handleSave} />
        {modalOn && <Modal selectedData={selected} handleCancel={handleCancel} // &&를 사용하여 엘리먼트를 조건부로 넣음
        handleEditSubmit={handleEditSubmit} />}
    </div>
    );
};


export default Complete;