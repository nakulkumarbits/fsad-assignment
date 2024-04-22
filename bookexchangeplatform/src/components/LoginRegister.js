import React, {useState} from 'react'
import { useNavigate } from "react-router-dom";

export default function LoginRegister(props) {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [addressLine1, setAddressLine1] = useState('');
  const [addressLine2, setAddressLine2] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [pincode, setPincode] = useState('');

  // const [token, setToken] = useState('');
  let navigate = useNavigate();
  const handleLogin = (data) => {
    // console.log(data);
    const requestBody = {
      username,
      password
    };

    // let navigate = useNavigate();

    fetch('http://localhost:9000/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })
    .then(response => {
      if(response.ok) {
        localStorage.setItem("username", username);
        console.log('Login successful');
        props.showAlert('Login successful', "success");
        return response.text();
      } else if(response.status >=400 && response.status < 500){
        props.showAlert('Username/Password is incorrect', "danger");
        console.log('Login Failed', response);
        return 'error';
      } else {
        props.showAlert('Please try after sometime!', "danger");
        console.log('Server Error!!', response);
        return 'error';
      }
    })
    .then(res => {
      if(res === 'error') {
        console.log('Error in login');
        localStorage.removeItem("username");
        navigate("/login");
      } else {
        console.log('response : ', res);
        props.setToken(res);
        localStorage.setItem("token", res);
        navigate("/");
      }
    })
    .catch(error => {
      // Handle any network errors
      console.error('Network error:', error);
    });
  }; 

  const handleRegister = () => {
    const requestBody = {
      username,
      password,
      email,
      firstName,
      lastName,
      addressDTO : {
        addressLine1,
        addressLine2,
        city,
        state,
        pincode
      }
    };

    console.log(requestBody);

    fetch('http://localhost:9000/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })
    .then(response => {
      if(response.ok) {
        console.log('Registration successful');
        props.showAlert('Registration successful!! Proceed to Login now.', "success");
        return response.text();
      } else if(response.status === 409){
        console.log('Registration failed!!', response);
        props.showAlert('Already registered with username/email.', "danger");
      } else {
        console.log('Registration failed!!', response);
        props.showAlert('Registration failed!!', "danger");
      }
    })
    .then(res => {
      console.log('response : ', res);
      // setToken(res);
    })
    .catch(error => {
      console.error('Network error:', error);
    });
  };

  const handleInputChange = (event, setStateFunction) => {
    setStateFunction(event.target.value);
  };


  const [showLogin, setShowLogin] = useState(true); // Initially show login form
  const toggleForm = () => {
    setUsername('');  // clear username and password as common for both login and register form.
    setPassword('');

    setShowLogin((prevShowLogin) => !prevShowLogin); // Toggle the state
  };

  return (
    <>
    <div className='wrapper'>
      <div id='login-form' className={`container form-box login ${showLogin ? '' : 'hidden'}`}>
          <form>
          <div className="mb-1">
              <label htmlFor="username" className="form-label">Username</label>
              <input type="username" className="form-control" id="username" value={username} onChange={(event) => handleInputChange(event, setUsername)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="inputPassword" className="form-label">Password</label>
              <input type="password" id="inputPassword" className="form-control" value={password} onChange={(event) => handleInputChange(event, setPassword)} required/>
          </div>
          
          <button type="button" className="btn btn-primary" onClick={handleLogin}>Login</button>
          <button type="button" className="btn btn-link" onClick={toggleForm}>Register</button>
          </form>
      </div>

      <div id='register-form' className={`container form-box register ${showLogin ? 'hidden' : ''}`}>
          <form>
          <div className="mb-1">
              <label htmlFor="username" className="form-label">Username</label>
              <input type="username" className="form-control" id="username" value={username} onChange={(event) => handleInputChange(event, setUsername)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="password" className="form-label">Password</label>
              <input type="password" id="password" className="form-control" value={password} onChange={(event) => handleInputChange(event, setPassword)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="email" className="form-label">Email</label>
              <input type="email" id="email" className="form-control" value={email} onChange={(event) => handleInputChange(event, setEmail)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="firstName" className="form-label">FirstName</label>
              <input type="text" id="firstName" className="form-control" value={firstName} onChange={(event) => handleInputChange(event, setFirstName)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="lastName" className="form-label">LastName</label>
              <input type="text" id="lastName" className="form-control" value={lastName} onChange={(event) => handleInputChange(event, setLastName)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="addressLine1" className="form-label">AddressLine1</label>
              <input type="text" id="addressLine1" className="form-control"  value={addressLine1} onChange={(event) => handleInputChange(event, setAddressLine1)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="addressLine2" className="form-label">AddressLine2</label>
              <input type="text" id="addressLine2" className="form-control" value={addressLine2} onChange={(event) => handleInputChange(event, setAddressLine2)}/>
          </div>
          <div className="mb-1">
              <label htmlFor="city" className="form-label">City</label>
              <input type="text" id="city" className="form-control" value={city} onChange={(event) => handleInputChange(event, setCity)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="state" className="form-label">State</label>
              <input type="text" id="state" className="form-control" value={state} onChange={(event) => handleInputChange(event, setState)} required/>
          </div>
          <div className="mb-1">
              <label htmlFor="pincode" className="form-label">Pincode</label>
              <input type="text" id="pincode" className="form-control" value={pincode} onChange={(event) => handleInputChange(event, setPincode)} required/>
          </div>

          <button type="button" className="btn btn-primary" onClick={handleRegister}>Register</button>
          <button type="button" className="btn btn-link" onClick={toggleForm}>Login</button>
          </form>
      </div>
    </div>
    </>
  )
}