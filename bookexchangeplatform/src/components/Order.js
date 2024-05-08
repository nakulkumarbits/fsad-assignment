import React, {useEffect, useState, useRef} from 'react';
import { useNavigate } from "react-router-dom";
import BookDetailPrompt from './BookDetailPrompt';
import Paginate from './Paginate';

export default function Order() {

    const [orders, setOrders] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const recordsPerPage = 5;
    const [nPages, setNPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [requests, setRequests] = useState([]);
    const [requestCurrentPage, setRequestCurrentPage] = useState(0);
    const [requestsNPages, setRequestsNPages] = useState(0);
    const [requestsTotalElements, setRequestsTotalElements] = useState(0);
    
    let navigate = useNavigate();
    const count = useRef(0);
    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login");
        }

        if (count.current !== 0) {
            orderSummary();
            orderRequests();
        }
        count.current++;
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const orderSummary = () => {
        const queryParams = new URLSearchParams({
            page: currentPage,
            size: recordsPerPage
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
                    setCurrentPage(response.page);
                    setNPages(response.totalPages);
                    setTotalElements(response.totalElements);           
                    console.log('length : ', Object.keys(response).length);            
                }
            });
    }

    const orderRequests = () => {
        const queryParams = new URLSearchParams({
            page: requestCurrentPage,
            size: recordsPerPage
          });
        const getRequestsForUser = `http://localhost:9001/requests?${queryParams.toString()}`;
        fetch(getRequestsForUser, {
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
                    console.log('Error in loading requests!');
                    localStorage.removeItem("token");
                    navigate("/login");
                } else {
                    console.log('setting requests : ', response);
                    setRequests(response.orderDTOS);
                    setRequestCurrentPage(response.page);
                    setRequestsNPages(response.totalPages);
                    setRequestsTotalElements(response.totalElements);           
                    console.log('length : ', Object.keys(response).length);            
                }
            });
    };

    const paginate = (pageNumber) => {
        if(currentPage + 1 !== pageNumber) {
        //   console.log('current page : ' + currentPage + ', page number : ' + pageNumber);
          setCurrentPage(pageNumber-1);
        }
    };
    
    const previousPage = () => {
    //   console.log('prev page : ', currentPage - 1);
      if (currentPage !== 0) {
         setCurrentPage(currentPage - 1);
      }
    };
    
    const nextPage = () => {
    //   console.log('next page : ', currentPage + 1);
      if (currentPage !== nPages-1) {
         setCurrentPage(currentPage + 1);
      }
    };

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

    const handleApproveRequest = (ownerBookID) => {
        console.log('Approve request', ownerBookID);
    };

  return (
    <div className='container'>
      <div className='order-container-wrapper'>
        <h3>Orders</h3>
        <div className={`order-container ${orders.length===0? 'hidden': ''}`}>
            <table className='table table-hover table-sm'>
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

            <Paginate
            recordsPerPage={recordsPerPage}
            totalElements={totalElements}
            paginate={paginate}
            previousPage={previousPage}
            nextPage={nextPage}
            />

        </div>
        <div className={`no-orders-found-container ${orders.length === 0 ? '' : 'hidden'}`}>
            <h5>No Orders Exist!!</h5>
        </div>
      </div>

      <hr />

      <div className='request-container-wrapper'>
        <h3>My Pending Requests</h3>
        <div className={`request-container ${requests.length===0? 'hidden': ''}`}>
            <table className='table table-hover table-sm'>
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Owner Book Id</th>
                        <th>Action</th>
                        <th>Recipient Book ID</th>
                        <th>DeliveryMode</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                {requests.map((item,index)=>{
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
                            <td>
                                <p>
                                <button type="button" className={`btn btn-outline-success`} 
                                    onClick={() => handleApproveRequest(item.ownerBookID)}>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-check-circle-fill" viewBox="0 0 16 16">
                                    <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
                                    </svg>
                                    <span className="visually-hidden">Button</span>
                                </button>
                                <button type="button" className={`btn btn-outline-danger mx-1`} >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-x-circle-fill" viewBox="0 0 16 16">
                                    <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293z"/>
                                    </svg>
                                    <span className="visually-hidden">Button</span>
                                </button>
                                </p>
                            </td>
                            
                        </tr>
                    })}
                </tbody>
            </table>

            <Paginate
            recordsPerPage={recordsPerPage}
            totalElements={requestsTotalElements}
            paginate={paginate}
            previousPage={previousPage}
            nextPage={nextPage}
            />
        </div>
        <div className={`no-request-found-container ${requests.length === 0 ? '' : 'hidden'}`}>
            <h5>No Pending Requests!!</h5>
        </div>
      </div>

    </div>
  )
}
