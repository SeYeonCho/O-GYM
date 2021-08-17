import React from 'react'
import { useState, useEffect, useCallback } from 'react';
import axios from 'axios'
import ReactDOM from 'react';
import 'antd/dist/antd.css'
import { List, Avatar } from 'antd'
import './ProfileRightTrainer.css'

function ProfileRightTrainer() {
  const [myStudents, setMyStudents] = useState<any[]>([])
  let accessToken = localStorage.getItem('accessToken');
  
  useEffect(() => {
    axios.get("https://i5b305.p.ssafy.io/api/health/mystudents", {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      console.log(response.data.data)
      setMyStudents(response.data.data.studentHealthList)
    })
  }, [])

  return (
      <>
      <List
        header={<div>교육생 목록</div>}
        style={{overflowY: "scroll", height: "500px"}}
        itemLayout="horizontal"
        dataSource={myStudents}
        renderItem={myStudents => (
          <List.Item
            actions={[<a key="myStudent-chart">건강정보</a>]}
          >
            <List.Item.Meta
              avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
              title={<a href="https://ant.design">{myStudents.username}</a>}
            />
      </List.Item>
        )}
  />
  </>
  )
}

export default ProfileRightTrainer
