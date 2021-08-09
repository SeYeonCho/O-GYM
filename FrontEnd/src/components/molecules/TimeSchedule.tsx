import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import { Radio } from 'antd';

function TimeSchedule() {
  return (
    <>
      <Radio.Group defaultValue="09:00" buttonStyle="solid">
      <Radio.Button value="09:00">09:00</Radio.Button>
      <Radio.Button value="10:00">10:00</Radio.Button>
      <Radio.Button value="11:00">11:00</Radio.Button>
      <Radio.Button value="12:00">12:00</Radio.Button>
      <Radio.Button value="15:00">15:00</Radio.Button>
      <Radio.Button value="20:00">20:00</Radio.Button>
    </Radio.Group>
    </>
  )
}

export default TimeSchedule
