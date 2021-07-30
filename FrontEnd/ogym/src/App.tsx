import React from 'react';
import './App.css';
import 'antd/dist/antd.css';
import Button from './components/atoms/Button';
import Span from './components/atoms/Span';
import Input from './components/atoms/Input';
import Label from './components/atoms/Label';
import ListItem from './components/molecules/ListItem';
import LoginPage from './pages/LoginPage/LoginPage';
import MainNavigation from './components/organisms/Main/Main-Navigation';


function App() {
  return (
    <>
    <MainNavigation />
      {/* <Button text='button' backgroundColor="blue"></Button>
      <Span text='테스트' />
      <div>
        <Label text='연습' />
        <Input type="text" />
      </div> */}
      {/* <ListItem>
        <Input type="text" value='button' ></Input>
        <Button text='button' backgroundColor="blue"></Button>

      </ListItem>
      <ButtonList>
        <Label label="테스트" fontSize="25px" color="blue" />
        <Span span="나와라" fontSize="30px"  color="green" backgroundColor="pink" />
        <Button text="button1" backgroundColor="green"></Button>
        <Button text="button2" backgroundColor="white"></Button>
        <Button text="button3" backgroundColor="yellow"></Button>
      </ButtonList>
      </ListItem> */}
      {/* <LoginPage /> */}
    </>
  );
}

export default App;
