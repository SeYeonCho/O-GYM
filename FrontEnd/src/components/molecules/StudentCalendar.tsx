import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import { DatePicker, Space } from 'antd';


function StudentCalendar() {

  return (
    <Space direction="vertical" size={12}>
      <DatePicker renderExtraFooter={() => 'extra footer'} />

    </Space>

  );
}

export default StudentCalendar;