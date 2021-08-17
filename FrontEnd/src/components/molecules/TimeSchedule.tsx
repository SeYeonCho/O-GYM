import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import { Radio } from 'antd';
import { Time } from '../../recoil/atoms/Reservation/Time';
import { useRecoilState } from 'recoil'

function TimeSchedule() {
  const [ value, setValue ] = useRecoilState(Time)
  function onChange (e) {
    console.log(e.target.value)
    setValue(e.target.value)
  }

  return (
    <>
      <Radio.Group onChange={onChange} value={value} buttonStyle="solid" >
        <Radio.Button value="00:00">09:00</Radio.Button>
        <Radio.Button value="01:00">10:00</Radio.Button>
        <Radio.Button value="02:00">11:00</Radio.Button>
        <Radio.Button value="03:00">12:00</Radio.Button>
        <Radio.Button value="06:00">15:00</Radio.Button>
        <Radio.Button value="11:00">20:00</Radio.Button>
    </Radio.Group>
    </>
  )
}

export default TimeSchedule
