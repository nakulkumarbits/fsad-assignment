import React, {useEffect, useState, useRef} from 'react';
import { useNavigate } from "react-router-dom";
import BookDetailPrompt from './BookDetailPrompt';

export default function Order() {

    const [orders, setOrders] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    
    let navigate = useNavigate();
    const count = useRef(0);
    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login");
        }

        if (count.current !== 0) {
            orderSummary();
        }
        count.current++;
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const orderSummary = () => {
        const queryParams = new URLSearchParams({
            page: currentPage,
            size: 5
          });
        const getOrdersForUser = `http://localhost:9001/orders?${queryParams.toString()}`;
        fetch(getOrdersForUser, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
            }).then(response=> {
                if(response.ok) {
                    console.log('response :', response);
                    return response.json();
                } else {
                    console.log('Server Error!!', response);
                    return 'error';
                }
            }).then(response=> {
                if(response === 'error') {
                    console.log('Error in loading orders!');
                    localStorage.removeItem("token");
                    navigate("/login");
                } else {
                    console.log('setting orders : ', response);
                    setOrders(response.orderDTOS);
                    console.log('length : ', Object.keys(response).length);            
                }
            });
    }

    const [bookDetail, setBookDetail] = useState('');
    const handleBookDetail = (bookId) => {
        console.log('fetching book details : ', bookId);
        fetch(`http://localhost:9001/books/${bookId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => {
            console.log("book detail response : ",response);
            if(response.ok) {
                return response.json();
            } else {
                console.log('Server Error!!', response);
                return 'error';
            }
        }).then(response => {
            if(response === 'error') {
                console.log('Error in fetching book detail!');
                localStorage.removeItem("token");
                navigate("/login");
            } else {
                console.log('res', response);
                // setBookDetail(response.bookDTO);
                setBookDetail(response);
            }
        });
    };

  return (
    <div className='container'>
      <h3>Orders</h3>
      <div className='order-container'>
        <table className="table table-hover table-sm">
            <thead>
                <tr>
                    <th>Order Id</th>
                    <th>Owner Book Id</th>
                    <th>Action</th>
                    <th>Recipient Book ID</th>
                    <th>DeliveryMode</th>
                    <th>Status</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
            {orders.map((item,index)=>{
                return <tr key={index}>
                        <td>
                            {item.id}
                        </td>
                        <td>
                            <BookDetailPrompt bookId={item.ownerBookID} 
                                              handleBookDetail={handleBookDetail} 
                                              setBookDetail={setBookDetail}
                                              bookDetail={bookDetail}/>
                        </td>
                        <td>
                            {item.action}
                        </td>
                        <td>
                            {
                                item.recipientBookID ? (<BookDetailPrompt bookId={item.recipientBookID} handleBookDetail={handleBookDetail} 
                                    setBookDetail={setBookDetail} bookDetail={bookDetail}/>) : ('Not Applicable')
                            }
                        </td>
                        <td>
                            {item.deliveryMode}
                        </td>
                        <td>
                            {item.orderStatus}
                        </td>
                        <td>
                            {item.createdDate}
                        </td>
                    </tr>
                })}
            </tbody>
        </table>
      </div>
    </div>
  )
}
