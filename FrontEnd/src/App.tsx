import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import './App.css';
import 'antd/dist/antd.css';
import MainPage from './pages/MainPage/MainPage';
import StudentReservation from './pages/StudentReservation/StudentReservation';
import LoginPage from './pages/LoginPage/LoginPage';

function App() {
  return (
    <>
      <LoginPage/>
      {/* <Router>
        <Route exact path='/' component={MainPage} />
        <Route exact path='/studentreservation' component={StudentReservation} />
        <Route exact path='/login' component={LoginPage} />
      </Router> */}
    </>
  );
}

export default App;
