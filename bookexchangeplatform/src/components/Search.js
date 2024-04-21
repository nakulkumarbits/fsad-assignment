import React, {useState} from 'react'

export default function Search(props) {

  const [genre, setGenre] = useState('');
  const [author, setAuthor] = useState('');
  const [title, setTitle] = useState('');
  const [location, setLocation] = useState('');

  const handleInputChange = (event, setStateFunction) => {
    setStateFunction(event.target.value);
  };

  const handleSearch = ()=> {
    // console.log("search");
    // Construct the search query string based on form data
    const queryParams = new URLSearchParams({
      genre: genre,
      author: author,
      title: title,
      location: location,
    });

    // Replace 'https://your-search-endpoint.com' with your actual endpoint URL
    const url = `http://localhost:9001/search?${queryParams.toString()}`;

    try {
      const response = fetch(url);
      if (!response.ok) {
        throw new Error(`Error searching: ${response.status}`);
      }
      const data = response.json();
      // Handle the fetched data (display results, etc.)
      console.log("Search results:", data);
    } catch (error) {
      console.error("Error fetching data:", error);
      props.showAlert('Some error occurred!', "danger");
    }
  }

  return (
    <div className='container search-container'>
      <form>
          <div className="mb-1">
              <label htmlFor="genre" className="form-label">genre</label>
              <input type="text" className="form-control" id="genre" value={genre} 
              onChange={(event) => handleInputChange(event, setGenre)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="author" className="form-label">author</label>
              <input type="text" className="form-control" id="author" value={author} 
              onChange={(event) => handleInputChange(event, setAuthor)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="title" className="form-label">title</label>
              <input type="text" className="form-control" id="title" value={title} 
              onChange={(event) => handleInputChange(event, setTitle)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="location" className="form-label">location</label>
              <input type="text" className="form-control" id="location" value={location} 
              onChange={(event) => handleInputChange(event, setLocation)} required/>
          </div>
          
          <button type="button" className="btn btn-primary" onClick={handleSearch}>Search</button>
          </form>
    </div>
  )
}
