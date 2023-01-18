import React from 'react';
import { Link } from 'react-router-dom';
const Main = () => {
	return (
		<>
			<h3>안녕하세요. 메인페이지 입니다.</h3>
			<button className='bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow'>
				<Link to ="/login">로그인 페이지로 이동</Link>
			</button>
		</>
	);
};

export default Main;