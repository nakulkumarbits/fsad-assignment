import React from 'react'
import { useNavigate } from "react-router-dom";

export default function NavBar(props) {

  let navigate = useNavigate();
  const handleLogout = ()=> {
    console.log("handleLogout token : ", localStorage.getItem("token"));
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <>
    <nav className="navbar navbar-expand-lg bg-body-tertiary">
      <div className="container-fluid">
        
        {/* Logo added below */}
        <a className="navbar-brand" href="/">
        <div className="logo-image">
              <img src="logo192.png" className="img-fluid" />
        </div>
        </a>
        {/* Logo added above */}

        <a className="navbar-brand" href="/">
          Book Exchange Platform
        </a>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <a className="nav-link active" aria-current="page" href="/">Home</a>
            </li>
            {
              localStorage.getItem("token") ? 
              (<li className="nav-item">
                <a className="nav-link" href="/userprofile">User Profile</a>
              </li>) : ('')
            }
            {
              localStorage.getItem("token") ? 
              (<li className="nav-item">
                <a className="nav-link" href="/books">My Books</a>
              </li>) : ('')
            }
            {
              localStorage.getItem("token") ? 
              (<li className="nav-item">
                <a className="nav-link" href="/wishlist">My Wishlist</a>
              </li>) : ('')
            }
            {
              localStorage.getItem("token") ? 
              (<li className="nav-item">
                <a className="nav-link" href="/orders">My Orders</a>
              </li>) : ('')
            }

            {/* <li className="nav-item dropdown">
              <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Dropdown
              </a>
              <ul className="dropdown-menu">
                <li><a className="dropdown-item" href="/userprofile">User Profile</a></li>
                <li><a className="dropdown-item" href="/books">My Books</a></li>
                <li><hr className="dropdown-divider" /></li>
                <li><a className="dropdown-item" href="/about">About</a></li>
              </ul> 
            </li> */}


            <li className="nav-item">
              <a className="nav-link" href="/about">About</a>
            </li>
          </ul>
                  
          <div className="d-flex">
                  {localStorage.getItem("token") ? (
                    <button className="btn btn-danger" onClick={handleLogout}>
                      Log out
                    </button>
                  ) : (
                    <div>
                      {/* <Link
                        className="btn btn-primary mx-1"
                        to="/login"
                        role="button"
                      >
                        Login/Register
                      </Link> */}
                    </div>
                  )}
            </div>
        </div>
      </div>
    </nav>
    </>
  )
}
