import React from "react";
function About() {
  return (
    <div>
      <h1>About Us </h1>
      <p>
        This Book Exchange Platform has been developed as part of Assignment of Full Stack Application Development.
        Frontend is developed using Reactjs.
        Backend has been implemented in Java and Spring Boot.
        For more details please refer Github.
      </p>
      <p>
        This page is accessible without any Authentication and Authorization.
      </p>
      <ul>
        <li>
          User service:
          <ul>
            <li>Github link: https://github.com/nakulkumarbits/fsad-assignment/tree/main/userservice</li>
          </ul>
        </li>
        <li>
          Book service:
          <ul>
            <li>Github link: https://github.com/nakulkumarbits/fsad-assignment/tree/main/bookservice</li>
          </ul>
        </li>
        <li>
          BookExchangePlatform:
          <ul>
            <li>Github link: https://github.com/nakulkumarbits/fsad-assignment/tree/main/bookexchangeplatform</li>
          </ul>
        </li>
        <li>
          Google drive: 
          <ul>
            <li>
              Demonstration videos: https://drive.google.com/file/d/12hFE-L1aYP6cOGm7_po2G2ZkU-A3myzE/view?usp=drive_link
            </li>
          </ul>
        </li>
      </ul>
    </div>
  );
}

export default About;
