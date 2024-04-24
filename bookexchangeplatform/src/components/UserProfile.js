import React, { useEffect, useState,useRef } from "react";
import { useNavigate } from "react-router-dom";

export default function UserProfile(props) {
    const [user, setUser] = useState([])
    const [address, setAddress] = useState([])

    const loggedInUsername = localStorage.getItem("username");
    const userprofileUrl = `http://localhost:9000/userprofile/${loggedInUsername}`;

    let navigate = useNavigate();
    const count = useRef(0);
    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login");
        }

        if (count.current !== 0) {
            fetch(userprofileUrl, {
                method: 'GET',
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': localStorage.getItem('token')
                }
              }).then(response => {
                // console.log("response : ",response);
                if(response.ok) {
                    // setUser(response.text());
                    return response.json();
                } else if(response.status >=400 && response.status < 500) {
                    console.log('Auth Error!!', response);
                    return 'error';
                } else {
                    console.log('Server Error!!', response);
                    return 'error';
                }
            }).then(response => {
                if(response === 'error') {
                    console.log('Error in loading profile!');
                    localStorage.removeItem("token");
                    navigate("/login");
                } else {
                    // console.log('user profile : ', response);
                    setAddress(response.addressDTO);
                    setUser(response);
                }
            });
          }

        count.current++;
        }, []); // eslint-disable-next-line

        const [showProfile, setShowProfile] = useState(true); // Initially show profile form
        const toggleForm = () => {
            setShowProfile((prevShowProfile) => !prevShowProfile); // Toggle the state
          };

        //   console.log('user.firstName' , user.firstName);
    const [username, setUsername] = useState(user.username);
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [addressLine1, setAddressLine1] = useState('');
    const [addressLine2, setAddressLine2] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [pincode, setPincode] = useState('');
    const handleUpdateProfile = ()=> {
        const requestBody = {
            username,
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

        console.log("update user : ",requestBody);
        fetch(`http://localhost:9000/users/${loggedInUsername}`,{
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify(requestBody)
        }).then(response => {
            console.log("update profile response : ",response);
            if(response.ok) {
                // setUser(response.text());
                return response.json();
            } else {
                console.log('Server Error!!', response);
                return 'error';
            }
        }).then(response => {
            if(response === 'error') {
                console.log('Error in loading profile!');
                navigate("/");
            } else {
                // console.log('user profile : ', response);
                setAddress(response.addressDTO);
                setUser(response);
                props.showAlert('Profile updated successfully!!', "success");
                toggleForm();
            }
        });
    };

    const handleInputChange = (event, setStateFunction) => {
        setStateFunction(event.target.value);
      };

  return (
    <div className="container">
      <h2>User Profile</h2>
      <div className={`${showProfile ? '' : 'hidden'}`}>
      <table className="table table-hover table-sm userprofile">
        <tbody>
        <tr>
            <th scope="col">First Name</th><td>{user.firstName}</td>
        </tr>
        <tr>
            <th scope="col">Last Name</th><td>{user.lastName}</td>
        </tr>
        <tr>
            <th scope="col">Email</th><td>{user.email}</td>
        </tr>
        <tr>
            <th scope="col">Username</th><td>{user.username}</td>
        </tr>
        <tr>
            <th scope="col">Address Line1</th><td>{address.addressLine1}</td>
        </tr>
        <tr>
            <th scope="col">Address Line2</th><td>{address.addressLine2}</td>
        </tr>
        <tr>
            <th scope="col">City</th><td>{address.city}</td>
        </tr>
        <tr>
            <th scope="col">State</th><td>{address.state}</td>
        </tr>
        <tr>
            <th scope="col">Pincode</th><td>{address.pincode}</td>
        </tr>

        </tbody>
      </table>
      <button type="button" className="btn btn-primary" onClick={toggleForm}>Edit</button>
      </div>

      
      <form className={`container ${showProfile ? 'hidden' : ''}`}>
        <table className="table table-hover table-sm userprofile">
        <tbody>
        <tr>
            <th scope="col">First Name</th>
            <td><input type="text" id="firstName" className="form-control" value={firstName || user.firstName} onChange={(event) => handleInputChange(event, setFirstName)} required/></td>
        </tr>
        <tr>
            <th scope="col">Last Name</th>
            <td><input type="text" id="lastName" className="form-control" value={lastName || user.lastName} onChange={(event) => handleInputChange(event, setLastName)} required/></td>
        </tr>
        <tr>
            <th scope="col">Email</th>
            <td><input type="email" id="email" className="form-control" value={email || user.email} onChange={(event) => handleInputChange(event, setEmail)} required/></td>
        </tr>
        <tr>
            <th scope="col">Username</th>
            <td><input type="username" className="form-control" id="username" value={username || user.username} onChange={(event) => handleInputChange(event, setUsername)} required/></td>
        </tr>
        <tr>
            <th scope="col">Address Line1</th>
            <td><input type="text" id="addressLine1" className="form-control"  value={addressLine1 || address.addressLine1} onChange={(event) => handleInputChange(event, setAddressLine1)} required/></td>
        </tr>
        <tr>
            <th scope="col">Address Line2</th>
            <td><input type="text" id="addressLine2" className="form-control" value={addressLine2 || address.addressLine2} onChange={(event) => handleInputChange(event, setAddressLine2)}/></td>
        </tr>
        <tr>
            <th scope="col">City</th>
            <td><input type="text" id="city" className="form-control" value={city || address.city} onChange={(event) => handleInputChange(event, setCity)} required/></td>
        </tr>
        <tr>
            <th scope="col">State</th>
            <td><input type="text" id="state" className="form-control" value={state || address.state} onChange={(event) => handleInputChange(event, setState)} required/></td>
        </tr>
        <tr>
            <th scope="col">Pincode</th>
            <td><input type="text" id="pincode" className="form-control" value={pincode || address.pincode} onChange={(event) => handleInputChange(event, setPincode)} required/></td>
        </tr>

        </tbody>
      </table>

        <button type="button" className="btn btn-primary" onClick={handleUpdateProfile}>Update Profile</button>
        <button type="button" className="btn btn-link" onClick={toggleForm}>Cancel</button>
      </form>

    </div>
  )
}
