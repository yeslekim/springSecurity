import React, { useState } from "react";
import axios from 'axios';
import '../Login.css';
import { useCookies } from 'react-cookie'; // useCookies import

function Login() {
    const [inputId, setInputId] = useState('');
    const [inputPw, setInputPw] = useState('');
    const [cookies, setCookie] = useCookies(['access_token']); // 쿠키 훅 
    
    // input data의 변화가 있을 때 마다 value값을 변경해서 useState 해준다
    const handleInputId = (e) => {
        setInputId(e.target.value);
    }

    const handleInputPw = (e) => {
        setInputPw(e.target.value);
    }

    // login 버튼 클릭 이벤트
    const onClickLogin = () => {    
        let body = {
            account : inputId
            , password : inputPw
        };

        axios.post("/login", body)
        .then((res) => {
            console.log(res.data)            
            if(res.data.result === "success") {
                alert("로그인 성공");               
                
                // access token은 cookie,refresh token은 session
                localStorage.setItem('refresh_token', res.data.token['refresh_token']);
                setCookie('access_token', res.data.token['access_token']);              
                              
                axios.defaults.headers.common['Authorization'] = `BEARER ${cookies.access_token}`;                
                document.location.href='/board';
            } else {
                setInputPw('');
                alert("로그인 실패");
            }
            
        })
        .catch((error) => {
            console.log(error);
            setInputPw('');
            alert(error.response.data);
        });

    }

    return (
        <div className="login-container">
            <div className="login-form">
                <h1>Login</h1>                
                <input 
                    type='text' 
                    name='input_id' 
                    value={inputId} 
                    onChange={handleInputId} 
                    placeholder="아이디"
                />                
                <input 
                    type='password' 
                    name='input_pw' 
                    value={inputPw} 
                    onChange={handleInputPw} 
                    placeholder="비밀번호"
                />

                <button type='button' onClick={onClickLogin}>Login</button>
            </div>            
        </div>
    )
}

export default Login;