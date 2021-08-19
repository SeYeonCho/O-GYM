import React from 'react'
import { useState, useEffect, useCallback } from 'react';
import axios from 'axios'
import ReactDOM from 'react';
import 'antd/dist/antd.css'
import { List, Avatar, Row, Col } from 'antd'
import './ProfileRightTrainer.css'

function ProfileRightTrainer() {
  const [myStudents, setMyStudents] = useState<any[]>([])
  let accessToken = localStorage.getItem('accessToken');
  let today = new Date()
  let month = today.getMonth()
  const [ gender, setGender ] = useState('')

  
  useEffect(() => {
    axios.get(`${process.env.REACT_APP_API_ROOT_ADDRESS}/api/health/mystudents`, {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      setMyStudents(response.data.data.studentHealthList)
      if (response.data.data.studentHealthList.gender === "MAN") {
        setGender("남성")
      } else {
        setGender("여성")
      }
    })
  }, [])

  return (
      <div style={{marginLeft: '1rem', marginRight: '1rem'}}>
      <List
        header={<div style={{fontWeight:700}}>교육생 목록</div>}
        style={{overflowY: "scroll", height: "500px"}}
        itemLayout="horizontal"
        dataSource={myStudents}
        renderItem={myStudent => (
          <List.Item
            actions={[<p>{myStudent.description}</p>]}
          >
            <List.Item.Meta
              avatar={<Avatar src={myStudent.profileUrl} />}
              title={<p >{myStudent.username}</p>}
            />
            <Row>
              <Col style={{marginRight: "2rem"}}>
                <p>{myStudent.heightList[month]}cm</p>
              </Col>
              <Col style={{marginRight: "2rem"}}>
                <p>{myStudent.weightList[month]}kg</p>
              </Col>
              <Col style={{marginRight: "1rem"}}>
                <p>{gender}</p>
              </Col>

            </Row>
      </List.Item>
        )}
  />
  </div>
  )
}

export default ProfileRightTrainer
