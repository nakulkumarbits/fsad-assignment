import React, {useState, useEffect, useRef} from 'react';
import { useNavigate } from "react-router-dom";
import Paginate from './Paginate';

export default function Search(props) {
  const [books, setBooks] = useState([]);
  const [genre, setGenre] = useState('');
  const [author, setAuthor] = useState('');
  const [title, setTitle] = useState('');
  const [location, setLocation] = useState('');

  const [searchDone, setSearchDone] = useState(false);

  const handleInputChange = (event, setStateFunction) => {
    setStateFunction(event.target.value);
  };

  const [currentPage, setCurrentPage] = useState(0);
  const [nPages, setNPages] = useState(0);
  const recordsPerPage = 5;
  const [totalElements, setTotalElements] = useState(0);

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
      console.log('all empty skipping call');
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
      console.log('current page updated : ', currentPage);
      searchBooks();
    }
    count.current++;
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



  const handleBookExchange = () => {

  };

  const handleBookBorrow = () => {

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
                            <button type="button" className="btn btn-info btn-sm mx-1 my-1" onClick={() => handleBookExchange(index)} >Exchange</button>
                            <button type="button" className="btn btn-info btn-sm mx-1" onClick={() => handleBookBorrow(index)} >Borrow</button>
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
    </div>
  )
}
