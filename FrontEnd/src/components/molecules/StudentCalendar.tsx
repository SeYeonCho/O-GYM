import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import { DatePicker, Space } from 'antd';
import { useRecoilState } from 'recoil'
import { Date } from '../../recoil/atoms/Reservation/Date';

function StudentCalendar() {
  const [ date, setDate ] = useRecoilState(Date)

  function onChange(date, dateString) {
    console.log(dateString)
    setDate(dateString)
  }

  return (
    <Space direction="vertical" size={12}>
      <DatePicker onChange={onChange}/>

    </Space>

  );
}

export default StudentCalendar;