import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import './App.css';
import 'antd/dist/antd.css';
import MainPage from './pages/MainPage/MainPage';
import StudentReservation from './pages/StudentReservation/StudentReservation';

function App() {
  return (
    <>
    {/* <StudentReservation /> */}
      {/* <MainPage></MainPage> */}
      <Router>
        <Route exact path='/' component={MainPage} />
        <Route exact path='/studentreservation' component={StudentReservation} />

      </Router>
    </>
  );
}

export default App;
