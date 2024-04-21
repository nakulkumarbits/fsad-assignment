import Search from "./Search";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Home(props) {
    let navigate = useNavigate();
    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login");
        }
      }, []); // eslint-disable-line react-hooks/exhaustive-deps
  return (
    <div className="container my-3">
      <Search showAlert={props.showAlert}/>
    </div>
  );
}

export default Home;
