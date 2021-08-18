import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import { Radio } from 'antd';
import { Time } from '../../recoil/atoms/Reservation/Time';
import { useRecoilState } from 'recoil'
import { PossibleTime } from '../../recoil/atoms/Reservation/PossibleTime';

const timeData = [
  "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00", "19:00", "20:00", "21:00", "22:00"
]

function TimeSchedule() {
  const [ value, setValue ] = useRecoilState(Time)
  const [ possibleTime, setPossibleTime ] = useRecoilState(PossibleTime)

  function onChange (e) {
    setValue(e.target.value)
  }

  return (
    <>
      <Radio.Group onChange={onChange} value={value} buttonStyle="solid" >
        {timeData.map((td, index) => (
          !possibleTime.includes(td) && <Radio.Button value={td}>{td}</Radio.Button>  
        ))}
    </Radio.Group>
    </>
  )
}

export default TimeSchedule
