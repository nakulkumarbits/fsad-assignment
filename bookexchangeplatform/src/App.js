import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import './App.css';
import NavBar from './components/NavBar';
import LoginRegister from './components/LoginRegister';
import Alert from './components/Alert';
// import Search from './components/Search';
import Home from './components/Home';
import About from './components/About';
import UserProfile from './components/UserProfile';
import Books from './components/Books';

function App() {

  const[alert, setAlert] = useState(null); 
  const [token, setToken] = useState('');

  const showAlert = (message, type) => {
    setAlert({
      msg: message,
      type: type
    });
    setTimeout(() => {
      setAlert(null);
    }, 1500);
  };

  return (
    <>    
        <Router>
          <NavBar token={token}/> 
          <Alert alert={alert} />
          <div className="container">
            <Routes>
              <Route
                exact
                path="/"
                element={<Home showAlert={showAlert} />}
              ></Route>
              <Route exact path="/userprofile" element={<UserProfile showAlert={showAlert} />}></Route>
              <Route exact path='/books' element={<Books showAlert={showAlert}/>}></Route>
              <Route exact path="/about" element={<About />}></Route>
              <Route
                exact
                path="/login"
                element={<LoginRegister showAlert={showAlert} setToken={setToken}/>}
              ></Route>
            </Routes>
          </div>
        </Router>
    </>
  );
}

export default App;
