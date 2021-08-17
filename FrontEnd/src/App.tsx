import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import './App.css';
import 'antd/dist/antd.css';
import MainPage from './pages/MainPage/MainPage';
import StudentReservation from './pages/StudentReservation/StudentReservation';
import LoginPage from './pages/LoginPage/LoginPage';
import Profile from './pages/Profile/Profile';
import SessionPage from './pages/WebRtcPage/SessionPage';
import TeacherReservation from './pages/TeacherReservation/TeacherReservation';

function App() {
  return (
    <>
      <Router>
        <Route exact path='/' component={MainPage} />
        <Route exact path='/studentreservation' component={StudentReservation} />
        <Route exact path='/login' component={LoginPage} />
        <Route exact path='/profile' component={Profile} />
        
        <Route exact path="/dovideo" component={SessionPage}/>
        <Route exact path='/teacherreservation' component={TeacherReservation} />
      
      
      
      
      </Router>
    </>
  );
}

export default App;