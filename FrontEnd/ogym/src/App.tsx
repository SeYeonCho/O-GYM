import React from 'react';
import './App.css';
import 'antd/dist/antd.css';
import Button from './components/atoms/Button';
import Span from './components/atoms/Span';
import Input from './components/atoms/Input';
import Label from './components/atoms/Label';
import ListItem from './components/molecules/ListItem';
import LoginPage from './pages/LoginPage/LoginPage';
import MainPage from './pages/MainPage/MainPage';



function App() {
  return (
    <>
      <MainPage></MainPage>
    </>
  );
}

export default App;
