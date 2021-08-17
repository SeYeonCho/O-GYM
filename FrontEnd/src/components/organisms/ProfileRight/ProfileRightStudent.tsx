import React from 'react'
import { useState, useEffect, useCallback } from 'react';
import axios from 'axios'
import ReactDOM from 'react';
import 'antd/dist/antd.css'
import { List, Avatar } from 'antd'
import './ProfileRightStudent.css'

function ProfileRightStudent() {
  const [myReservation, setMyReservation] = useState<any[]>([])
  let accessToken = localStorage.getItem('accessToken');
  
  useEffect(() => {
    axios.get("https://i5b305.p.ssafy.io/api/pt/reservation", {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      console.log(response.data.data)
      setMyReservation(response.data.data)
    })
  }, [])

  return (
      <>
      <List
        header={<div>예약현황</div>}
        style={{overflowY: "scroll", height: "500px"}}
        itemLayout="horizontal"
        dataSource={myReservation}
        renderItem={myReservation => (
          <List.Item
            actions={[<a key="reservation-cancel">예약취소</a>]}
          >
            <List.Item.Meta
              avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
              title={<a href="https://ant.design">{myReservation.nickname}</a>}
              description={myReservation.description}
            />
            <div>{
                myReservation.reservationTime.substring(0, 4) + "년 " +
                myReservation.reservationTime.substring(5, 7) + "월 " +
                myReservation.reservationTime.substring(8, 10) + "일 " +
                myReservation.reservationTime.substring(11, 13) + "시"
              }</div>
      </List.Item>
        )}
  />
  </>
  )
}

export default ProfileRightStudent
