import React, {useEffect, useState, useRef} from 'react'
import { useNavigate } from "react-router-dom";
import Paginate from './Paginate';

export default function Wishlist(props) {

    const [books, setBooks] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const recordsPerPage = 5;
    const [nPages, setNPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [bookIdForWishlist, setBookIdForWishlist] = useState(0);
    const [reload, setReload] = useState(false);

    const count = useRef(0);
    let navigate = useNavigate();
    useEffect(() => {
        if (count.current !== 0) {
            fetchWishlist();
        }
        setReload(false);
        count.current++;
    // eslint-disable-next-line react-hooks/exhaustive-deps
    },[currentPage, reload]);

    const fetchWishlist = () => {
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
                
                if(response.bookDTOs == null) {
                  setBooks([]);
                } else {
                  setBooks(response.bookDTOs);
                }
                setCurrentPage(response.page);
                setNPages(response.totalPages);
                setTotalElements(response.totalElements);           
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

    useEffect(() => {
        if(bookIdForWishlist === 0) {
                return;
        }
        console.log('update wishlist...', bookIdForWishlist);
        removeBookFromWishlist();

    // eslint-disable-next-line react-hooks/exhaustive-deps
    },[bookIdForWishlist]);

    const removeBookFromWishlist = () => {
        const removeWishlistUrl = `http://localhost:9001/wishlist/${bookIdForWishlist}`;
        fetch(removeWishlistUrl, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
          }
        }).then(response => {
          if(response.status === 204) {
            console.log('response ', response);
            props.showAlert('Book Id : '+ bookIdForWishlist +' removed from wishlist !!', "warning");
      
            // Setting color for book without making extra call
            // as next call will already set the color.
            for (let i = 0; i < books.length; i++) {
              if(books[i].id === bookIdForWishlist) {
                books[i].inWishlist = false;
              }
            }
            // setReload(true);

          } else {
            console.log('Error in adding to wishlist!');
            localStorage.removeItem("token");
            navigate("/login");
          }
        });
      };

    const handleWishlist = (i) => {
        const book = books.filter((book, index) => index === i);
        console.log('book for wishlist: ', book);
        // console.log('book id : '+bookId + 'add to wishlist : '+inWishlist);
        setBookIdForWishlist(book[0].id);
      };

  return (
    <div className='container'>
      <h2>My Wishlist</h2>
      <div className={`no-wishlist-found-container ${books.length === 0 ? '' : 'hidden'}`}>
        <h3>No Wishlist Exists!!</h3>
      </div>
      <div className={`wishlist-container ${books.length === 0 ? 'hidden' : ''}`}>
        <table className="table table-hover table-sm table-bordered">
            <thead>
                <tr>
                    <th>Book Id</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Condition</th>
                    <th>Publisher</th>
                    <th>Remove</th>
                </tr>
            </thead>
            <tbody>
                {books.map((item,index)=>{
                    return <tr key={index}>
                        <td>
                            {item.id}
                        </td>
                        <td>
                            {item.title}
                        </td>
                        <td>
                            {item.author}
                        </td>
                        <td>
                            {item.genre}
                        </td>
                        <td>
                            {item.bookCondition}
                        </td>
                        <td>
                            {item.publisher}
                        </td>
                        <td>
                          <button type="button" className={`btn btn-outline-${item.inWishlist ? 'danger' : 'dark'}`} onClick={() => handleWishlist(index)}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-trash-fill" viewBox="0 0 16 16">
                            <path d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5M8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0"/>
                            </svg>
                            <span className="visually-hidden">Button</span>
                          </button>
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
    </div>
  )
}
