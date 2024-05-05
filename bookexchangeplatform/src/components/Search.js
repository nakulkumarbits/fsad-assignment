import React, {useState, useEffect, useRef} from 'react';
import { useNavigate } from "react-router-dom";
import Paginate from './Paginate';

import Prompt from './Prompt';

export default function Search(props) {
  const [books, setBooks] = useState([]);
  const [genre, setGenre] = useState('');
  const [author, setAuthor] = useState('');
  const [title, setTitle] = useState('');
  const [location, setLocation] = useState('');

  const [searchDone, setSearchDone] = useState(false);
  
  const [bookIdForWishlist, setBookIdForWishlist] = useState(0);
  const [toggleBookWishlist, setToggleBookWishlist] = useState(false);

  const handleInputChange = (event, setStateFunction) => {
    setStateFunction(event.target.value);
  };

  const [currentPage, setCurrentPage] = useState(0);
  const [nPages, setNPages] = useState(0);
  const recordsPerPage = 5;
  const [totalElements, setTotalElements] = useState(0);

  const [duration, setDuration] = useState('');
  const [deliveryMode, setDeliveryMode] = useState('');
  const [exchangeForm, setExchangeForm] = useState(false);
  // const [bookIdForBorrow, setBookIdForBorrow] = useState(0);

  let navigate = useNavigate();
  const handleSearch = ()=> {
    // console.log('search called from search button');
    searchBooks();
  }

  const searchBooks = () => {
    const queryParams = new URLSearchParams({
      genre: genre,
      author: author,
      title: title,
      location: location,
      page: currentPage,
      size: recordsPerPage
    });

    if(genre.trim() === "" && author.trim() === "" && title.trim() === "" && location.trim() === "") {
      // console.log('all empty skipping call');
      return;
    }
      const url = `http://localhost:9001/search?${queryParams.toString()}`;
      fetch(url, {
        method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('token')
            }
      }).then(response=> {
        // console.log('setting searchDone : ', searchDone);
        setSearchDone(true);
        // console.log('searchDone : ', searchDone);
        if(response.ok) {
            console.log('search response :', response);
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
            console.log('setting books : ', response);
            setBooks(response.bookDTOs);
            setCurrentPage(response.page);
            setNPages(response.totalPages);
            setTotalElements(response.totalElements);           
        }
    });
  };

  const count = useRef(0);
  useEffect(() => {
    // console.log('count current : ', count.current);
    if (count.current !== 0) {
      // console.log('current page updated : ', currentPage);
      searchBooks();
    }
    count.current++;
  // eslint-disable-next-line react-hooks/exhaustive-deps
  },[currentPage]);

  const paginate = (pageNumber) => {
    
    if(currentPage + 1 !== pageNumber) {
      console.log('current page : ' + currentPage + ', page number : ' + pageNumber);
      setCurrentPage(pageNumber-1);
    }
 };

 const previousPage = () => {
  console.log('prev page : ', currentPage - 1);
  if (currentPage !== 0) {
     setCurrentPage(currentPage - 1);
    //  console.log('prev page update : ', currentPage - 1);
    //  handleSearch();

  }
};

const nextPage = () => {
  console.log('next page : ', currentPage + 1);
  if (currentPage !== nPages-1) {
     setCurrentPage(currentPage + 1);
    //  console.log('next page update : ', currentPage + 1);
    //  handleSearch();
  }
};

const [bookIdForExchange, setBookIdForExchange] = useState('');
const [bookTitleForExchange, setBookTitleForExchange] = useState('');
const [booksForExchange, setBooksForExchange] = useState([]);
const [bookIdToExchange, setBookIdToExchange] = useState('');

const handleBookExchange = (id, title) => {
  // console.log('show exchange form');
  setBookIdForExchange(id);
  setBookTitleForExchange(title);
  setExchangeForm(true);

  const getAllBooksForUser = `http://localhost:9001/books`;
  fetch(getAllBooksForUser, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
    }
  }).then(response=> {
      if(response.ok) {
          // console.log('fetch all books for user :', response);
          return response.json();
      } else {
          console.log('Server Error!!', response);
          return 'error';
      }
  }).then(response=> {
      if(response === 'error') {
          console.log('Error in loading books!');
          localStorage.removeItem("token");
          navigate("/login");
      } else {
          console.log('setting books for exchange : ', response);
          setBooksForExchange(response);
          // console.log('length : ', Object.keys(response).length);            
      }
  });
};

const initiateExchange = () => {
  const requestBody = {
    action: 'EXCHANGE',
    ownerBookID: bookIdForExchange,
    recipientBookID: bookIdToExchange,
    deliveryMode: deliveryMode.length===0?'STANDARD':deliveryMode,
  }; 
  console.log('exchange request : ', requestBody);
  fetch('http://localhost:9001/exchange', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
    },
    body: JSON.stringify(requestBody)
  }).then(response => {
    console.log('response : ', response);
    if(response.ok) {
      console.log('request created');
      props.showAlert('Borrow request raised for Book Id : '+ bookIdForExchange +' !!', "success");
      closeExchangeForm();
    } else if(response.status === 409) {
      props.showAlert('Borrow request already exists for Book Id : '+ bookIdForExchange +' !!', "danger");
      closeExchangeForm();
    } else {
      console.log('server error!!');
      localStorage.removeItem("token");
      navigate("/login");
    }
  });
};

const closeExchangeForm = ()=> {
  console.log('close exchange form and show other components');
  setBookIdToExchange('');
  setDeliveryMode('');
  setExchangeForm(false);
}

const handleBookBorrow = (bookIdForBorrow) => {
  console.log('borrow book', bookIdForBorrow);
  const requestBody = {
    action: 'LEND',
    ownerBookID: bookIdForBorrow,
    duration,
    deliveryMode,
  }; 

  console.log('lend :', requestBody);

  fetch(`http://localhost:9001/lend`,{
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
      },
      body: JSON.stringify(requestBody)
  }).then(response=>{
    console.log('borrow res: ', response);
    if(response.ok) {
      console.log('request created');
      props.showAlert('Borrow request raised for Book Id : '+ bookIdForBorrow +' !!', "success");
    } else if(response.status === 409) {
      console.log('request exists');
      props.showAlert('Borrow request already exists for Book Id : '+ bookIdForBorrow +' !!', "danger");
    } else {
      console.log('server error!!');
      localStorage.removeItem("token");
      navigate("/login");
    }
  });


};

useEffect(() => {
  // console.log('bookIdForWishlist: ', bookIdForWishlist);

  if(bookIdForWishlist === 0) {
    return;
  }

  if(toggleBookWishlist) {
    // console.log('toggle to add to wishlist');
    addBookToWishlist();
  } else {
    // console.log('toggle to remove from wishlist');
    removeBookFromWishlist();
  }

// eslint-disable-next-line react-hooks/exhaustive-deps
},[bookIdForWishlist, toggleBookWishlist]);

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
    } else {
      console.log('Error in adding to wishlist!');
      localStorage.removeItem("token");
      navigate("/login");
    }
  });
};

const addBookToWishlist = () => {
  const addToWishlistUrl = `http://localhost:9001/wishlist/${bookIdForWishlist}`;
  fetch(addToWishlistUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.getItem('token')
    }
  }).then(response => {
    if(response.ok) {
      console.log('response ', response);
      props.showAlert('Book Id : '+ bookIdForWishlist +' added to wishlist !!', "success");

      // Setting color for book without making extra call
      // as next call will already set the color.
      for (let i = 0; i < books.length; i++) {
        if(books[i].id === bookIdForWishlist) {
          books[i].inWishlist = true;
        }
      }
      // return response.json();
    } else {
      console.log('Error in adding to wishlist!');
      localStorage.removeItem("token");
      navigate("/login");
    }
  });
}

const handleWishlist = (i, inWishlist, bookId) => {
  const book = books.filter((book, index) => index === i);
  // console.log('book for wishlist: ', book);
  // console.log('book id : '+bookId + 'add to wishlist : '+inWishlist);
  setBookIdForWishlist(book[0].id);
  setToggleBookWishlist(inWishlist);
};

  return (
    <div className='container search-container'>
      <div className='search-form-container'>
        <form>
            <table>
              <tbody>
                <tr>
                  <td>
                  <label htmlFor="genre" className="form-label">genre</label>
                  <input type="text" className="form-control" id="genre" value={genre} 
                  onChange={(event) => handleInputChange(event, setGenre)} required/>
                  </td>
                  <td>
                  <label htmlFor="author" className="form-label">author</label>
                  <input type="text" className="form-control" id="author" value={author} 
                  onChange={(event) => handleInputChange(event, setAuthor)} required/>
                  </td>
                  <td>
                  <label htmlFor="title" className="form-label">title</label>
                  <input type="text" className="form-control" id="title" value={title} 
                  onChange={(event) => handleInputChange(event, setTitle)} required/>
                  </td>
                  <td>
                  <label htmlFor="location" className="form-label">location</label>
                  <input type="text" className="form-control" id="location" value={location} 
                  onChange={(event) => handleInputChange(event, setLocation)} required/>
                  </td>
                </tr>              
              </tbody>
            </table>
            <button type="button" className="btn btn-primary my-1" onClick={handleSearch}>Search</button>
        </form>
      </div>
      <div className={`no-books-found-container ${books.length === 0 ? '' : 'hidden'}`}>
            {
              searchDone ? 
              (<h3>No books found for the criteria!!</h3>) : ('')
            }
      </div>
      <div className={`search-books-container ${books.length === 0 ? 'hidden' : ''}`}>
        <h3>Searched Books</h3>
        <table className="table table-hover table-sm table-bordered">
            <thead>
                <tr>
                    <th>Book Id</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Condition</th>
                    <th>Publisher</th>
                    <th>Action</th>
                    <th>Wishlist</th>
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
                            <button type="button" className="btn btn-info btn-sm mx-1 my-1" onClick={() => handleBookExchange(item.id, item.title)} >Exchange</button>
                            {/* <button type="button" className="btn btn-info btn-sm mx-1" onClick={() => handleBookBorrow()} >Borrow</button> */}
                            {/* <Popup trigger=
                            {<button> Click to open popup </button>}
                            position="right center">
                            <div>GeeksforGeeks</div>
                            <button onClick={() => handleBookBorrow(index)}>Click here</button>
                          </Popup> */}
                          <Prompt handleBookBorrow={handleBookBorrow} 
                                  bookIdForBorrow={item.id}
                                  duration={duration}
                                  setDuration={setDuration}
                                  deliveryMode={deliveryMode}
                                  setDeliveryMode={setDeliveryMode}
                                  handleInputChange={handleInputChange}
                          />
                          {/* <GenericPopup message='asfdgf'/> */}
                        </td>
                        <td>
                          <button type="button" className={`btn btn-outline-${item.inWishlist ? 'danger' : 'dark'}`} onClick={() => handleWishlist(index, !item.inWishlist, item.id)}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-heart-fill" viewBox="0 0 16 16">
                            <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"></path>
                            </svg>
                            <span className="visually-hidden">Button</span>
                          </button>
                        </td>
                    </tr>
                })}
        </tbody>
        </table>

        {/* <div className='pagination'>
          <p>current page : {currentPage}</p>
          <p>total pages : {nPages}</p>
          <p>totalElements : {totalElements}</p>
          <p>page size : {recordsPerPage}</p>
        </div> */}

        <Paginate
          recordsPerPage={recordsPerPage}
          totalElements={totalElements}
          paginate={paginate}
          previousPage={previousPage}
          nextPage={nextPage}
        />

      </div>

      <div className={`exchange-book-container ${exchangeForm?'':'hidden'}`}>
        <h3>Exchange book</h3>
        <form>
          <table className="table table-hover table-sm">
            <tbody>
              <tr>
                <th scope="col">Book Id</th>
                <td>{bookIdForExchange}</td>
              </tr>
              <tr>
                <th scope="col">Book title</th>
                <td>{bookTitleForExchange}</td>
              </tr>
              <tr>
                <th scope="col">Book to exchange with</th>
                <td>
                  <select className="form-select" 
                    value={bookIdToExchange}
                    onChange={(event) => handleInputChange(event, setBookIdToExchange)}>
                    <option value='' disabled>Open this select menu</option>
                    {booksForExchange.map((book, i) => {
                      return <option key={i} value={book.id}>
                                {book.title} ({book.bookCondition})
                            </option>
                    })}
                  </select>
                </td>
              </tr>
              <tr>
                <th scope="col">Delivery Mode</th>
                <td>
                  <div onChange={(event) => handleInputChange(event, setDeliveryMode)}>
                  <input type="radio" value="STANDARD" className='mx-1' name="deliveryMode" defaultChecked/> STANDARD
                  <input type="radio" value="EXPRESS" className='mx-1'name="deliveryMode"/> EXPRESS
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <button type="button" className="btn btn-primary" onClick={initiateExchange}>Initiate Exchange</button>
          <button type="button" className="btn btn-link" onClick={closeExchangeForm}>Cancel</button>
        </form>
      </div>

    </div>
  )
}
