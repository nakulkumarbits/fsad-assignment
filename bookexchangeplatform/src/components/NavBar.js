import React from 'react'
import { Link, useNavigate } from "react-router-dom";

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
    <a className="navbar-brand" href="/">Book Exchange Platform</a>
    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span className="navbar-toggler-icon"></span>
    </button>
    <div className="collapse navbar-collapse" id="navbarSupportedContent">
      <ul className="navbar-nav me-auto mb-2 mb-lg-0">
        <li className="nav-item">
          <a className="nav-link active" aria-current="page" href="/">Home</a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="/">User Profile</a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="/">About</a>
        </li>
      </ul>
      {/* <button type="button" className={`btn btn-secondary ${localStorage.getItem("token") ? '' : 'hidden'}`} onClick={handleLogout}>Logout</button> */}
      
      
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
                  {/* <Link
                    className="btn btn-primary mx-1"
                    to="/signup"
                    role="button"
                  >
                    Signup
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
