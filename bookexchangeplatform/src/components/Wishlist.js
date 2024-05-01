import React, {useEffect, useState} from 'react'
import { useNavigate } from "react-router-dom";

export default function Wishlist(props) {

    const [currentPage, setCurrentPage] = useState(0);
    const recordsPerPage = 5;

    let navigate = useNavigate();
    useEffect(() => {
        const queryParams = new URLSearchParams({
            page: currentPage,
            size: recordsPerPage
          });
        const wishlistUrl = `http://localhost:9001/wishlist?${queryParams.toString()}`;
        if (!localStorage.getItem("token")) {
            navigate("/login");
        }
        // console.log('wishlist loading...');
        fetch(wishlistUrl, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            }
        }).then(response=> {
            if(response.ok) {
                console.log('wishlist response :', response);
                return response.json();
            } else {
                console.log('Server Error!!', response);
                return 'error';
            }
        }).then(response=> {
            if(response === 'error') {
                console.log('Error in searching books!');
                localStorage.removeItem("token");
                navigate("/login");
            } else {
                console.log('wishlist : ', response);
            }
        });

    // eslint-disable-next-line react-hooks/exhaustive-deps
    },[]);

  return (
    <div className='container'>
      <h2>My Wishlist</h2>
      <div className='wishlist-container'>

      </div>
    </div>
  )
}
