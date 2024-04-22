import React, { useEffect, useState,useRef } from "react";
import { useNavigate } from "react-router-dom";

export default function UserProfile() {
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
                console.log("response : ",response);
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
                    console.log('user profile : ', response);
                    setAddress(response.addressDTO);
                    setUser(response);
                }
            });
          }

        count.current++;
        }, []); // eslint-disable-next-line

  return (
    <div>
      <h2>User Profile</h2>
      <table className="table table-hover table-sm">
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
    </div>
  )
}
