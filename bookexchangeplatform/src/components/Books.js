import React, { useEffect, useState,useRef } from "react";
import { useNavigate } from "react-router-dom";

export default function Books(props) {
  const [reload, setReload] = useState(false);

  const [books, setBooks] = useState([]);
  const [addForm, setAddForm] = useState(false);
  const [editForm, setEditForm] = useState(false);

//   const loggedInUsername = localStorage.getItem("username");

  let navigate = useNavigate();
  const count = useRef(0);
  useEffect(() => {
    if (!localStorage.getItem("token")) {
        navigate("/login");
    }

    if (count.current !== 0) {
        const getAllBooksForUser = `http://localhost:9001/books`;
        fetch(getAllBooksForUser, {
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
                    console.log('Error in loading books!');
                    localStorage.removeItem("token");
                    navigate("/login");
                } else {
                    console.log('setting books : ', response);
                    setBooks(response);
                    console.log('length : ', Object.keys(response).length);            
                }
            });
    }

    setReload(false);

    count.current++;
    // eslint-disable-next-line
  }, [reload]);
  // useEffect ends above.

  const displayAddForm = ()=> {
    console.log('Display only add form now.');
    setAddForm(true);
    // hide the edit form if shown
    setEditForm(false);
  }

  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [genre, setGenre] = useState('');
  const [bookCondition, setBookCondition] = useState('');
  const [publisher, setPublisher] = useState('');

  const saveBook = ()=> {
    const requestBody = {
        title,
        author,
        genre,
        bookCondition,
        publisher
      };
      console.log("save book request body: ",requestBody);

      fetch(`http://localhost:9001/books`,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify(requestBody)
        }).then(response => {
            console.log("add book response : ",response);
            if(response.ok) {
                setReload(true);
                closeAddForm();
                // return response.json();
            } else {
                console.log('Server Error!!', response);
                return 'error';
            }
        });
  }

  const closeAddForm = ()=> {
    console.log('close add form and show other components');
    setAddForm(false);
  }

  const [bookToDelete, setBookToDelete] = useState('');
  const handleBookDelete = (i)=> {
    console.log('Delete the book', i);
    setBookToDelete(books.filter((book, index) => index === i));
    console.log('bookToDelete to delete set ', bookToDelete);

  };

  useEffect(() => {
    console.log('bookToDelete:', bookToDelete);

    if(bookToDelete) {
        console.log('bookToDelete is present');
        console.log('bookToDelete id:', bookToDelete[0].id);
        const bookId = bookToDelete[0].id;
        fetch(`http://localhost:9001/books/${bookId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => {
            // console.log('response : ', response);
            if(response.status === 204) {
                console.log('book deletion successful!');
                props.showAlert('Book '+ bookToDelete[0].title +' deleted successfully!!', "success");
                setReload(true);
            } else {
                console.log('deletion failed with response : ', response);
                localStorage.removeItem("token");
                navigate("/login");
            }
        });
    } else {
        console.log('bookToDelete is not present');
    }

  }, [bookToDelete]); // Log bookToDelete inside useEffect to see updated value

  const handleInputChange = (event, setStateFunction) => {
    setStateFunction(event.target.value);
  };


  const [updateBookId, setUpdateBookId] = useState('');
  const [updateTitle, setUpdateTitle] = useState('');
  const [updateAuthor, setUpdateAuthor] = useState('');
  const [updateGenre, setupdateGenre] = useState('');
  const [updateBookCondition, setupdateBookCondition] = useState('');
  const [updatePublisher, setUpdatePublisher] = useState('');
  const handleBookEdit = (i) => {
    console.log('Display only edit form now.', i);
    setEditForm(true);
    // hide the add form if shown.
    setAddForm(false);

    const book = books.filter((book, index) => index === i);
    console.log('book to edit: ', book);

    setUpdateBookId(book[0].id);
    setUpdateTitle(book[0].title);
    setUpdateAuthor(book[0].author);
    setupdateGenre(book[0].author);
    setupdateBookCondition(book[0].bookCondition);
    setUpdatePublisher(book[0].publisher);
  }

  const updateBook = ()=> {
    const requestBody = {
        title: updateTitle,
        author: updateAuthor,
        genre: updateGenre,
        bookCondition: updateBookCondition,
        publisher: updatePublisher
      };
      console.log("update book request body: ",requestBody);

      fetch(`http://localhost:9001/books/${updateBookId}`,{
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify(requestBody)
        }).then(response => {
            console.log("update book response : ",response);
            if(response.ok) {
                setReload(true);
                closeEditForm();
                props.showAlert('Book '+ updateTitle +' updated successfully!!', "success");
                // return response.json();
            } else {
                console.log('Server Error!!', response);
                return 'error';
            }
        });
  }

  const closeEditForm = ()=> {
    console.log('close edit form and show other components');
    setEditForm(false);
  }
    
  return (
    <div className='container'>
      <h2>My Books</h2>
      <div className={`book-container container ${Object.keys(books).length === 0 ? 'hidden' : '' } `}>
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
                            <button type="button" className="btn btn-outline-warning btn-sm mx-1" onClick={() => handleBookEdit(index)} >Edit</button>
                            <button type="button" className="btn btn-outline-danger btn-sm mx-1" onClick={() => handleBookDelete(index)} >Delete</button>
                        </td>
                    </tr>
                })}
        </tbody>
        </table>      
      </div>
      <div className={`add-book-form container ${addForm ? '' : 'hidden'} `}>
        <form>
            <table className="table table-hover table-sm">
                <tbody>
                    <tr>
                        <th scope="col">Title</th>
                        <td><input type="text" id="title" className="form-control" value={title} onChange={(event) => handleInputChange(event, setTitle)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Author</th>
                        <td><input type="text" id="author" className="form-control" value={author} onChange={(event) => handleInputChange(event, setAuthor)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Genre</th>
                        <td><input type="text" id="genre" className="form-control" value={genre} onChange={(event) => handleInputChange(event, setGenre)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Book condition</th>
                        {/* <input type="text" id="bookCondition" className="form-control" value={bookCondition} onChange={(event) => handleInputChange(event, setBookCondition)} required/> */}
                        <td>
                            <select className="form-select" aria-label="Default select example"
                            value={bookCondition} 
                            onChange={(event) => handleInputChange(event, setBookCondition)}
                            >
                            <option value='' disabled>Open this select menu</option>
                            <option value="BEST">BEST</option>
                            <option value="GOOD">GOOD</option>
                            <option value="POOR">POOR</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th scope="col">Publisher</th>
                        <td><input type="text" id="publisher" className="form-control" value={publisher} onChange={(event) => handleInputChange(event, setPublisher)} required/></td>
                    </tr>
                </tbody>
            </table>
            <button type="button" className="btn btn-primary" onClick={saveBook}>Save Book</button>
            <button type="button" className="btn btn-link" onClick={closeAddForm}>Cancel</button>
        </form>
      </div>
      <button type="button" className={`btn btn-primary my-2 ${addForm || editForm ? 'hidden' : ''}`} onClick={displayAddForm}>Add Book</button>

      {/* Edit book container below */}

      <div className={`edit-book-form container ${editForm ? '' : 'hidden'} `}>
        <h4>Edit Book</h4>
        <form>
            <table className="table table-hover table-sm">
                <tbody>
                    <tr>
                        <th scope="col">Title</th>
                        <td><input type="text" id="title" className="form-control" value={updateTitle} onChange={(event) => handleInputChange(event, setUpdateTitle)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Author</th>
                        <td><input type="text" id="author" className="form-control" value={updateAuthor} onChange={(event) => handleInputChange(event, setUpdateAuthor)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Genre</th>
                        <td><input type="text" id="genre" className="form-control" value={updateGenre} onChange={(event) => handleInputChange(event, setupdateGenre)} required/></td>
                    </tr>
                    <tr>
                        <th scope="col">Book condition</th>
                        {/* <input type="text" id="bookCondition" className="form-control" value={bookCondition} onChange={(event) => handleInputChange(event, setBookCondition)} required/> */}
                        <td>
                            <select className="form-select" aria-label="Default select example"
                            value={updateBookCondition} 
                            onChange={(event) => handleInputChange(event, setupdateBookCondition)}
                            >
                            <option value='' disabled>Open this select menu</option>
                            <option value="BEST">BEST</option>
                            <option value="GOOD">GOOD</option>
                            <option value="POOR">POOR</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th scope="col">Publisher</th>
                        <td><input type="text" id="publisher" className="form-control" value={updatePublisher} onChange={(event) => handleInputChange(event, setUpdatePublisher)} required/></td>
                    </tr>
                </tbody>
            </table>
            <button type="button" className="btn btn-primary" onClick={updateBook}>Update Book</button>
            <button type="button" className="btn btn-link" onClick={closeEditForm}>Cancel</button>
        </form>
      </div>
    </div>
  )
}
